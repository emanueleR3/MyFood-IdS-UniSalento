package it.unisalento.myfood.Business.Decorator.Login;

import it.unisalento.myfood.Business.CarrelloBusiness;
import it.unisalento.myfood.Business.Security.AbstractFactory.StrategyFactory;
import it.unisalento.myfood.Business.Security.Strategy.PasswordHashingContext;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.Result.LoginResult;
import it.unisalento.myfood.model.Utente;

public class LoginClienteDecorator extends Login{
    private Login login;
    private String email;
    private String password;

    public LoginClienteDecorator(Login login, String email, String password){
        this.login = login;
        this.email = email;
        this.password = password;
    }

    @Override
    public LoginResult esegui() {
        LoginResult loginResult = new LoginResult();
        UtenteDAO utenteDAO = UtenteDAO.getInstance();

        //1. se l'utente esiste, controlla se la password fornita coincide
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

        //2. se le credenziali coincidono non sia stato bloccato dall'amministratore
        boolean isEnable = utenteDAO.isEnable(email);
        if (!isEnable) {
            loginResult.setLoginResult(LoginResult.LOGIN_RESULT.USER_BLOCKED);
            loginResult.setMessage("Questo utente è stato bloccato dall'amministratore");
            return loginResult;
        }

        //3. controlla e carica la tipologia di utente (cliente, ecc)
        if (utenteDAO.isCliente(UtenteDAO.getInstance().findByEmail(email).getId())) {
            login.utente = utenteDAO.caricaCliente(email);


            //4. salva l'utente in sessione
            login.utente.setRuolo(Utente.RUOLO.CLIENTE);


            UtenteBusiness.getSession().put(UtenteBusiness.LOGGED_IN_USER_ID, login.utente);

            CarrelloBusiness.getInstance().putCarrelloToSession();

            return login.esegui();
        }
        return loginResult;

    }
}