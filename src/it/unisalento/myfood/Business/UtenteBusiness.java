package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.Decorator.Login.*;
import it.unisalento.myfood.Business.Decorator.SignUp.*;
import it.unisalento.myfood.Business.Security.AbstractFactory.StrategyFactory;
import it.unisalento.myfood.Business.Security.Strategy.PasswordHashingContext;

import it.unisalento.myfood.DAO.UtenteDAO;

import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.IResult;
import it.unisalento.myfood.model.LoginResult;
import it.unisalento.myfood.model.SignUpResult;
import it.unisalento.myfood.model.Utente;

import java.util.ArrayList;
import java.util.HashMap;

public class UtenteBusiness {

    private static UtenteBusiness instance = new UtenteBusiness();

    private static HashMap<String, Object> session = new HashMap<>();

    public final static String LOGGED_IN_USER = "LOGGED_IN_USER";
    public final static String CART = "CART";       // TODO: va bene il carrello nell'UtenteBusiness come ha fatto Vergallo?
    public final static String SELECTED_OBJECT = "SELECTED_OBJECT"; //serve a tenere in sessione l'oggetto che si visualizza (articolo, ordine, commento ecc.)

    public static final String GUEST = "GUEST";

    public static  UtenteDAO UDAO = UtenteDAO.getInstance();

    private static IResult loginResult;

    private static IResult signUpResult;

    private static String message;


    private UtenteBusiness() {
    }

    public static UtenteBusiness getInstance() {
        return instance;
    }

