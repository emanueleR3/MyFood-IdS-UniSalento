package it.unisalento.myfood.Business.Decorator.SignUp;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.Business.MailSender;
import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.Business.Security.AbstractFactory.StrategyFactory;
import it.unisalento.myfood.Business.Security.Strategy.PasswordHashingContext;
import it.unisalento.myfood.Business.Security.Strategy.SHA512Hashing;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.SignUpResult;
import it.unisalento.myfood.model.Utente;

import java.security.SecureRandom;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpUtente extends SignUp{
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_!%$&";
    private static final int FIRST_PASSWORD_LENGTH = 16;

    private String email;
    private String nome;
    private String cognome;
    private String telefono;
    private String dataDiNascitaString;
    private String professione;
    private IResultFactory resultFactory;

    public SignUpUtente (String email, String nome, String cognome, String telefono, String dataDiNascitaString, String professione){
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.dataDiNascitaString = dataDiNascitaString;
        this.professione = professione;
        utente = new Utente();
        resultFactory = new ResultFactory("SignUp");
    }

    public String generateFirstPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(FIRST_PASSWORD_LENGTH);

        for (int i = 0; i < FIRST_PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    private String generateUniqueSalt() {
        UtenteDAO utenteDAO = UtenteDAO.getInstance();

        String salt = SHA512Hashing.generateSalt();
        while (utenteDAO.saltExists(salt)) {
            salt = SHA512Hashing.generateSalt();
        }

        return salt;
    }

    @Override
    public SignUpResult esegui() throws InvalidFormatException{
        UtenteDAO utenteDAO = UtenteDAO.getInstance();
        SignUpResult signUpResult = (SignUpResult) resultFactory.crea();
        Date dataDiNascita = null;

        if(email.isEmpty() || nome.isEmpty() || cognome.isEmpty() || telefono.isEmpty() || professione.isEmpty())
            throw new InvalidFormatException("Uno o più campi vuoti");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dataDiNascita = new Date(dateFormat.parse(dataDiNascitaString).getTime());
        } catch (ParseException e) {
            throw new InvalidFormatException("Formato data non valido");
        }
        //espressione regolare email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        // Crea il matcher con l'input dell'indirizzo email
        Matcher matcher = pattern.matcher(email);

        // Verifica il formato dell'indirizzo email
        if(!matcher.matches())
            throw new InvalidFormatException("Formato email non valido");

        if(!telefono.matches("\\d+"))//espressione regolare valore numerici
            throw new InvalidFormatException("Formato telefono non valido");

        if (utenteDAO.userExists(email)) {
            signUpResult.setSignUpResult(SignUpResult.SIGN_UP_RESULT.USER_ALREADY_EXISTS);
            signUpResult.setMessage("Esiste già un utente con questa e-mail: " + email);

            return signUpResult;
        }

        utente.setEmail(email);
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setTelefono(telefono);
        utente.setDataNascita(dataDiNascita);
        utente.setProfessione(professione);
        utente.setOrdini(new ArrayList<>());
        utente.setInterazioni(new ArrayList<>());
        utente.setDisabilitato(false);
        utente.setCambiaPassword(true);


        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);
        utente.setDataRegistrazione(sqlDate);

        // Affinchè il salt sia unico
        String salt = generateUniqueSalt();
        utente.setSaltHex(salt);

        String firstPassword = generateFirstPassword();
        System.out.println(firstPassword);  //Lo lasciamo per comodità

        StrategyFactory strategyFactory = new StrategyFactory();

        PasswordHashingContext passwordHashingContext = new PasswordHashingContext(strategyFactory.getStrategy("SHA512"));
        String hashedPassword = passwordHashingContext.executeStrategy(firstPassword, salt);

        utente.setHashedPassword(hashedPassword);


        boolean done = utenteDAO.addUtente(utente);
        if (done) {
            signUpResult.setSignUpResult(SignUpResult.SIGN_UP_RESULT.SIGNED_UP_SUCCESFULLY);
            signUpResult.setMessage("Cliente registrato con successso!");

            MailSender.getInstance().sendOnlyText(
                utente.getEmail(),
                "Credenziali primo accesso",
                "Credenziali:\nusername: " + utente.getEmail() + "\nPassword: " + firstPassword + "\nTi ricordiamo che dovrai cambiare password al primo accesso!\n\nBuoni acquisti!\nTeam MyFood"
            );
        }

        return signUpResult;
    }
}
