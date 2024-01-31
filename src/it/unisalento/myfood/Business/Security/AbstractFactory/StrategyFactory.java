package it.unisalento.myfood.Business.Security.AbstractFactory;

import it.unisalento.myfood.Business.Security.Strategy.IPasswordHashingStrategy;

public class StrategyFactory extends AbstractFactory{

    @Override
    public IPasswordHashingStrategy getStrategy(String strategyType) {
        try{
            Class cls = Class.forName("it.unisalento.myfood.Business.Security.Strategy." + strategyType + "Hashing");
            Object obj = cls.getConstructor().newInstance();
            return (IPasswordHashingStrategy) obj;
        } catch (Exception e){
            System.out.println("implementazione della strategy non trovata");
        }
        return null;
    }
}
