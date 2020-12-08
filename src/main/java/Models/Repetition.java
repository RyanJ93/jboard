package Models;

import Support.Database;

import java.sql.*;
import java.util.ArrayList;

public class Repetition extends Model {
    public static ArrayList<Repetition> getAll() throws SQLException {
        String query = "SELECT * FROM repetitions LEFT JOIN courses ON repetitions.course_id = courses.id LEFT JOIN teachers ON repetitions.teacher_id = teachers.id;";
        ArrayList<Repetition> elements = new ArrayList<>();
        Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while( resultSet.next() ){
            Repetition repetition = new Repetition();
            repetition.setPropertiesFromResultSet(resultSet);
            elements.add(repetition);
        }
        statement.close();
        return elements;
    }

    public static Repetition find(int id) throws SQLException {
        Repetition repetition = null;
        String query = "SELECT * FROM repetitions LEFT JOIN courses on repetitions.course_id = courses.id LEFT JOIN teachers on repetitions.teacher_id = teachers.id WHERE repetitions.id = ?;";
        Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if ( resultSet != null && resultSet.next() ){
            repetition = new Repetition();
            repetition.setPropertiesFromResultSet(resultSet);
        }
        statement.close();
        return repetition;
    }

    public static ArrayList<Repetition> getAllOrderedByCourse() throws SQLException {
        ArrayList<Repetition> elements = new ArrayList<>();
        String query = "SELECT * FROM repetitions LEFT JOIN courses on repetitions.course_id = courses.id LEFT JOIN teachers on repetitions.teacher_id = teachers.id ORDER BY course_id;";
        Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while( resultSet.next() ){
            Repetition repetition = new Repetition();
            repetition.setPropertiesFromResultSet(resultSet);
            elements.add(repetition);
        }
        statement.close();
        return elements;
    }

    private int id = 0;
    private Teacher teacher = null;
    private Course course = null;

    public int getID(){
        return this.id;
    }

    public Teacher getTeacher(){
        return teacher;
    }

    public Repetition setTeacher(Teacher teacher){
        this.teacher = teacher;
        return this;
    }

    public Course getCourse(){
        return course;
    }

    public Repetition setCourse(Course course){
        this.course = course;
        return this;
    }

    public Repetition setPropertiesFromResultSet(ResultSet resultSet) throws SQLException {
        this.id = Repetition.containsField(resultSet, "repetition_id") ? resultSet.getInt("repetition_id") : resultSet.getInt("id");
        this.teacher = null;
        if ( resultSet.getInt("teacher_id") > 0 ){
            this.teacher = new Teacher().setPropertiesFromResultSet(resultSet);
        }
        this.course = null;
        if ( resultSet.getInt("course_id") > 0 ){
            this.course = new Course().setPropertiesFromResultSet(resultSet);
        }
        return this;
    }

    public Repetition save(){
        int teacherID = this.teacher == null ? 0 : this.teacher.getID();
        int courseID = this.course == null ? 0 : this.course.getID();
        if ( this.id == 0 ){
            try {
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO repetitions (teacher_id, course_id) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, teacherID);
                statement.setInt(2, courseID);
                if ( statement.executeUpdate() == 1 ){
                    ResultSet resultSet = statement.getGeneratedKeys();
                    if ( resultSet.next() ){
                        this.id = resultSet.getInt(1);
                    }
                }
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            //
        }
        return this;
    }

    public Repetition delete(){
        if ( this.id != 0 ){
            try {
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM repetitions WHERE id = ?;");
                statement.setInt(1, this.id);
                statement.executeUpdate();
                this.id = 0;
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return this;
    }
}
