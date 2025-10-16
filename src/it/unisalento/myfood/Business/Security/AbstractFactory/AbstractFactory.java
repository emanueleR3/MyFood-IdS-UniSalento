package it.unisalento.myfood.Business.Security.AbstractFactory;

import it.unisalento.myfood.Business.Security.Strategy.IPasswordHashingStrategy;

public abstract class AbstractFactory {
    public abstract IPasswordHashingStrategy getStrategy(String strategyType);
}
