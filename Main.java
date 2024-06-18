import View.GameBase;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static int frameWidth, frameHeight;

    public static void main(String[] args) {
        String currentDirectory = System.getProperty("user.dir") + "/src/";
        // Print the current directory
        System.out.println("Current directory: " + currentDirectory);

        // Create a JFrame instance
        JFrame frame = new JFrame("Fullscreen Game");

        // Set the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an instance of the Game panel
        setFrameSize();
        GameBase gameBasePanel = new GameBase(currentDirectory, frameWidth, frameHeight);

        // Add the Game panel to the frame
        frame.add(gameBasePanel);

        // Set the frame to fullscreen mode
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        // Set the frame visible
        frame.setVisible(true);
    }

    public static void setFrameSize() {
        // Get the local graphics environment
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Get the default screen device
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        // Get the display mode of the screen
        DisplayMode displayMode = graphicsDevice.getDisplayMode();

        frameWidth = displayMode.getWidth();
        frameHeight = displayMode.getHeight();
    }
}
