package it.unisalento.myfood.DBInterface.Command;

import it.unisalento.myfood.DBInterface.DbConnection;

public class ReadOperation implements IDbOperation {

    private DbConnection conn = DbConnection.getInstance();
    private String sql;

    public ReadOperation(String sql) {
        this.sql = sql;
    }

    @Override
    public DbOperationResult execute() {
        DbOperationResult result = new DbOperationResult();
        result.setResultSet(conn.executeQuery(this.sql));
        return result;
    }
}
