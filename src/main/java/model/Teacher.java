package model;

import support.Database;
import exception.ModelException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Teacher extends Model {
    public static ArrayList<Teacher> getAll(boolean includeDeleted) throws ModelException {
        try{
            ArrayList<Teacher> teachers = new ArrayList<>();
            String query = "SELECT * FROM teachers";
            if ( !includeDeleted ){
                query += " WHERE deleted_at IS NULL";
            }
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while ( resultSet.next() ){
                Teacher teacher = new Teacher();
                teacher.setPropertiesFromResultSet(resultSet);
                teachers.add(teacher);
            }
            statement.close();
            return teachers;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public static Teacher find(int id) throws ModelException {
        try{
            Teacher teacher = null;
            String query = "SELECT * FROM teachers WHERE id = ? AND deleted_at IS NULL;";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet != null && resultSet.next() ){
                teacher = new Teacher();
                teacher.setPropertiesFromResultSet(resultSet);
            }
            statement.close();
            return teacher;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    private int id = 0;
    private String name;
    private String surname;
    private Date deletedAt;

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

    public Date getDeletedAt(){
        return this.deletedAt;
    }

    public Teacher setPropertiesFromResultSet(ResultSet resultSet) throws ModelException {
        try{
            this.id = Teacher.containsField(resultSet, "teacher_id") ? resultSet.getInt("teacher_id") : resultSet.getInt("id");
            this.surname = resultSet.getString("surname");
            this.name = resultSet.getString("name");
            this.deletedAt = resultSet.getTimestamp("deleted_at");
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public Teacher save() throws ModelException {
        try{
            if ( this.id == 0 ){
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
            }else{
                String query = "UPDATE teachers SET name = ?, surname = ? WHERE id = ?;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, this.name);
                statement.setString(2, this.surname);
                statement.setInt(3, this.id);
                statement.executeUpdate();
                statement.close();
            }
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public Teacher delete(boolean softDelete) throws ModelException {
        try{
            if ( this.id != 0 ){
                Connection connection = Database.getConnection();
                if ( softDelete ){
                    String[] queries = new String[]{
                            "UPDATE lessons SET deleted_at = NOW() WHERE teacher_id = ? AND completed = FALSE;",
                            "UPDATE repetitions SET deleted_at = NOW() WHERE teacher_id = ?;",
                            "UPDATE teachers SET deleted_at = NOW() WHERE id = ?;"
                    };
                    for ( String query : queries ){
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, this.id);
                        statement.executeUpdate();
                        statement.close();
                    }
                }else{
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM teachers WHERE id = ?;");
                    statement.setInt(1, this.id);
                    statement.executeUpdate();
                    statement.close();
                }
                this.id = 0;
            }
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }
}
