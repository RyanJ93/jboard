package Models;

import Support.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Lesson extends Model {
    protected static String getLessonStringIdentifier(int courseID, int teacherID, int day, int hour){
        String lessonStringIdentifier = Integer.toString(courseID);
        lessonStringIdentifier += Integer.toString(teacherID);
        lessonStringIdentifier += Integer.toString(day);
        lessonStringIdentifier += Integer.toString(hour);
        return lessonStringIdentifier;
    }

    public static Lesson find(int id) throws SQLException {
        Lesson lesson = null;
        String query = "SELECT * FROM lessons LEFT JOIN courses ON lessons.course_id = courses.id LEFT JOIN users ON lessons.user_id = users.id LEFT JOIN teachers ON lessons.teacher_id = teachers.id WHERE lessons.id = ? AND lessons.deleted_at IS NULL;";
        Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if ( resultSet != null && resultSet.next() ){
            lesson = new Lesson();
            lesson.setPropertiesFromResultSet(resultSet);
        }
        statement.close();
        return lesson;
    }

    public static ArrayList<Lesson> getAll(boolean includeDeleted) throws SQLException {
        String query = "SELECT * FROM lessons LEFT JOIN courses ON lessons.course_id = courses.id LEFT JOIN users ON lessons.user_id = users.id LEFT JOIN teachers ON lessons.teacher_id = teachers.id";
        query += includeDeleted ? ";" : " WHERE lessons.deleted_at IS NULL;";
        ArrayList<Lesson> elements = new ArrayList<>();
        Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while( resultSet.next() ){
            Lesson lesson = new Lesson();
            lesson.setPropertiesFromResultSet(resultSet);
            elements.add(lesson);
        }
        statement.close();
        return elements;
    }

    public static ArrayList<Lesson> getAllByUser(User user, boolean includeDeleted) throws SQLException {
        String query = "SELECT * FROM lessons LEFT JOIN courses ON lessons.course_id = courses.id LEFT JOIN users ON lessons.user_id = users.id LEFT JOIN teachers ON lessons.teacher_id = teachers.id";
        if ( user == null ){
            query += includeDeleted ? ";" : " WHERE lessons.deleted_at IS NULL;";
        }else{
            query += " WHERE user_id = ?" + ( includeDeleted ? ";" : " AND lessons.deleted_at IS NULL;" );
        }
        ArrayList<Lesson> elements = new ArrayList<>();
        Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        if ( user != null ){
            statement.setInt(1, user.getID());
        }
        ResultSet resultSet = statement.executeQuery();
        while( resultSet.next() ){
            Lesson lesson = new Lesson();
            lesson.setPropertiesFromResultSet(resultSet);
            if ( user != null ){
                lesson.setUser(null);
            }
            elements.add(lesson);
        }
        statement.close();
        return elements;
    }

    public static ArrayList<HashMap<String, Object>> getAvailable(int userID) throws SQLException {
        ArrayList<HashMap<String, Object>> availableLessons = new ArrayList<>();
        Connection connection = Database.getConnection();
        String bookedLessonsQuery = "SELECT * FROM lessons WHERE completed = FALSE AND deleted_at IS NULL;";
        PreparedStatement statement = connection.prepareStatement(bookedLessonsQuery);
        ResultSet resultSet = statement.executeQuery();
        HashMap<String, Integer> bookedLessons = new HashMap<>();
        while( resultSet.next() ){
            String id = Lesson.getLessonStringIdentifier(resultSet.getInt("course_id"), resultSet.getInt("teacher_id"), resultSet.getInt("day"), resultSet.getInt("hour"));
            bookedLessons.put(id, resultSet.getInt("user_id"));
        }
        statement.close();
        String allCoursesQuery = "SELECT * FROM repetitions LEFT JOIN courses ON repetitions.course_id = courses.id LEFT JOIN teachers ON repetitions.teacher_id = teachers.id ORDER BY course_id;";
        statement = connection.prepareStatement(allCoursesQuery);
        resultSet = statement.executeQuery();
        ArrayList<HashMap<String, Object>> lessons = new ArrayList<>();
        Course course = null;
        int lastCourseID = 0;
        HashMap<String, Object> block = new HashMap<>();
        while( resultSet.next() ){
            int courseID = resultSet.getInt("course_id");
            if ( courseID != lastCourseID ){
                if ( course != null ){
                    availableLessons.add(block);
                    block = new HashMap<>();
                }
                course = new Course();
                lessons = new ArrayList<>();
                course.setPropertiesFromResultSet(resultSet);
                block.put("course", course);
                block.put("lessons", lessons);
                lastCourseID = courseID;
            }
            HashMap<String, Object> element = new HashMap<>();
            ArrayList<HashMap<String, Integer>> slots = new ArrayList<>();
            Teacher teacher = new Teacher();
            teacher.setPropertiesFromResultSet(resultSet);
            element.put("teacher", teacher);
            element.put("slots", slots);
            for ( int day = 1 ; day < 6 ; day ++ ) {
                for ( int hour = 16 ; hour < 19 ; hour++ ) {
                    String id = Lesson.getLessonStringIdentifier(resultSet.getInt("course_id"), resultSet.getInt("teacher_id"), day, hour);
                    Integer ownerUserID = bookedLessons.get(id);
                    HashMap<String, Integer> slot = new HashMap<>();
                    slot.put("day", day);
                    slot.put("hour", hour);
                    slot.put("available", bookedLessons.containsKey(id) ? 0 : 1);
                    slot.put("mine", ownerUserID != null && userID == ownerUserID ? 1 : 0);
                    slots.add(slot);
                }
            }
            lessons.add(element);
        }
        if ( course != null ){
            availableLessons.add(block);
        }
        statement.close();
        return availableLessons;
    }

    private int id = 0;
    private Course course;
    private User user;
    private Teacher teacher;
    private int day = 0;
    private int hour = 0;
    private boolean completed = false;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

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

    public boolean getCompleted(){
        return this.completed;
    }

    public Lesson setCompleted(boolean completed){
        this.completed = completed;
        return this;
    }

    public Date getCreatedAt(){
        return this.createdAt;
    }

    public Date getUpdatedAt(){
        return this.updatedAt;
    }

    public Date getDeletedAt(){
        return this.deletedAt;
    }

    public Lesson setPropertiesFromResultSet(ResultSet resultSet) throws SQLException {
        this.id = Lesson.containsField(resultSet, "lesson_id") ? resultSet.getInt("lesson_id") : resultSet.getInt("id");
        this.course = null;
        if ( resultSet.getInt("course_id") > 0 ){
            this.course = new Course().setPropertiesFromResultSet(resultSet);
        }
        int userID = 0;
        this.day = this.hour = 0;
        this.user = null;
        try{
            userID = resultSet.getInt("user_id");
        }catch(Exception ex){}
        if ( userID > 0 ){
            this.user = new User().setPropertiesFromResultSet(resultSet);
        }
        this.teacher = null;
        if ( resultSet.getInt("teacher_id") > 0 ){
            this.teacher = new Teacher().setPropertiesFromResultSet(resultSet);
        }
        try{
            this.day = resultSet.getInt("day");
        }catch(Exception ex){}
        try{
            this.hour = resultSet.getInt("hour");
        }catch(Exception ex){}
        this.createdAt = resultSet.getDate("created_at");
        this.updatedAt = resultSet.getDate("updated_at");
        this.deletedAt = resultSet.getDate("deleted_at");
        this.completed = resultSet.getBoolean("completed");
        return this;
    }

    public Lesson save() throws SQLException {
        if ( this.day <= 0 || this.day > 5 ){
            throw new RuntimeException("Invalid day defined.");
        }
        if ( this.hour < 15 || this.hour > 18 ){
            throw new RuntimeException("Invalid hour defined.");
        }
        int courseID = this.course == null ? 0 : this.course.getID();
        int userID = this.user == null ? 0 : this.user.getID();
        int teacherID = this.teacher == null ? 0 : this.teacher.getID();
        Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
        if ( this.id == 0 ){
            String query = "INSERT INTO lessons (course_id, user_id, teacher_id, day, hour, completed, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, courseID);
            statement.setInt(2, userID);
            statement.setInt(3, teacherID);
            statement.setInt(4, this.day);
            statement.setInt(5, this.hour);
            statement.setBoolean(6, this.completed);
            statement.setDate(7, currentDate);
            statement.setDate(8, currentDate);
            if ( statement.executeUpdate() == 1 ){
                ResultSet resultSet = statement.getGeneratedKeys();
                if ( resultSet.next() ){
                    this.id = resultSet.getInt(1);
                    this.createdAt = currentDate;
                    this.updatedAt = currentDate;
                }
            }
            statement.close();
        }else{
            String query = "UPDATE lessons SET course_id = ?, user_id = ?, teacher_id = ?, day = ?, hour = ?, completed = ?, updated_at = ? WHERE id = ? AND deleted_at IS NULL;";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, courseID);
            statement.setInt(2, userID);
            statement.setInt(3, teacherID);
            statement.setInt(4, this.day);
            statement.setInt(5, this.hour);
            statement.setBoolean(6, this.completed);
            statement.setDate(7, currentDate);
            statement.setInt(8, this.id);
            statement.executeUpdate();
            statement.close();
            this.updatedAt = currentDate;
        }
        return this;
    }

    public Lesson delete(boolean force) throws SQLException {
        if ( this.id != 0 ){
            String query = force ? "DELETE FROM lessons WHERE id = ?;" : "UPDATE lessons SET deleted_at = NOW() WHERE id = ?;";
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.id);
            statement.executeUpdate();
            this.id = 0;
            this.deletedAt = new Date(Calendar.getInstance().getTimeInMillis());
            statement.close();
        }
        return this;
    }
}
