package Database;

import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    public Database() {
        String host = "localhost";
        String port = "3306";
        String database = "updowndb";
        String user = "root";
        String password = "";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectQuery(String sql) {
        try {
            statement.executeQuery(sql);
            return statement.getResultSet();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertUpdateDeleteQuery(String sql) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Statement getStatement() {
        return statement;
    }
}
