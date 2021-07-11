package model;

import support.Database;
import exception.ModelException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Course extends Model {
    public static ArrayList<Course> getAll(boolean includeDeleted) throws ModelException {
        try{
            ArrayList<Course> courses = new ArrayList<>();
            String query = "SELECT * FROM courses";
            if ( !includeDeleted ){
                query += " WHERE deleted_at IS NULL";
            }
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while ( resultSet.next() ){
                Course course = new Course();
                course.setPropertiesFromResultSet(resultSet);
                courses.add(course);
            }
            statement.close();
            return courses;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public static Course find(int id) throws ModelException {
        try{
            Course course = null;
            String query = "SELECT * FROM courses WHERE id = ? AND deleted_at IS NULL;";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet != null && resultSet.next() ){
                course = new Course();
                course.setPropertiesFromResultSet(resultSet);
            }
            statement.close();
            return course;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    private int id;
    private String title;
    private Date deletedAt;

    public int getID(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public Course setTitle(String title){
        this.title = title;
        return this;
    }

    public Date getDeletedAt(){
        return this.deletedAt;
    }

    public Course setPropertiesFromResultSet(ResultSet resultSet) throws ModelException {
        try{
            this.id = Course.containsField(resultSet, "course_id") ? resultSet.getInt("course_id") : resultSet.getInt("id");
            this.title = resultSet.getString("title");
            this.deletedAt = resultSet.getTimestamp("deleted_at");
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public Course save() throws ModelException {
        try{
            if ( this.id == 0 ){
                String query = "INSERT INTO courses (title) VALUES (?);";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, this.title);
                if ( statement.executeUpdate() == 1 ){
                    ResultSet resultSet = statement.getGeneratedKeys();
                    if ( resultSet.next() ){
                        this.id = resultSet.getInt(1);
                    }
                }
                statement.close();
            }else{
                String query = "UPDATE courses SET title = ? WHERE id = ?;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, this.title);
                statement.setInt(2, this.id);
                statement.executeUpdate();
                statement.close();
            }
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public Course delete(boolean softDelete) throws ModelException {
        try{
            if ( this.id != 0 ){
                Connection connection = Database.getConnection();
                if ( softDelete ){
                    String[] queries = new String[]{
                        "UPDATE lessons SET deleted_at = NOW() WHERE course_id = ? AND completed = FALSE;",
                        "UPDATE repetitions SET deleted_at = NOW() WHERE course_id = ?;",
                        "UPDATE courses SET deleted_at = NOW() WHERE id = ?;"
                    };
                    for ( String query : queries ){
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, this.id);
                        statement.executeUpdate();
                        statement.close();
                    }
                }else{
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM courses WHERE id = ?;");
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
