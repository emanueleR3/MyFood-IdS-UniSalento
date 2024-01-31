package it.unisalento.myfood.DBInterface.Command;

import java.sql.ResultSet;

public class DbOperationResult {

    private int rowsAffected;
    private ResultSet resultSet;

    public int getRowsAffected() {
        return rowsAffected;
    }

    public void setRowsAffected(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
