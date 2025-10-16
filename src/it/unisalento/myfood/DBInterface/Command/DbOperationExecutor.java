package it.unisalento.myfood.DBInterface.Command;

import java.util.ArrayList;
import java.util.List;

public class DbOperationExecutor {
    private final int MAX_CTRL_Z = 10;
    private List<IDbOperation> dbOperationList = new ArrayList<>();

    public DbOperationResult executeOperation(IDbOperation dbOperation) {
        if(dbOperationList.size() == MAX_CTRL_Z) {
            dbOperationList.remove(0);
        }
        dbOperationList.add(dbOperation);
        return dbOperation.execute();
    }
}
