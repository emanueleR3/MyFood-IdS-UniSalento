package it.unisalento.myfood.Business.Decorator.Login;

import it.unisalento.myfood.Business.CarrelloBusiness;
import it.unisalento.myfood.Business.Security.AbstractFactory.StrategyFactory;
import it.unisalento.myfood.Business.Security.Strategy.PasswordHashingContext;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.Result.LoginResult;
import it.unisalento.myfood.model.Utente;

public class LoginCucinaDecorator extends Login{
    private Login login;
    private String email;
    private String password;

    public LoginCucinaDecorator(Login login, String email, String password){
        this.login = login;
        this.email = email;
        this.password = password;
    }

    @Override
    public LoginResult esegui() {
        LoginResult loginResult = new LoginResult();
        UtenteDAO utenteDAO = UtenteDAO.getInstance();

        //1. controlla se la password fornita coincide
        boolean passwordOk;

        String salt = utenteDAO.findByEmail(email).getSaltHex();

        StrategyFactory strategyFactory = new StrategyFactory();

        PasswordHashingContext passwordHashingContext = new PasswordHashingContext(strategyFactory.getStrategy("SHA512"));
        String hashedPassword = passwordHashingContext.executeStrategy(password, salt);

        passwordOk = utenteDAO.passwordOk(email, hashedPassword);
        if (!passwordOk) {
            loginResult.setLoginResult(LoginResult.LOGIN_RESULT.WRONG_PASSWORD);
            loginResult.setMessage("Hai sbagliato la password");
            return loginResult;
        }

        //2. carica la tipologia di utente (cliente, ecc)
        login.utente = utenteDAO.caricaCucina(email);

        //3. salva l'utente in sessione
        login.utente.setRuolo(Utente.RUOLO.CUCINA);

        UtenteBusiness.getSession().put(UtenteBusiness.LOGGED_IN_USER_ID, login.utente);

        CarrelloBusiness.getInstance().putCarrelloToSession();

        return login.esegui();
    }
}