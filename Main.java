/**
 * Saya Franklin Impianro Turnip mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
 * Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
 * tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

import View.StartMenu;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    // screen properties
    public static int frameWidth, frameHeight;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // set frame
                JFrame frame = new JFrame("Fullscreen Game");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setUndecorated(true);

                setFrameSize();

                // Create StartMenu panel and add it to the frame
                StartMenu startMenu = new StartMenu(frame, frameWidth, frameHeight);
                frame.add(startMenu);

                // Set up the audio file path
                String audioFilePath = "src/assets/pensil.wav";

                // Use a separate thread for playing the audio to avoid blocking the Swing UI
                Thread audioThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // set up value
                            File audioFile = new File(audioFilePath);
                            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                            Clip clip = AudioSystem.getClip();
                            clip.open(audioStream);

                            // Loop the clip indefinitely
                            clip.loop(Clip.LOOP_CONTINUOUSLY);

                            // Start playing the background music
                            clip.start();
                        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Start the audio thread
                audioThread.start();

                // show frame
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
