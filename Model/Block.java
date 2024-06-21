package Model;

import javax.swing.*;
import java.awt.*;

public class Block {
    // block image properties
    private Image image;
    private int blockPosX, blockPosY;
    private int width, height;
    private int velocity;
    private boolean onTop;

    // score properties
    private int score;
    private int scoreLabelPosX, scoreLabelPosY;
    private JLabel scoreLabel;

    public Block(String prefixDir, int width, int height, int blockPosX, int blockPosY, int velocity, boolean onTop) {
        // init properties value
        image = new ImageIcon(prefixDir + "assets/block.png").getImage();
        this.width = width;
        this.height = height;
        this.blockPosX = blockPosX;
        this.blockPosY = blockPosY;
        this.velocity = velocity;
        this.onTop = onTop;
        score = (2000 - blockPosY) / 10;
        scoreLabel = new JLabel(Integer.toString(score));
        scoreLabelPosX = blockPosX + width / 2;
        scoreLabelPosY = blockPosY - 30;
        scoreLabel.setBounds(scoreLabelPosX, scoreLabelPosY, width, 30);
    }

    /*
    * setter and getter for all properties
    * */
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getPosX() {
        return blockPosX;
    }

    public void setPosX(int blockPosX) {
        this.blockPosX = blockPosX;
        scoreLabel.setBounds(blockPosX + width / 2, scoreLabelPosY, width, 30);
    }

    public int getPosY() {
        return blockPosY;
    }

    public void setPosY(int blockPosY) {
        this.blockPosY = blockPosY;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isOnTop() {
        return onTop;
    }

    public void setOnTop(boolean onTop) {
        this.onTop = onTop;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public void setScoreLabel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }
}
