package Model;

import javax.swing.*;
import java.awt.*;

public class Block {
    private Image image;
    private int posX, posY;
    private int width, height;
    private int velocity;
    private boolean onTop;
    private int score;

    public Block(String prefixDir, int width, int height, int posX, int posY, int velocity, boolean onTop) {
        image = new ImageIcon(prefixDir + "assets/block.png").getImage();
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.velocity = velocity;
        this.onTop = onTop;
        score = 2000 - posY;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
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
}
