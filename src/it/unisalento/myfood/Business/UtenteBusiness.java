package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.Command.Utente.EditUtente;
import it.unisalento.myfood.Business.Command.Utente.IUtenteOperation;
import it.unisalento.myfood.Business.Command.Utente.RemoveUtente;
import it.unisalento.myfood.Business.Decorator.Login.*;
import it.unisalento.myfood.Business.Decorator.SignUp.*;
import it.unisalento.myfood.Business.Security.AbstractFactory.StrategyFactory;
import it.unisalento.myfood.Business.Security.Strategy.PasswordHashingContext;
import it.unisalento.myfood.DAO.InterazioneUtenteDAO;
import it.unisalento.myfood.DAO.OrdineDAO;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.Composite.CommentoCliente;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.*;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Result.IResult;
import it.unisalento.myfood.model.Result.LoginResult;
import it.unisalento.myfood.model.Result.OperationUtenteResult;
import it.unisalento.myfood.model.Result.SignUpResult;

import java.security.SecureRandom;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtenteBusiness {   //undo e redo sono state implementate solo per le operazioni di rimozione e disabilitazione utente

    // L'ho creato veramente su gmail
    public static final String EMAIL_CLIENTE_UNIVERSALE = "clienteuniversale@gmail.com";

    private static UtenteBusiness instance = new UtenteBusiness();

    private static HashMap<String, Object> session = new HashMap<>();

    public final static String LOGGED_IN_USER_ID = "LOGGED_IN_USER";
    public final static String CART = "CART";
    public final static String SELECTED_OBJECT = "SELECTED_OBJECT"; //serve a tenere in sessione l'oggetto che si visualizza (articolo, ordine, commento ecc.)
    public final static String OPERATION = "OPERATION";   //serve a indicare agli EditPanel l'operazione che si vuole eseguire
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_!%$&";
    private static final int FIRST_PASSWORD_LENGTH = 16;

    public enum OPERATIONS {
        AGGIUNGI, MODIFICA, VISUALIZZA
    }

    public enum CAMPO{
        NOME, COGNOME, EMAIL, TELEFONO, DATADINASCITA, PROFESSIONE, RESIDENZA, DATAREGISTRAZIONE
    }

    public static final String GUEST = "GUEST";

    public UtenteDAO UDAO = UtenteDAO.getInstance();

    private IResult loginResult;

    private IResult signUpResult;

    private OperationUtenteResult operationUtenteResult;

    private Stack<IUtenteOperation> undoStack = new Stack<>();
    private Stack<IUtenteOperation> redoStack = new Stack<>();
    private Integer MAX_CTRL_Z = 10;
    private String message;

    private UtenteBusiness() {
    }

    public static UtenteBusiness getInstance() {
        return instance;
    }

    public boolean isLoggedCliente() {
        return ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID)).getRuolo() == Utente.RUOLO.CLIENTE;
    }

    public boolean isLoggedCucina() {
        return ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID)).getRuolo() == Utente.RUOLO.CUCINA;
    }

    public boolean isLoggedAmministratore() {
        return ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID)).getRuolo() == Utente.RUOLO.AMMINISTRATORE;
    }

    public boolean isLoggedGuest() {
        return ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID)).getRuolo() == Utente.RUOLO.GUEST;
    }

    //isTest serve a non mandare la mail nel caso in cui si stiano effettuando dei test per evitare di mandare email a raffica e ricevere un ban sull'indirizzo mail
    public void signUpCliente(String email, String nome, String cognome, String telefono, String dataDiNascitaString, String professione, String residenza, Boolean isTest) throws InvalidFormatException {
        SignUpUtente signUpUtente = new SignUpUtente(email, nome, cognome, telefono, dataDiNascitaString, professione, isTest);
        SignUp signUpCliente = new SignUpClienteDecorator(signUpUtente, residenza);
        signUpResult = signUpCliente.esegui();
        message = signUpResult.getMessage();
    }

    public void signUpCucina(String email, String nome, String cognome, String telefono, String dataDiNascitaString, String professione, Boolean isTest) throws InvalidFormatException {
        SignUpUtente signUpUtente = new SignUpUtente(email, nome, cognome, telefono, dataDiNascitaString, professione, isTest);
        SignUp signUpCucina = new SignUpCucinaDecorator(signUpUtente);
        signUpResult = signUpCucina.esegui();
        message = signUpResult.getMessage();
    }

    public void signUpAmministratore(String email, String nome, String cognome, String telefono, String dataDiNascitaString, String professione, Boolean isTest) throws InvalidFormatException {
        SignUpUtente signUpUtente = new SignUpUtente(email, nome, cognome, telefono, dataDiNascitaString, professione, isTest);
        SignUp signUpAmministratore = new SignUpAmministratoreDecorator(signUpUtente);
        signUpResult = signUpAmministratore.esegui();
        message = signUpResult.getMessage();
    }

    public void login(){
        LoginUtente loginUtente = new LoginUtente();
        LoginGuestDecorator loginGuestDecorator = new LoginGuestDecorator(loginUtente);
        loginResult = loginGuestDecorator.esegui();
        message = loginResult.getMessage();

    }

    public void login(String email, String password) {
        // Controlla se esiste l'utente
        if (!UDAO.userExists(email)) {
            LoginResult lr = new LoginResult();
            lr.setLoginResult(LoginResult.LOGIN_RESULT.USER_DOESNT_EXIT);
            lr.setMessage("Non esiste un utente con questa email!");
            loginResult = lr;
            message = loginResult.getMessage();
            return;
        }

        LoginUtente loginUtente = new LoginUtente();
        Utente utente = UDAO.findByEmail(email);

        if (UDAO.isCliente(utente.getId())) {
            LoginClienteDecorator loginClienteDecorator = new LoginClienteDecorator(loginUtente, email, password);
            loginResult = loginClienteDecorator.esegui();
        } else if (UDAO.isAmministratore(utente.getId())) {
            LoginAmministratoreDecorator loginAmministratoreDecorator = new LoginAmministratoreDecorator(loginUtente, email, password);
            loginResult = loginAmministratoreDecorator.esegui();
        } else if (UDAO.isCucina(utente.getId())) {
            LoginCucinaDecorator loginCucinaDecorator = new LoginCucinaDecorator(loginUtente, email, password);
            loginResult = loginCucinaDecorator.esegui();
        }

        message = loginResult.getMessage();
    }

    public boolean loggedUtenteHasPurchasedArticolo() {
        IArticolo viewArticolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente loggedUtente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER_ID);

        return UtenteDAO.getInstance().checkIfUtenteHasPurchasedArticolo(loggedUtente, viewArticolo);
    }

    public boolean loggedUtenteHasCommentedArticolo() {
        IArticolo viewArticolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente loggedUtente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER_ID);

        return UtenteDAO.getInstance().checkIfUtenteHasCommentedArticolo(loggedUtente, viewArticolo);
    }

    public String getLoggedInName(){
        return ((Utente) session.get(LOGGED_IN_USER_ID)).getNome();
    }

    public String getLoggedInCognome(){
        return ((Utente) session.get(LOGGED_IN_USER_ID)).getCognome();
    }

    public static HashMap<String, Object> getSession() {
        return session;
    }

    public boolean isFirstAccess(String email) {
        return UtenteDAO.getInstance().isFirstAccess(email);
    }

    public boolean changePasswordForLoggedUser(String newPassword) {
        Utente loggedUser = (Utente) session.get(LOGGED_IN_USER_ID);

        String saltHex = loggedUser.getSaltHex();

        StrategyFactory strategyFactory = new StrategyFactory();

        PasswordHashingContext passwordHashingContext = new PasswordHashingContext(strategyFactory.getStrategy("SHA512"));
        String hashedPassword = passwordHashingContext.executeStrategy(newPassword, saltHex);

        return UtenteDAO.getInstance().changePassword(loggedUser.getEmail(), hashedPassword);
    }

    public void setChangePasswordForLoggedUser(boolean b) {
        Utente loggedUser = (Utente) session.get(LOGGED_IN_USER_ID);

        UtenteDAO.getInstance().setCambiaPassword(loggedUser.getEmail(), b);

        loggedUser.setCambiaPassword(false);

        session.replace(LOGGED_IN_USER_ID, loggedUser);
    }

    public String getMessage() {
        return message;
    }

    public boolean isUserDoesntExist(){
        return LoginResult.LOGIN_RESULT.USER_DOESNT_EXIT.equals(((LoginResult)loginResult).getLoginResult());
    }

    public boolean isWrongPassword(){
        return LoginResult.LOGIN_RESULT.WRONG_PASSWORD.equals(((LoginResult)loginResult).getLoginResult());
    }

    public boolean isLoginOk(){
        return LoginResult.LOGIN_RESULT.LOGIN_OK.equals(((LoginResult)loginResult).getLoginResult());
    }

    public boolean isUserBlocked(){
        return LoginResult.LOGIN_RESULT.USER_BLOCKED.equals(((LoginResult)loginResult).getLoginResult());
    }

    public boolean isUserAlreadyExit(){
        return SignUpResult.SIGN_UP_RESULT.USER_ALREADY_EXISTS.equals(((SignUpResult)signUpResult).getSignUpResult());
    }

    public boolean isUsedEmailReserved() {
        return SignUpResult.SIGN_UP_RESULT.USED_EMAIL_IS_RESERVED.equals(((SignUpResult)signUpResult).getSignUpResult());
    }

    public boolean isSignedUpSuccessfully(){
        return SignUpResult.SIGN_UP_RESULT.SIGNED_UP_SUCCESFULLY.equals(((SignUpResult)signUpResult).getSignUpResult());
    }

    public Object[][] loadClientiTable() {
        ArrayList<Utente> allClienti = UtenteDAO.getInstance().findAllClienti();
        allClienti.removeIf(u -> u.getEmail().equals(EMAIL_CLIENTE_UNIVERSALE));

        Object[][] clientiTable = new Object[allClienti.size()][4];

        for (int i = 0; i < allClienti.size() ; i++) {
            Utente u = allClienti.get(i);

            clientiTable[i][0] = u.getId();
            clientiTable[i][1] = u.getNome() + " " + u.getCognome();
            clientiTable[i][2] = u.getDataNascita().toString();
            clientiTable[i][3] = (u.isDisabilitato() ? "Si" : "No");
        }

        return clientiTable;
    }

    public Object[][] loadAmministratoriTable() {
        ArrayList<Utente> allAdmins = UtenteDAO.getInstance().findAllAmministratori();

        Object[][] adminTable = new Object[allAdmins.size()][4];

        for (int i = 0; i < allAdmins.size(); i++) {
            Utente u = allAdmins.get(i);

            adminTable[i][0] = u.getId();
            adminTable[i][1] = u.getNome() + " " + u.getCognome();
            adminTable[i][2] = u.getEmail();
            adminTable[i][3] = u.getProfessione();
        }

        return adminTable;
    }

    public Object[][] loadCucinaTable() {
        ArrayList<Utente> allCucina = UtenteDAO.getInstance().findAllCucina();

        Object[][] cucinaTable = new Object[allCucina.size()][4];

        for (int i = 0; i < allCucina.size(); i++) {
            Utente u = allCucina.get(i);

            cucinaTable[i][0] = u.getId();
            cucinaTable[i][1] = u.getNome() + " " + u.getCognome();
            cucinaTable[i][2] = u.getEmail();
            cucinaTable[i][3] = u.getProfessione();
        }

        return cucinaTable;
    }

    public void setSelectedUtente(Integer idUtente) {
        session.putIfAbsent(SELECTED_OBJECT, UtenteDAO.getInstance().findById(idUtente));

        session.replace(SELECTED_OBJECT, UtenteDAO.getInstance().findById(idUtente));
    }

    public void removeUtente(Integer idUtente){
        Utente u = UDAO.findById(idUtente);
        Utente clienteUniversale = UtenteDAO.getInstance().findByEmail(EMAIL_CLIENTE_UNIVERSALE);

        // Se l'utente ha fatto ordini o commenti, questi ultimi verranno assegnati all'utente universale
        if (u.getRuolo().equals(Utente.RUOLO.CLIENTE)) {
            if (!u.getOrdini().isEmpty()) {
                ArrayList<Ordine> ordini = u.getOrdini();

                for (Ordine o : ordini) {
                    o.setIdCliente(clienteUniversale.getId());
                    boolean done = OrdineDAO.getInstance().updateOrdine(o);
                    if (!done) {
                        System.out.println("ERRORE: Update Ordine fallito!");
                        return;
                    }
                }
            }

            if (!u.getInterazioni().isEmpty()) {
                ArrayList<IInterazioneUtente> interazioniUtente = u.getInterazioni();

                for (IInterazioneUtente iu : interazioniUtente) {
                    ((CommentoCliente) iu).setCliente(clienteUniversale);
                    boolean done = InterazioneUtenteDAO.getInstance().updateCommento((CommentoCliente) iu, iu.getTesto(), ((CommentoCliente) iu).getIndiceDiGradimento());
                    if (!done) {
                        System.out.println("ERRORE: Update Commento fallito!");
                        return;
                    }
                }
            }
        }

        IUtenteOperation utenteOperation = new RemoveUtente(u);
        operationUtenteResult = utenteOperation.execute();

        if(isRemovedSuccessfully()) {
            undoStack.push(utenteOperation);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }
        message = operationUtenteResult.getMessage();
    }

    public void editUtente(Integer idUtente, String email, String nome, String cognome, String telefono, String dataDiNascitaString, String professione, String residenza) throws InvalidFormatException {
        Utente utente = UDAO.findById(idUtente);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dataDiNascita;

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

        Utente newUtente = new Utente(nome, cognome, email, utente.getHashedPassword(), utente.getRuolo(), dataDiNascita, telefono, utente.getDataRegistrazione(), utente.isCambiaPassword(), utente.isDisabilitato(), residenza, professione, utente.getOrdini(), utente.getInterazioni(), utente.getSaltHex());

        IUtenteOperation editUtente = new EditUtente(newUtente);
        operationUtenteResult = editUtente.execute();
        message = operationUtenteResult.getMessage();

        if(isEditedSuccessfully()) {
            undoStack.push(editUtente);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }
    }

    public boolean isSelectedUtenteDisabilitato() {
        return ((Utente) session.get(SELECTED_OBJECT)).isDisabilitato();
    }

    public void setSelectedUtenteDisabilitato(boolean b) {
        Utente u = (Utente) session.get(SELECTED_OBJECT);
        u.setDisabilitato(b);

        IUtenteOperation editUtente = new EditUtente(u);

        operationUtenteResult = editUtente.execute();
        message = operationUtenteResult.getMessage();

        if(isEditedSuccessfully()) {
            undoStack.push(editUtente);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

        session.replace(SELECTED_OBJECT, u);
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

    public void resetPasswordSelectedUtente(boolean b) {
        Utente u = (Utente) session.get(SELECTED_OBJECT);

        StrategyFactory strategyFactory = new StrategyFactory();

        String tempPassword = generateFirstPassword();

        PasswordHashingContext passwordHashingContext = new PasswordHashingContext(strategyFactory.getStrategy("SHA512"));
        String hashedPassword = passwordHashingContext.executeStrategy(tempPassword, u.getSaltHex());

        u.setHashedPassword(hashedPassword);

        u.setCambiaPassword(b);

        IUtenteOperation editUtente = new EditUtente(u);

        operationUtenteResult = editUtente.execute();
        message = operationUtenteResult.getMessage();

        if (isEditedSuccessfully()) {
            MailSender.getInstance().sendOnlyText(
                    u.getEmail(),
                    "Credenziali temporanee, reset password",
                    "Credenziali:\nusername: " + u.getEmail() + "\nPassword: " + tempPassword + "\nTi ricordiamo che dovrai cambiare password al primo accesso!\n\nBuoni acquisti!\nTeam MyFood"
            );
        }
        session.replace(SELECTED_OBJECT, u);
    }



    public boolean isSelectedUtenteCliente() {
        return ((Utente) session.get(SELECTED_OBJECT)).getRuolo().equals(Utente.RUOLO.CLIENTE);
    }

    public Object getCampoSelectedUtente(CAMPO campo){
        Utente utente = (Utente) session.get(SELECTED_OBJECT);

        switch (campo) {
            case NOME -> {
                return utente.getNome();
            }
            case COGNOME -> {
                return utente.getCognome();
            }
            case EMAIL -> {
                return utente.getEmail();
            }
            case TELEFONO -> {
                return utente.getTelefono();
            }
            case RESIDENZA -> {
                return utente.getResidenza();
            }
            case PROFESSIONE -> {
                return utente.getProfessione();
            }
            case DATADINASCITA -> {
                return utente.getDataNascita().toString();
            }
            case DATAREGISTRAZIONE -> {
                return utente.getDataRegistrazione().toString();
            }
        }
        System.out.println("campo non gestito");
        return null;

    }

    public boolean isAddedSuccessfully(){
        return OperationUtenteResult.EDIT_UTENTE_RESULT.CREATED_SUCCESSFULLY.equals(operationUtenteResult.getEditUtenteResult());
    }

    public boolean isRemovedSuccessfully(){
        return OperationUtenteResult.EDIT_UTENTE_RESULT.REMOVED_SUCCESSFULLY.equals(operationUtenteResult.getEditUtenteResult());
    }
    public boolean isEditedSuccessfully(){
        return OperationUtenteResult.EDIT_UTENTE_RESULT.EDITED_SUCCESSFULLY.equals(operationUtenteResult.getEditUtenteResult());
    }

    public boolean isError(){
        return OperationUtenteResult.EDIT_UTENTE_RESULT.ERROR.equals(operationUtenteResult.getEditUtenteResult());
    }

    public boolean undo(){
        if (!undoStack.isEmpty()) {
            IUtenteOperation operation = undoStack.pop();
            operationUtenteResult = operation.undo();
            message = operationUtenteResult.getMessage();
            redoStack.push(operation);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if (!redoStack.isEmpty()) {
            IUtenteOperation operation = redoStack.pop();
            operationUtenteResult = operation.execute();
            message = operationUtenteResult.getMessage();
            undoStack.push(operation);
            return true;
        }
        return false;
    }


    public boolean isIdOfAdminLogged(Integer idUtente) {
        return  ((Utente) session.get(LOGGED_IN_USER_ID)).getId().equals(idUtente);
    }
}
