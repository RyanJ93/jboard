package support;

import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static String url;
    private static String username;
    private static String password;
    private static String database;
    private static Connection connection;

    private static void connect() throws SQLException {
        String url = Database.url + "/" + Database.database;
        url += "?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        Database.connection = DriverManager.getConnection(url, Database.username, Database.password);
    }

    private static void setupConnectionProperties() throws RuntimeException {
        JsonObject config = Config.getConfig();
        JsonObject databaseBlock = config.getAsJsonObject("database");
        if ( databaseBlock == null ){
            throw new RuntimeException("No database configuration defined.");
        }
        String host = databaseBlock.get("host").getAsString();
        int port = databaseBlock.get("port").getAsInt();
        String username = databaseBlock.get("username").getAsString();
        String password = databaseBlock.get("password").getAsString();
        String database = databaseBlock.get("database").getAsString();
        if ( host == null || host.isEmpty() ){
            host = "127.0.0.1";
        }
        if ( port <= 0 || port > 65535 ){
            port = 3306;
        }
        if ( database == null || database.isEmpty() ){
            throw new RuntimeException("No database name defined.");
        }
        Database.url = "jdbc:mysql://" + host + ":" + port;
        Database.username = username;
        Database.password = password;
        Database.database = database;
    }

    public static void setup() throws SQLException {
        if ( Database.connection == null ){
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Database.setupConnectionProperties();
            Database.connect();
        }
    }

    public static Connection getConnection(){
        return Database.connection;
    }
}
