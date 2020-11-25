package Support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String user = "root";
    private static final String password = "mysql_password";
    private static final String database = "jboard";
    private static Connection connection = null;

    protected static void registerDriver() {
        try{
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    protected static void connect(){
        String url = Database.url + Database.database;
        try{
            Database.connection = DriverManager.getConnection(url, Database.user, Database.password);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public static void setup(){
        if ( Database.connection == null ){
            Database.registerDriver();
            Database.connect();
        }
    }

    public static Connection getConnection(){
        return Database.connection;
    }
}
