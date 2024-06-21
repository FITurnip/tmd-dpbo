package Database;

import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    public Database() {
        // prepare environment value
        String host = "localhost";
        String port = "3306";
        String database = "updowndb";
        String user = "root";
        String password = "";

        // make connection to mysql
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectQuery(String sql) {
        // execute select query
        try {
            statement.executeQuery(sql);
            return statement.getResultSet();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertUpdateDeleteQuery(String sql) {
        // execute insert, update, or delete query
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Statement getStatement() {
        // return statement
        return statement;
    }
}
