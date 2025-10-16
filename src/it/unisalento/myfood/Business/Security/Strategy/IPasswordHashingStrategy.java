package it.unisalento.myfood.Business.Security.Strategy;

public interface IPasswordHashingStrategy {
    String encrypt(String password, String salt);
}
