package View;

import ViewModel.GameViewModel;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private JLabel scoreLabel, upLabel, downLabel;
    private int score, upCounter, downCounter;

    public ScorePanel(GameViewModel viewModel) {
        this.score = viewModel.getPlayer().getScore();
        Font defaultFont = new Font("Comic Sans MS", Font.BOLD, 20);

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(defaultFont);
        scoreLabel.setBounds(10, 10, 150, 30);

        upLabel = new JLabel("Up: " + upCounter);
        upLabel.setFont(defaultFont);
        upLabel.setBounds(10, 50, 100, 20);

        downLabel = new JLabel("Down: " + downCounter);
        downLabel.setFont(defaultFont);
        downLabel.setBounds(10, 80, 100, 20);

        // Create score panel and add labels to it
        setLayout(null); // Using GridLayout for vertical arrangement
        setOpaque(false); // Make score panel transparent
        add(scoreLabel);
        add(upLabel);
        add(downLabel);
    }

    public void updateScore(int point) {
        this.score += point;
        scoreLabel.setText("Score: " + score);
    }

    // Additional methods for updating upCounter and downCounter
    public void updateUpCounter(int upIncrement) {
        this.upCounter += upIncrement;
        upLabel.setText("Up: " + upCounter);
    }

    public void updateDownCounter(int downIncrement) {
        this.downCounter += downIncrement;
        downLabel.setText("Down: " + downCounter);
    }

    // Getters and setters for score, upCounter, and downCounter
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
        upLabel.setText("Up: " + upCounter);
    }

    public int getDownCounter() {
        return downCounter;
    }

    public void setDownCounter(int downCounter) {
        this.downCounter = downCounter;
        downLabel.setText("Down: " + downCounter);
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public void setScoreLabel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }
}
