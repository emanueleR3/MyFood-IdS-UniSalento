package it.unisalento.myfood.Business.Decorator.SignUp;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.model.Result.SignUpResult;
import it.unisalento.myfood.model.Utente;

public class SignUpAmministratoreDecorator extends SignUp{
    private SignUp signUp;

    public SignUpAmministratoreDecorator(SignUp signUp){
        this.signUp = signUp;
    }

    @Override
    public SignUpResult esegui() throws InvalidFormatException {
        signUp.utente.setRuolo(Utente.RUOLO.AMMINISTRATORE);
        return signUp.esegui();
    }
}
