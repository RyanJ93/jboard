package model;

import support.Database;
import exception.ModelException;
import java.sql.*;
import java.util.ArrayList;

public class Course extends Model {
    public static ArrayList<Course> getAll() throws ModelException {
        try{
            ArrayList<Course> courses = new ArrayList<>();
            String query = "SELECT * FROM courses;";
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
            String query = "SELECT * FROM courses WHERE id = ?;";
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

    private int id = 0;
    private String title = null;

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

    public Course setPropertiesFromResultSet(ResultSet resultSet) throws ModelException {
        try{
            this.id = Course.containsField(resultSet, "course_id") ? resultSet.getInt("course_id") : resultSet.getInt("id");
            this.title = resultSet.getString("title");
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

    public Course delete() throws ModelException {
        try{
            if ( this.id != 0 ){
                String query = "DELETE FROM courses WHERE id = ?;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, this.id);
                statement.executeUpdate();
                this.id = 0;
                statement.close();
            }
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }
}