    public boolean isLoggedCliente() {
        return ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER)).getRuolo() == Utente.RUOLO.CLIENTE;
    }

    public boolean isLoggedCucina() {
        return ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER)).getRuolo() == Utente.RUOLO.CUCINA;
    }

    public boolean isLoggedAmministratore() {
        return ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER)).getRuolo() == Utente.RUOLO.AMMINISTRATORE;
    }

    public boolean isLoggedGuest() {
        return ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER)).getRuolo() == Utente.RUOLO.GUEST;
    }

    public void signUpCliente(String email, String nome, String cognome, String telefono, String dataDiNascitaString, String professione, String residenza) throws InvalidFormatException {
        SignUpUtente signUpUtente = new SignUpUtente(email, nome, cognome, telefono, dataDiNascitaString, professione);
        SignUp signUpCliente = new SignUpClienteDecorator(signUpUtente, residenza);
        signUpResult = signUpCliente.esegui();
        message = signUpResult.getMessage();
    }

    public void signUpCucina(String email, String nome, String cognome, String telefono, String dataDiNascitaString, String professione) throws InvalidFormatException {
        SignUpUtente signUpUtente = new SignUpUtente(email, nome, cognome, telefono, dataDiNascitaString, professione);
        SignUp signUpCucina = new SignUpCucinaDecorator(signUpUtente);
        signUpResult = signUpCucina.esegui();
        message = signUpResult.getMessage();
    }

    public void signUpAmministratore(String email, String nome, String cognome, String telefono, String dataDiNascitaString, String professione) throws InvalidFormatException {
        SignUpUtente signUpUtente = new SignUpUtente(email, nome, cognome, telefono, dataDiNascitaString, professione);
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
            lr.setMessage("L'utente non esiste");
            loginResult = lr;
            message = "Non esiste un utente con questa email!";
            return;
        }

        LoginUtente loginUtente = new LoginUtente();
        Utente utente = UDAO.findByEmail(email);
        if(UDAO.isCliente(utente.getId())){
            LoginClienteDecorator loginClienteDecorator = new LoginClienteDecorator(loginUtente, email, password);
            loginResult = loginClienteDecorator.esegui();
            message = loginResult.getMessage();
        } else if(UDAO.isAmministratore(utente.getId())){
            LoginAmministratoreDecorator loginAmministratoreDecorator = new LoginAmministratoreDecorator(loginUtente, email, password);
            loginResult = loginAmministratoreDecorator.esegui();
            message = loginResult.getMessage();
        } else if(UDAO.isCucina(utente.getId())){
            LoginCucinaDecorator loginCucinaDecorator = new LoginCucinaDecorator(loginUtente, email, password);
            loginResult = loginCucinaDecorator.esegui();
            message = loginResult.getMessage();
        }
    }

    public boolean loggedUtenteHasPurchasedArticolo() {
        IArticolo viewArticolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente loggedUtente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER);

        return UtenteDAO.getInstance().checkIfUtenteHasPurchasedArticolo(loggedUtente, viewArticolo);
    }

    public boolean loggedUtenteHasCommentedArticolo() {
        IArticolo viewArticolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente loggedUtente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER);

        return UtenteDAO.getInstance().checkIfUtenteHasCommentedArticolo(loggedUtente, viewArticolo);
    }

    public String getLoggedInName(){
        return ((Utente) session.get(LOGGED_IN_USER)).getNome();
    }

    public String getLoggedInCognome(){
        return ((Utente) session.get(LOGGED_IN_USER)).getCognome();
    }

    public static HashMap<String, Object> getSession() {
        return session;
    }

    public boolean isFirstAccess(String email) {
        return UtenteDAO.getInstance().isFirstAccess(email);
    }

    public boolean changePasswordForLoggedUser(String newPassword) {
        Utente loggedUser = (Utente) session.get(LOGGED_IN_USER);

        String saltHex = loggedUser.getSaltHex();

        StrategyFactory strategyFactory = new StrategyFactory();

        //TODO: spostare nel business: il dao usa il business
        PasswordHashingContext passwordHashingContext = new PasswordHashingContext(strategyFactory.getStrategy("SHA512"));
        String hashedPassword = passwordHashingContext.executeStrategy(newPassword, saltHex);

        return UtenteDAO.getInstance().changePassword(loggedUser.getEmail(), hashedPassword);
    }

    public void setChangePasswordForLoggedUser(boolean b) {
        Utente loggedUser = (Utente) session.get(LOGGED_IN_USER);

        UtenteDAO.getInstance().setCambiaPassword(loggedUser.getEmail(), b);

        loggedUser.setCambiaPassword(false);

        session.replace(LOGGED_IN_USER, loggedUser);
    }

    public static String getMessage() {
        return message;
    }

    public static boolean isUserDoesntExist(){
        return LoginResult.LOGIN_RESULT.USER_DOESNT_EXIT.equals(((LoginResult)loginResult).getLoginResult());
    }

    public static boolean isWrongPassword(){
        return LoginResult.LOGIN_RESULT.WRONG_PASSWORD.equals(((LoginResult)loginResult).getLoginResult());
    }

    public static boolean isLoginOk(){
        return LoginResult.LOGIN_RESULT.LOGIN_OK.equals(((LoginResult)loginResult).getLoginResult());
    }

    public static boolean isUserBlocked(){
        return LoginResult.LOGIN_RESULT.USER_BLOCKED.equals(((LoginResult)loginResult).getLoginResult());
    }

    public static boolean isUserAlreadyExit(){
        return SignUpResult.SIGN_UP_RESULT.USER_ALREADY_EXISTS.equals(((SignUpResult)signUpResult).getSignUpResult());
    }

    public static boolean isSignedUpSuccessfully(){
        return SignUpResult.SIGN_UP_RESULT.SIGNED_UP_SUCCESFULLY.equals(((SignUpResult)signUpResult).getSignUpResult());
    }

    public Object[][] loadClientiTable() {
        ArrayList<Utente> allClienti = UtenteDAO.getInstance().findAllClienti();

        Object[][] clientiTable = new Object[allClienti.size()][4];

        for (int i = 0; i < allClienti.size(); i++) {
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

    public boolean isViewUtenteDisabilitato() {
        return ((Utente) session.get(SELECTED_OBJECT)).isDisabilitato();
    }

    public void setViewUtenteDisabilitato(boolean b) {
        Utente u = (Utente) session.get(SELECTED_OBJECT);
        u.setDisabilitato(b);

        UtenteDAO.getInstance().setDisabilitatoUtente(u.getId(), b);

        session.replace(SELECTED_OBJECT, u);
    }

    public boolean isViewUtenteCliente() {
        return ((Utente) session.get(SELECTED_OBJECT)).getRuolo().equals(Utente.RUOLO.CLIENTE);
    }
}
