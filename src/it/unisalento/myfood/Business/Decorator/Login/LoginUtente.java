package it.unisalento.myfood.Business.Decorator.Login;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.model.LoginResult;
import it.unisalento.myfood.model.Utente;

import java.util.HashMap;

import static it.unisalento.myfood.Business.UtenteBusiness.LOGGED_IN_USER;

public class LoginUtente extends Login {
    private static HashMap<String, Object> session = new HashMap<>();
    private IResultFactory resultFactory;

    public LoginUtente(){
        session = UtenteBusiness.getSession();  //TODO : testare
        utente = new Utente();
        resultFactory = new ResultFactory("Login");
    }

    @Override
    public LoginResult esegui(){

        session.put(LOGGED_IN_USER, utente);
        LoginResult loginResult = (LoginResult) resultFactory.crea();
        loginResult.setLoginResult(LoginResult.LOGIN_RESULT.LOGIN_OK);
        return loginResult;
    }
}
