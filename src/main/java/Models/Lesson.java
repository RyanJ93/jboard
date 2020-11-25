package Models;

import Support.Database;

import java.sql.*;
import java.util.ArrayList;

public class Lesson extends Model {
    public static Lesson find(int id){
        Lesson lesson = null;
        try {
            String query = "SELECT * FROM lessons LEFT JOIN courses ON lessons.course_id = courses.id LEFT JOIN users ON lessons.user_id = users.id LEFT JOIN teachers ON lessons.teacher_id = teachers.id WHERE lessons.id = ?;";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet != null && resultSet.next() ){
                lesson = new Lesson();
                lesson.setPropertiesFromResultSet(resultSet);
            }
            statement.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return lesson;
    }

    public static ArrayList<Lesson> getAll(){
        ArrayList<Lesson> elements = new ArrayList<>();
        try{
            String query = "SELECT * FROM lessons LEFT JOIN courses ON lessons.course_id = courses.id LEFT JOIN users ON lessons.user_id = users.id LEFT JOIN teachers ON lessons.teacher_id = teachers.id;";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ){
                Lesson lesson = new Lesson();
                lesson.setPropertiesFromResultSet(resultSet);
                elements.add(lesson);
            }
            statement.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return elements;
    }

    private int id = 0;
    private Course course;
    private User user;
    private Teacher teacher;
    private int day = 0;
    private int hour = 0;

    public int getID(){
        return this.id;
    }

    public Course getCourse(){
        return this.course;
    }

    public Lesson setCourse(Course course){
        this.course = course;
        return this;
    }

    public User getUser(){
        return this.user;
    }

    public Lesson setUser(User user){
        this.user = user;
        return this;
    }

    public Teacher getTeacher(){
        return this.teacher;
    }

    public Lesson setTeacher(Teacher teacher){
        this.teacher = teacher;
        return this;
    }

    public int getDay(){
        return this.day;
    }

    public String getDayName(){
        String dayName = null;
        switch ( this.day ){
            case 1: {
                dayName = "Monday";
            }break;
            case 2: {
                dayName = "Tuesday";
            }break;
            case 3: {
                dayName = "Wednesday";
            }break;
            case 4: {
                dayName = "Thursday";
            }break;
            case 5: {
                dayName = "Friday";
            }break;
        }
        return dayName;
    }

    public Lesson setDay(int day){
        if ( day <= 0 || day > 5 ){
            throw new IllegalArgumentException("Invalid day.");
        }
        this.day = day;
        return this;
    }

    public int getHour(){
        return this.hour;
    }

    public String getTimeSlot(){
        String timeSlot = null;
        if ( this.hour > 0 ){
            timeSlot = this.hour + ":00 - " + ( this.hour + 1 ) + ":00";
        }
        return timeSlot;
    }

    public Lesson setHour(int hour){
        if ( hour < 15 || hour > 18 ){
            throw new IllegalArgumentException("Invalid hour.");
        }
        this.hour = hour;
        return this;
    }

    public Lesson setPropertiesFromResultSet(ResultSet resultSet) throws SQLException {
        this.id = Lesson.containsField(resultSet, "lesson_id") ? resultSet.getInt("lesson_id") : resultSet.getInt("id");
        this.course = null;
        if ( resultSet.getInt("course_id") > 0 ){
            this.course = new Course().setPropertiesFromResultSet(resultSet);
        }
        this.user = null;
        if ( resultSet.getInt("user_id") > 0 ){
            this.user = new User().setPropertiesFromResultSet(resultSet);
        }
        this.teacher = null;
        if ( resultSet.getInt("teacher_id") > 0 ){
            this.teacher = new Teacher().setPropertiesFromResultSet(resultSet);
        }
        this.day = resultSet.getInt("day");
        this.hour = resultSet.getInt("hour");
        return this;
    }

    public Lesson save(){
        if ( this.day <= 0 || this.day > 5 ){
            throw new RuntimeException("Invalid day defined.");
        }
        if ( this.hour < 15 || this.hour > 18 ){
            throw new RuntimeException("Invalid hour defined.");
        }
        int courseID = this.course == null ? 0 : this.course.getID();
        int userID = this.user == null ? 0 : this.user.getID();
        int teacherID = this.teacher == null ? 0 : this.teacher.getID();
        if ( this.id == 0 ){
            try{
                String query = "INSERT INTO lessons (course_id, user_id, teacher_id, day, hour) VALUES (?, ?, ?, ?, ?);";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, courseID);
                statement.setInt(2, userID);
                statement.setInt(3, teacherID);
                statement.setInt(4, this.day);
                statement.setInt(5, this.hour);
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
                String query = "UPDATE lessons SET course_id = ?, user_id = ?, teacher_id = ?, day = ?, hour = ? WHERE id = ?;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, courseID);
                statement.setInt(2, userID);
                statement.setInt(3, teacherID);
                statement.setInt(4, this.day);
                statement.setInt(5, this.hour);
                statement.setInt(6, this.id);
                statement.executeUpdate();
                statement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this;
    }

    public Lesson delete(){
        if ( this.id != 0 ){
            try{
                String query = "DELETE FROM lessons WHERE id = ?;";
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
