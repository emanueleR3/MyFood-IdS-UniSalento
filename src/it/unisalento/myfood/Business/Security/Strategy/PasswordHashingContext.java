package it.unisalento.myfood.Business.Security.Strategy;

public class PasswordHashingContext {
    protected IPasswordHashingStrategy strategy;

    public PasswordHashingContext(IPasswordHashingStrategy strategy){
        this.strategy = strategy;
    }

    public String executeStrategy(String password, String salt){
        return strategy.encrypt(password, salt);
    }
}
