package Models;

import Support.Database;

import java.sql.*;

public class Course extends Model {
    public static Course find(int id){
        Course course = null;
        try{
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
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return course;
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

    public Course setPropertiesFromResultSet(ResultSet resultSet) throws SQLException {
        this.id = Course.containsField(resultSet, "course_id") ? resultSet.getInt("course_id") : resultSet.getInt("id");
        this.title = resultSet.getString("title");
        return this;
    }

    public Course save(){
        if ( this.id == 0 ){
            try{
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
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            try{
                String query = "UPDATE courses SET title = ? WHERE id = ?;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, this.title);
                statement.setInt(2, this.id);
                statement.executeUpdate();
                statement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this;
    }

    public Course delete(){
        if ( this.id != 0 ){
            try{
                String query = "DELETE FROM courses WHERE id = ?;";
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
