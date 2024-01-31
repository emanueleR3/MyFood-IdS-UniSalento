package it.unisalento.myfood.Business.Decorator.SignUp;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.model.SignUpResult;
import it.unisalento.myfood.model.Utente;

public abstract class SignUp {
    Utente utente;

    public abstract SignUpResult esegui() throws InvalidFormatException;
}
