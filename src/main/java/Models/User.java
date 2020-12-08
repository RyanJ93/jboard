package Models;

import Support.Database;
import Utils.PasswordCocktail;

import java.sql.*;

public class User extends Model {
    public static User find(int id) throws SQLException {
        User user = null;
        String query = "SELECT * FROM users WHERE id = ?;";
        Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if ( resultSet != null && resultSet.next() ){
            user = new User();
            user.setPropertiesFromResultSet(resultSet);
        }
        statement.close();
        return user;
    }

    public static User findByAccount(String account) throws SQLException {
        User user = null;
        String query = "SELECT * FROM users WHERE account = ?;";
        Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, account);
        ResultSet resultSet = statement.executeQuery();
        if ( resultSet != null && resultSet.next() ){
            user = new User();
            user.setPropertiesFromResultSet(resultSet);
        }
        statement.close();
        return user;
    }

    private int id = 0;
    private String account = null;
    private PasswordCocktail password = null;
    private String role = null;

    public int getID(){
        return this.id;
    }

    public String getAccount(){
        return this.account;
    }

    public User setAccount(String account){
        this.account = account;
        return this;
    }

    public PasswordCocktail getPasswordCocktail(){
        return this.password;
    }

    public User setPassword(String password){
        this.password = new PasswordCocktail(password);
        return this;
    }

    public User setPasswordCocktail(PasswordCocktail password){
        this.password = password;
        return this;
    }

    public String getRole(){
        return this.role;
    }

    public User setRole(String role){
        this.role = role;
        return this;
    }

    public User setPropertiesFromResultSet(ResultSet resultSet) throws SQLException {
        this.id = User.containsField(resultSet, "user_id") ? resultSet.getInt("user_id") : resultSet.getInt("id");
        this.account = resultSet.getString("account");
        this.role = resultSet.getString("role");
        String passwordHash = resultSet.getString("password_hash");
        String passwordSalt = resultSet.getString("password_salt");
        String passwordPepper = resultSet.getString("password_pepper");
        int passwordLoop = resultSet.getInt("password_loop");
        this.password = new PasswordCocktail(passwordHash, passwordSalt, passwordPepper, passwordLoop);
        return this;
    }

    public User save(){
        if ( this.id == 0 ){
            try{
                String query = "INSERT INTO users (account, password_salt, password_pepper, password_hash, password_loop, role) VALUES (?, ?, ?, ?, ?, ?);";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, this.account);
                statement.setString(2, this.password.getSalt());
                statement.setString(3, this.password.getPepper());
                statement.setString(4, this.password.getHash());
                statement.setInt(5, this.password.getLoop());
                statement.setString(6, this.role);
                if ( statement.executeUpdate() == 1 ){
                    ResultSet resultSet = statement.getGeneratedKeys();
                    if ( resultSet.next() ){
                        this.id = resultSet.getInt(1);
                    }
                }
                statement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            try{
                String query = "UPDATE users SET account = ?, role = ?";
                if ( this.password != null ){
                    query += ", password_salt = ?, password_pepper = ?, password_hash = ?, password_loop = ?";
                }
                query += " WHERE id = ?;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, this.account);
                statement.setString(2, this.role);
                if ( this.password == null ){
                    statement.setInt(3, this.id);
                }else{
                    statement.setString(3, this.password.getSalt());
                    statement.setString(4, this.password.getPepper());
                    statement.setString(5, this.password.getHash());
                    statement.setInt(6, this.password.getLoop());
                    statement.setInt(7, this.id);
                }
                statement.executeUpdate();
                statement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this;
    }

    public User delete(){
        if ( this.id != 0 ){
            try{
                String query = "DELETE FROM users WHERE id = ?;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, this.id);
                statement.executeUpdate();
                this.id = 0;
                statement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this;
    }
}
