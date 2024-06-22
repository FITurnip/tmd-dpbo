package View;

import Model.PlayerScore;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    // properties
    private JLabel scoreLabel, upLabel, downLabel;
    private PlayerScore playerScore;

    public ScorePanel(PlayerScore playerScore) {
        // init value
        this.playerScore = playerScore;
        Font defaultFont = new Font("Comic Sans MS", Font.BOLD, 20);

        // score
        scoreLabel = new JLabel("Score: " + playerScore.getScore());
        scoreLabel.setFont(defaultFont);
        scoreLabel.setBounds(10, 10, 150, 30);

        // up
        upLabel = new JLabel("Up: " + playerScore.getUpCounter());
        upLabel.setFont(defaultFont);
        upLabel.setBounds(10, 50, 100, 20);

        // down
        downLabel = new JLabel("Down: " + playerScore.getDownCounter());
        downLabel.setFont(defaultFont);
        downLabel.setBounds(10, 80, 100, 20);

        // Create score panel and add labels to it
        setLayout(null);
        setOpaque(false); // Make score panel transparent
        add(scoreLabel);
        add(upLabel);
        add(downLabel);
    }

    /**
     * Setter and gatter
     */
    public void updateScore(int point, boolean hangingBlock) {
        if(point != 0) {
            playerScore.setScore(playerScore.getScore() + point);
            scoreLabel.setText("Score: " + playerScore.getScore());

            if(hangingBlock) {
                playerScore.setUpCounter(playerScore.getUpCounter() + 1);
                upLabel.setText("Up: " + playerScore.getUpCounter());
            } else {
                playerScore.setDownCounter(playerScore.getDownCounter() + 1);
                downLabel.setText("Down: " + playerScore.getDownCounter());
            }
        }
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public void setScoreLabel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public JLabel getUpLabel() {
        return upLabel;
    }

    public void setUpLabel(JLabel upLabel) {
        this.upLabel = upLabel;
    }

    public JLabel getDownLabel() {
        return downLabel;
    }

    public void setDownLabel(JLabel downLabel) {
        this.downLabel = downLabel;
    }

    public PlayerScore getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(PlayerScore playerScore) {
        this.playerScore = playerScore;
    }
}
