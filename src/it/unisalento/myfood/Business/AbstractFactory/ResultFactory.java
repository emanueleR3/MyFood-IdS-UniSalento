package it.unisalento.myfood.Business.AbstractFactory;

import it.unisalento.myfood.model.IResult;

public class ResultFactory implements IResultFactory {

    private String resultType;

    public ResultFactory(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public IResult crea() {
        try{
            Class cls = Class.forName("it.unisalento.myfood.model." + resultType + "Result");
            Object obj = cls.getConstructor().newInstance();
            return (IResult) obj;
        } catch (Exception e){
            System.out.println("implementazione del result non trovata");
        }
        return null;
    }
}
