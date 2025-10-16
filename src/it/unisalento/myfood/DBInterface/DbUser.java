package it.unisalento.myfood.DBInterface;

public class DbUser {
    public static DbUser instance = new DbUser();
    private String schemaName;
    private String userName;
    private String pwd;

    private DbUser() {
        this.schemaName = "myfood";
        this.userName = "root";
        this.pwd = "123456789";
    }

    public static DbUser getInstance() {
        return instance;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPwd() {
        return pwd;
    }
}
