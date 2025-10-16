package it.unisalento.myfood.Business.Decorator.Login;

import it.unisalento.myfood.model.Result.LoginResult;
import it.unisalento.myfood.model.Utente;

public class LoginGuestDecorator extends Login{
    Login login;
    public LoginGuestDecorator(Login login){
        this.login = login;
    }

    @Override
    public LoginResult esegui() {

        login.utente.setRuolo(Utente.RUOLO.GUEST);
        return login.esegui();
    }
}
