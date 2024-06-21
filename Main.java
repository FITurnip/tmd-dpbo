import View.StartMenu;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static int frameWidth, frameHeight;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Fullscreen Game");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setUndecorated(true);

                setFrameSize();

                // Create and add StartMenu to the frame
                StartMenu startMenu = new StartMenu(frame, frameWidth, frameHeight);
                frame.add(startMenu);

                frame.setVisible(true);
            }
        });
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
