package it.unisalento.myfood.Business.Command.TipologiaProdotto;


import it.unisalento.myfood.model.Result.OperationTipologiaProdottoResult;

public interface ITipoloagiaProdottoOperation {
        OperationTipologiaProdottoResult execute();

        OperationTipologiaProdottoResult undo();

}
