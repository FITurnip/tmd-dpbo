package Model;

import Database.Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

public class TScore extends Database {
    private List<PlayerScore> playerList;

    public TScore() {
        super();
        playerList = new ArrayList<>();
        setFromDatabase();
    }

    public void setFromDatabase() {
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
        PlayerScore res = null;
        for (PlayerScore player : playerList) {
            System.out.println(username + " " + player.getUsername());
            if (player.getUsername().equals(username)) {
                System.out.println("FOUND");
                res = player;
            }
        }
        return res;
    }

    public void addPlayer(PlayerScore newPlayer) {
        playerList.add(newPlayer);
    }
}
