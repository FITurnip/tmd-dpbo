package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player {
    private Image stayImage;
    private ArrayList<Image> runKeyFrameList, flippedRunKeyFrameList;
    private int width, height;
    private int posX, posY;
    private int velocity;
    private int score;

    public Player(String prefixDir, int posX, int posY) {
        this.stayImage = new ImageIcon(prefixDir + "assets/player/stay.png").getImage();
        this.runKeyFrameList = new ArrayList<>();

        this.runKeyFrameList.add(new ImageIcon(prefixDir + "assets/player/run_keyframe_1.png").getImage());
        this.runKeyFrameList.add(new ImageIcon(prefixDir + "assets/player/run_keyframe_2.png").getImage());

        // Flip runKeyFrame horizontally
        this.flippedRunKeyFrameList = new ArrayList<>();
        this.flippedRunKeyFrameList.add(flipImageHorizontally(this.runKeyFrameList.get(0)));
        this.flippedRunKeyFrameList.add(flipImageHorizontally(this.runKeyFrameList.get(1)));

        this.posX = posX;
        this.posY = posY;

        this.width = 50;
        this.height = 100;
        this.velocity = 5;
        this.score = 0;
    }

    private Image flipImageHorizontally(Image img) {
        BufferedImage bufferedImage = toBufferedImage(img);
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-bufferedImage.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        bufferedImage = op.filter(bufferedImage, null);
        return bufferedImage;
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bufferedImage = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bufferedImage;
    }

    public Image getStayImage() {
        return stayImage;
    }

    public void setStayImage(Image stayImage) {
        this.stayImage = stayImage;
    }

    public Image getRunKeyFrame(int i) {
        return runKeyFrameList.get(i);
    }

    public void setRunKeyFrame(Image runKeyFrame) {
        this.runKeyFrameList.add(runKeyFrame);
    }

    public Image getFlippedRunKeyFrame(int i) {
        return flippedRunKeyFrameList.get(i);
    }

    public void setFlippedRunKeyFrame(Image flippedRunKeyFrame) {
        this.flippedRunKeyFrameList.add(flippedRunKeyFrame);
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
