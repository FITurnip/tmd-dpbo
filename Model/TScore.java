package Model;

import Database.Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

public class TScore extends Database {
    // player score data list
    private List<PlayerScore> playerList;

    public TScore() {
        // use db method
        super();

        // save all data from db
        playerList = new ArrayList<>();
        setFromDatabase();
    }

    /**
     * setter and getter
     */
    public void setFromDatabase() {
        // set from db
        try {
            ResultSet resultSet = selectQuery("SELECT * FROM tscore");
            String username;
            int score, up, down;

            while (resultSet.next()) {
                username = resultSet.getString("username");
                score = resultSet.getInt("score");
                up = resultSet.getInt("up");
                down = resultSet.getInt("down");
                PlayerScore playerScore = new PlayerScore(username, score, up, down);
                playerList.add(playerScore);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PlayerScore> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerScore> playerList) {
        this.playerList = playerList;
    }

    public PlayerScore getPlayer(String username) {
        // get player by username
        PlayerScore res = null;
        for (PlayerScore player : playerList) {
            if (player.getUsername().equals(username)) {
                res = player;
            }
        }
        return res;
    }

    public void addPlayer(PlayerScore newPlayer) {
        playerList.add(newPlayer);
    }
}
