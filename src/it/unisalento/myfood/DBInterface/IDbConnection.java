package it.unisalento.myfood.DBInterface;

import java.sql.ResultSet;

public interface IDbConnection {
    ResultSet executeQuery(String sqlStatement);    // Solo SELECT
    int executeUpdate(String sqlStatement);     // INSERT, UPDATE, DELETE
    void close();   // Chiude e rilascia le risorse
}
