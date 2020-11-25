package Models;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class Model {
    protected static boolean containsField(ResultSet resultSet, String fieldName) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int numberOfColumns = resultSetMetaData.getColumnCount();
        for ( int i = 1 ; i < numberOfColumns + 1 ; i++ ){
            if ( resultSetMetaData.getColumnName(i).equals(fieldName) ) {
                return true;
            }
        }
        return false;
    }
}
