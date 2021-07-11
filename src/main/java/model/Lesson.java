package model;

import support.Database;
import java.sql.*;
import java.util.*;
import java.util.Date;
import exception.ModelException;

public class Lesson extends Model {
    public static final int MIN_DAY = 1;
    public static final int MAX_DAY = 5;
    public static final int MIN_START_HOUR = 15;
    public static final int MAX_START_HOUR = 18;

    private static String getLessonStringIdentifier(int courseID, int teacherID, int day, int hour){
        String lessonStringIdentifier = Integer.toString(courseID);
        lessonStringIdentifier += Integer.toString(teacherID);
        lessonStringIdentifier += Integer.toString(day);
        lessonStringIdentifier += Integer.toString(hour);
        return lessonStringIdentifier;
    }

    public static Lesson find(int id) throws ModelException {
        try{
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
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public static ArrayList<Lesson> getAll(boolean includeDeleted, boolean uncompletedOnly) throws ModelException {
        try{
            String query = "SELECT * FROM lessons LEFT JOIN courses ON lessons.course_id = courses.id LEFT JOIN users ON lessons.user_id = users.id LEFT JOIN teachers ON lessons.teacher_id = teachers.id";
            ArrayList<String> filters = new ArrayList<>();
            if ( !includeDeleted ){
                filters.add("lessons.deleted_at IS NULL");
            }
            if ( uncompletedOnly ){
                filters.add("completed = FALSE");
            }
            if ( !filters.isEmpty() ){
                query += " WHERE " + String.join(" AND ", filters);
            }
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
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public static ArrayList<Lesson> getAllByUser(User user, boolean includeDeleted) throws ModelException {
        try{
            String query = "SELECT * FROM lessons LEFT JOIN courses ON lessons.course_id = courses.id LEFT JOIN users ON lessons.user_id = users.id LEFT JOIN teachers ON lessons.teacher_id = teachers.id";
            if ( user == null || user.getID() == 0 ){
                query += includeDeleted ? ";" : " WHERE lessons.deleted_at IS NULL;";
            }else{
                query += " WHERE user_id = ?" + ( includeDeleted ? ";" : " AND lessons.deleted_at IS NULL;" );
            }
            ArrayList<Lesson> elements = new ArrayList<>();
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            if ( user != null && user.getID() > 0 ){
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
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public static ArrayList<HashMap<String, Object>> getAvailable(User user) throws ModelException, IllegalArgumentException {
        if ( user == null || user.getID() == 0 ){
            throw new IllegalArgumentException("Invalid user.");
        }
        try{
            ArrayList<HashMap<String, Object>> availableLessons = new ArrayList<>();
            Connection connection = Database.getConnection();
            ArrayList<Lesson> lessonList = Lesson.getAll(false, true);
            HashMap<String, Lesson> bookedLessons = new HashMap<>();
            for ( Lesson lesson : lessonList ){
                String id = Lesson.getLessonStringIdentifier(lesson.getCourse().getID(), lesson.getTeacher().getID(), lesson.getDay(), lesson.getHour());
                bookedLessons.put(id, lesson);
            }
            String allCoursesQuery = "SELECT * FROM repetitions LEFT JOIN courses ON repetitions.course_id = courses.id LEFT JOIN teachers ON repetitions.teacher_id = teachers.id";
            allCoursesQuery += " WHERE courses.deleted_at IS NULL AND teachers.deleted_at IS NULL ORDER BY course_id";
            PreparedStatement statement = connection.prepareStatement(allCoursesQuery);
            ResultSet resultSet = statement.executeQuery();
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
                ArrayList<HashMap<String, Object>> slots = new ArrayList<>();
                Teacher teacher = new Teacher();
                teacher.setPropertiesFromResultSet(resultSet);
                element.put("teacher", teacher);
                element.put("slots", slots);
                for ( int day = Lesson.MIN_DAY ; day <= Lesson.MAX_DAY ; day ++ ) {
                    for ( int hour = Lesson.MIN_START_HOUR ; hour <= Lesson.MAX_START_HOUR ; hour++ ) {
                        String id = Lesson.getLessonStringIdentifier(resultSet.getInt("course_id"), resultSet.getInt("teacher_id"), day, hour);
                        Lesson bookedLesson = bookedLessons.get(id);
                        boolean isMine = bookedLesson != null && user.getID() == bookedLesson.getUser().getID();
                        HashMap<String, Object> slot = new HashMap<>();
                        slot.put("available", bookedLessons.containsKey(id) ? 0 : 1);
                        slot.put("isMine", isMine ? 1 : 0);
                        slot.put("hour", hour);
                        slot.put("day", day);
                        if ( isMine || ( user.isAdmin() && bookedLesson != null ) ){
                            slot.put("lesson", bookedLesson);
                        }
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
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public static boolean isLessonAvailable(int courseID, int teacherID, int day, int hour) throws ModelException {
        try{
            String query = "SELECT id FROM lessons WHERE teacher_id = ? AND course_id = ? AND hour = ? AND day = ? AND deleted_at IS NULL AND completed = FALSE LIMIT 1;";
            PreparedStatement statement = Database.getConnection().prepareStatement(query);
            statement.setInt(1, teacherID);
            statement.setInt(2, courseID);
            statement.setInt(3, hour);
            statement.setInt(4, day);
            ResultSet resultSet = statement.executeQuery();
            boolean isLessonAvailable = true;
            while( isLessonAvailable && resultSet.next() ){
                isLessonAvailable = false;
            }
            return isLessonAvailable;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
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
        if ( day < Lesson.MIN_DAY || day > Lesson.MAX_DAY ){
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
        if ( hour < Lesson.MIN_START_HOUR || hour > Lesson.MAX_START_HOUR ){
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

    public Lesson setPropertiesFromResultSet(ResultSet resultSet) throws ModelException {
        try{
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
            }catch(Exception ignored){}
            if ( userID > 0 ){
                this.user = new User().setPropertiesFromResultSet(resultSet);
            }
            this.teacher = null;
            if ( resultSet.getInt("teacher_id") > 0 ){
                this.teacher = new Teacher().setPropertiesFromResultSet(resultSet);
            }
            try{
                this.day = resultSet.getInt("day");
            }catch(Exception ignored){}
            try{
                this.hour = resultSet.getInt("hour");
            }catch(Exception ignored){}
            this.createdAt = resultSet.getTimestamp("created_at");
            this.updatedAt = resultSet.getTimestamp("updated_at");
            this.deletedAt = resultSet.getTimestamp("deleted_at");
            this.completed = resultSet.getBoolean("completed");
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public Lesson save() throws RuntimeException, ModelException {
        if ( this.day <= 0 || this.day > 5 ){
            throw new RuntimeException("Invalid day defined.");
        }
        if ( this.hour < 15 || this.hour > 18 ){
            throw new RuntimeException("Invalid hour defined.");
        }
        try{
            int courseID = this.course == null ? 0 : this.course.getID();
            int userID = this.user == null ? 0 : this.user.getID();
            int teacherID = this.teacher == null ? 0 : this.teacher.getID();
            Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
            if ( this.id == 0 ){
                String query = "INSERT INTO lessons (course_id, user_id, teacher_id, day, hour, completed, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW());";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, courseID);
                statement.setInt(2, userID);
                statement.setInt(3, teacherID);
                statement.setInt(4, this.day);
                statement.setInt(5, this.hour);
                statement.setBoolean(6, this.completed);
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
                String query = "UPDATE lessons SET course_id = ?, user_id = ?, teacher_id = ?, day = ?, hour = ?, completed = ?, updated_at = NOW() WHERE id = ? AND deleted_at IS NULL;";
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, courseID);
                statement.setInt(2, userID);
                statement.setInt(3, teacherID);
                statement.setInt(4, this.day);
                statement.setInt(5, this.hour);
                statement.setBoolean(6, this.completed);
                statement.setInt(7, this.id);
                statement.executeUpdate();
                statement.close();
                this.updatedAt = currentDate;
            }
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public Lesson delete(boolean force) throws ModelException {
        try{
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
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }
}
