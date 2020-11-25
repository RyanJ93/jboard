package Models;

import Support.Database;

import java.sql.*;

public class Teacher extends Model {
    public static Teacher find(int id){
        Teacher teacher = null;
        try{
            String query = "SELECT * FROM teachers WHERE id = ?;";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet != null && resultSet.next() ){
                teacher = new Teacher();
                teacher.setPropertiesFromResultSet(resultSet);
            }
            statement.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return teacher;
    }

    private int id = 0;
    private String name = null;
    private String surname = null;

    public int getID(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Teacher setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return this.surname;
    }

    public Teacher setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getFullName(){
        String fullName = null;
        if ( this.name != null || this.surname != null ){
            fullName = this.name == null ? this.surname : ( this.surname == null ? this.name : ( this.name + " " + this.surname ) );
        }
        return fullName;
    }

    public Teacher setPropertiesFromResultSet(ResultSet resultSet) throws SQLException {
        this.id = Teacher.containsField(resultSet, "teacher_id") ? resultSet.getInt("teacher_id") : resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.surname = resultSet.getString("surname");
        return this;
    }

    public Teacher save(){
        if ( this.id == 0 ){
            try{
                String query = "INSERT INTO teachers (name, surname) VALUES (?, ?);";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, this.name);
                statement.setString(2, this.surname);
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
                String query = "UPDATE teachers SET name = ?, surname = ? WHERE id = ?;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, this.name);
                statement.setString(2, this.surname);
                statement.setInt(3, this.id);
                statement.executeUpdate();
                statement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this;
    }

    public Teacher delete(){
        if ( this.id != 0 ){
            try{
                String query = "DELETE FROM teachers WHERE id = ?;";
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
