package Model;

import Database.Database;

import java.sql.SQLException;

public class PlayerScore extends Database {
    // follow the db attribute
    String username;
    protected int score, upCounter, downCounter;

    public PlayerScore(String username, int score, int upCounter, int downCounter) {
        // use database method
        super();

        // fill the value
        this.username = username;
        this.score = score;
        this.upCounter = upCounter;
        this.downCounter = downCounter;
    }

    // save a data player
    public void saveToDatabase() throws SQLException {
        String sql = "INSERT INTO tscore (username, score, up, down) " +
                "VALUES ('" + escapeString(username) + "', " + score + ", " + upCounter + ", " + downCounter + ") " +
                "ON DUPLICATE KEY UPDATE " +
                "score = VALUES(score), " +
                "up = VALUES(up), " +
                "down = VALUES(down)";
        int result = insertUpdateDeleteQuery(sql);
    }

    // Helper method to escape single quotes in the username
    private String escapeString(String input) {
        return input == null ? null : input.replace("'", "''");
    }

    /*
     * setter and getter for all properties
     * */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUpCounter() {
        return upCounter;
    }

    public void setUpCounter(int upCounter) {
        this.upCounter = upCounter;
    }

    public int getDownCounter() {
        return downCounter;
    }

    public void setDownCounter(int downCounter) {
        this.downCounter = downCounter;
    }
}
