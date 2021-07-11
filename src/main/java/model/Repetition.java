package model;

import exception.DuplicateEntryModelException;
import support.Database;
import exception.ModelException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Repetition extends Model {
    public static ArrayList<Repetition> getAll(boolean includeDeleted) throws ModelException {
        try{
            String query = "SELECT * FROM repetitions LEFT JOIN courses ON repetitions.course_id = courses.id LEFT JOIN teachers ON repetitions.teacher_id = teachers.id";
            if ( !includeDeleted ){
                query += " WHERE repetitions.deleted_at IS NULL";
            }
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
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public static Repetition find(int id) throws ModelException {
        try{
            Repetition repetition = null;
            String query = "SELECT * FROM repetitions LEFT JOIN courses on repetitions.course_id = courses.id LEFT JOIN teachers on repetitions.teacher_id = teachers.id";
            query += " WHERE repetitions.id = ? AND repetitions.deleted_at IS NULL;";
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
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    private int id;
    private Course course;
    private Date deletedAt;
    private Teacher teacher;

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

    public Date getDeletedAt(){
        return this.deletedAt;
    }

    public Repetition setPropertiesFromResultSet(ResultSet resultSet) throws ModelException {
        try{
            this.id = Repetition.containsField(resultSet, "repetition_id") ? resultSet.getInt("repetition_id") : resultSet.getInt("id");
            this.teacher = null;
            if ( resultSet.getInt("teacher_id") > 0 ){
                this.teacher = new Teacher().setPropertiesFromResultSet(resultSet);
            }
            this.course = null;
            if ( resultSet.getInt("course_id") > 0 ){
                this.course = new Course().setPropertiesFromResultSet(resultSet);
            }
            this.deletedAt = resultSet.getTimestamp("deleted_at");
            return this;
        }catch(SQLException ex){
            throw new ModelException("SQL exception.", ex);
        }
    }

    public Repetition save() throws ModelException, RuntimeException, DuplicateEntryModelException {
        try{
            int teacherID = this.teacher == null ? 0 : this.teacher.getID();
            int courseID = this.course == null ? 0 : this.course.getID();
            if ( this.id == 0 ){
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
            }else{
                throw new RuntimeException("Cannot update an existing repetition.");
            }
            return this;
        }catch(SQLException ex){
            if ( ex.getErrorCode() == 1062 ){
                throw new DuplicateEntryModelException("Repetition already existing.", ex);
            }
            throw new ModelException("SQL exception.", ex);
        }
    }

    public Repetition delete(boolean softDelete) throws ModelException {
        try{
            if ( this.id != 0 ){
                Connection connection = Database.getConnection();
                if ( softDelete ){
                    String query = "UPDATE lessons SET deleted_at = NOW() WHERE teacher_id = ? AND course_id = ? AND completed = FALSE;";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, this.teacher.getID());
                    statement.setInt(2, this.course.getID());
                    statement.executeUpdate();
                    statement.close();
                    query = "UPDATE repetitions SET deleted_at = NOW() WHERE id = ?;";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, this.id);
                    statement.executeUpdate();
                    statement.close();
                }else{
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM repetitions WHERE id = ?;");
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
