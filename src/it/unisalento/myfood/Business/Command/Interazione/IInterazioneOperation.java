package it.unisalento.myfood.Business.Command.Interazione;

import it.unisalento.myfood.model.Result.OperationInterazioneResult;

public interface IInterazioneOperation {
    OperationInterazioneResult execute();

    OperationInterazioneResult undo();


}
