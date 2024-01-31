package it.unisalento.myfood.Business.Decorator.Login;

import it.unisalento.myfood.Business.CarrelloBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.LoginResult;
import it.unisalento.myfood.model.Utente;

public class LoginAmministratoreDecorator extends Login{
    private Login login;
    private String email;
    private String password;

    public LoginAmministratoreDecorator(Login login, String email, String password){
        this.login = login;
        this.email = email;
        this.password = password;
    }

    @Override
    public LoginResult esegui() {
        LoginResult loginResult = new LoginResult();
        UtenteDAO utenteDAO = UtenteDAO.getInstance();

        // Lo fa prima
        /*1. Controllare se esiste un utente con questa email
        boolean userExists = utenteDAO.userExists(email);
        if (!userExists) {
            loginResult.setLoginResult(LoginResult.LOGIN_RESULT.USER_DOESNT_EXIT);
            loginResult.setMessage("L'utente non esiste");
            return loginResult;
        }
         */

        //2. se l'utente esiste, controlla se la password fornita coincide
        // TODO: implementare disabilitazione temporanea e poi definitiva
        boolean passwordOk;
        // int remainingAttempts = 5;
        // while (remainingAttempts > 0) { //TODO: così appena sbaglia la password blocca, bisogna memorizzare remainignAttemps fuori da questo metodo, nel db per ogni utente
        passwordOk = utenteDAO.passwordOk(email, password);
        if (!passwordOk) {

            //   remainingAttempts--;
            loginResult.setLoginResult(LoginResult.LOGIN_RESULT.WRONG_PASSWORD);
              /*if (remainingAttempts != 0) {
                     //si hanno ancora dei tentativi
                   loginResult.setMessage("Hai sbagliato la password. Tentativi rimanenti: " + remainingAttempts);
                } else {
                    // se non si hanno più tentativi viene bloccato l'utente
                    utenteDAO.disableUser(email);
                    loginResult.setMessage("Hai sbagliato la password. L'utente è stato bloccato");
                    loginResult.setLoginResult(LoginResult.LOGIN_RESULT.USER_BLOCKED);
                    return loginResult;
                }*/
            loginResult.setMessage("Hai sbagliato la password");
            return loginResult;
        } /*else {
                // la password è giusta
                break;
            }*/

        //3. carica la tipologia di utente (cliente, ecc)
            login.utente = utenteDAO.caricaAmministratore(email);
            //loginResult.setMessage("Benvenuto " + u.getNome() + "!");

            //5. salva l'utente in sessione
            login.utente.setRuolo(Utente.RUOLO.AMMINISTRATORE);

            UtenteBusiness.getSession().put(UtenteBusiness.LOGGED_IN_USER, login.utente);

            CarrelloBusiness.getInstance().loadCarrelloToSession();

            return login.esegui();

    }
}