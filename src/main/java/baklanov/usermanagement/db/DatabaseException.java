package baklanov.usermanagement.db;

import java.sql.SQLException;

public class DatabaseException extends Exception {
    public DatabaseException(SQLException e) {
    }

    public DatabaseException(String s) {
    }
}
