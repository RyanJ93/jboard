package model;

import support.Database;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.UUID;
import exception.ModelException;

public class UserToken extends Model {
    public static UserToken find(String token) throws ModelException  {
        try{
            UserToken userToken = null;
            String query = "SELECT * FROM user_tokens WHERE token = ?;";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet != null && resultSet.next() ){
                userToken = new UserToken();
                userToken.setPropertiesFromResultSet(resultSet);
            }
            statement.close();
            return userToken;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    private User user;
    private String token;

    public UserToken setUser(User user){
        this.user = user;
        return this;
    }

    public User getUser(){
        return this.user;
    }

    public UserToken generateToken() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(StandardCharsets.UTF_8.encode(UUID.randomUUID().toString()));
        this.token = String.format("%032x", new BigInteger(1, messageDigest.digest()));
        return this;
    }

    public String getToken(){
        return this.token;
    }

    public boolean compareToken(String token){
        return this.token != null && MessageDigest.isEqual(this.token.getBytes(), token.getBytes());
    }

    public UserToken setPropertiesFromResultSet(ResultSet resultSet) throws ModelException {
        try{
            this.user = User.find(resultSet.getInt("user_id"));
            this.token = resultSet.getString("token");
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public UserToken save() throws ModelException {
        try{
            String query = "INSERT INTO user_tokens (token, user_id) VALUES (?, ?);";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.token);
            statement.setInt(2, this.user.getID());
            statement.executeUpdate();
            statement.close();
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public UserToken delete() throws ModelException {
        try{
            if ( this.token != null ){
                String query = "DELETE FROM user_tokens WHERE token = ?;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, this.token);
                statement.executeUpdate();
                this.token = null;
                statement.close();
            }
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }
}
