package it.unisalento.myfood.model;

public class LoginResult implements IResult {

    public enum LOGIN_RESULT {
        USER_DOESNT_EXIT,
        WRONG_PASSWORD,
        LOGIN_OK,
        USER_BLOCKED
    }

    private LOGIN_RESULT loginResult;
    private String message;     // o enum di messaggi

    public LOGIN_RESULT getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LOGIN_RESULT loginResult) {
        this.loginResult = loginResult;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
