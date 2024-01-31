package it.unisalento.myfood.Business.Decorator.SignUp;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.model.SignUpResult;
import it.unisalento.myfood.model.Utente;

public class SignUpCucinaDecorator extends SignUp{
    private SignUp signUp;

    public SignUpCucinaDecorator(SignUp signUp){
        this.signUp = signUp;
    }

    @Override
    public SignUpResult esegui() throws InvalidFormatException {
        signUp.utente.setRuolo(Utente.RUOLO.CUCINA);
        return signUp.esegui();
    }
}
