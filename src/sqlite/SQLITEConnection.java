package sqlite;

import io.Logging;

import java.sql.*;

/**
 * @author Pim de Witte
 */
public final class SQLITEConnection {

    private final String url;
    private Connection connection;
    private Statement statement;
    private boolean connected;

    public SQLITEConnection(String database) {
        this.url = "jdbc:sqlite:"+database;
    }

    public boolean isConnected() {
        if (!connected) {
            return false;
        }
        try {
            return connected = !connection.isClosed();
        } catch (SQLException e) {
            Logging.log("SQLite connection problem", e);
            return connected = false;
        }
    }

    public boolean statementConnected() {
        try {
            return !statement.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean close() {
        if (this.isConnected()) {
            return false;
        }
        try {
            connection.close();
            statement.close();
            connected = false;
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean connect(String username, String password) throws SQLException {
        if (connected) {
            throw new IllegalStateException("SQL is already connected.");
        }
        connection = DriverManager.getConnection(url);
        statement = connection.createStatement();
        statement.setEscapeProcessing(true);
        return connected = true;
    }

    public boolean safeConnect() {
        if (connected) {
            throw new IllegalStateException("SQL is already connected.");
        }
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            System.out.println("Connected to SQLITE");
            return connected = true;
        } catch (SQLException e) {
            //Server.getLogger().log(Level.SEVERE, e.getMessage());
            Logging.log("SQLite problem", e);
            return false;
        }
    }

    public ResultSet executeQuery(String sql) {
        return this.executeQueryInternal(sql, true);
    }

    public ResultSet executeQueryInternal(String sql, boolean retry) {
        if (!this.isConnected()) {
            return null;
        }
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            if (retry) {
                return this.executeQueryInternal(sql, false);
            }
            return null;
        }
    }

    public int executeUpdate(String sql, boolean newStatement) {
        return this.executeUpdateInternal(sql, newStatement, true);
    }

    public int executeUpdateInternal(String sql, boolean newStatement, boolean retry) {
        if (!this.isConnected()) {
            return -1;
        }
        try {
            if (newStatement) {
                Statement statement = connection.createStatement();
                int value = statement.executeUpdate(sql);
                statement.close();
                return value;
            }
            if (statement.isClosed()) {
                return -1;
            }
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            if (retry) {
                return this.executeUpdateInternal(sql, newStatement, false);
            }
            return -2;
        }
    }
}
