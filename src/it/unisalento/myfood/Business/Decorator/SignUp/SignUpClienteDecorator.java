package it.unisalento.myfood.Business.Decorator.SignUp;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.SignUpResult;
import it.unisalento.myfood.model.Utente;

public class SignUpClienteDecorator extends SignUp{
    private SignUp signUp;
    private String residenza;

    public SignUpClienteDecorator(SignUp signUp, String residenza){
        this.signUp = signUp;
        this.residenza = residenza;
    }

    @Override
    public SignUpResult esegui() throws InvalidFormatException {

        signUp.utente.setRuolo(Utente.RUOLO.CLIENTE);
        signUp.utente.setResidenza(residenza);

        return signUp.esegui();
    }
}
