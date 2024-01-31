package it.unisalento.myfood.model;

public class SignUpResult implements IResult {

    public enum SIGN_UP_RESULT {
        USER_ALREADY_EXISTS,
        SIGNED_UP_SUCCESFULLY
    }

    private SIGN_UP_RESULT signUpResult;
    private String message;

    public SIGN_UP_RESULT getSignUpResult() {
        return signUpResult;
    }

    public void setSignUpResult(SIGN_UP_RESULT signUpResult) {
        this.signUpResult = signUpResult;
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
