package it.unisalento.myfood.Business.Decorator.Login;

import it.unisalento.myfood.model.LoginResult;
import it.unisalento.myfood.model.Utente;

public abstract class Login {
    Utente utente;

    public abstract LoginResult esegui();
}
