package View;

import Model.Block;
import Model.PlayerScore;
import ViewModel.GameViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.List;

public class GameBase extends JPanel implements KeyListener {
    private final GameViewModel viewModel;
    private final int panelWidth;
    private final int panelHeight;
    private final Image backgroundImage;

    private int currentBlockIndex = 0;
    private int inputAD = 0;
    private int verticalDirection = 0;

    private int keyFrameIndex = 0;

    private int verticalVelocity;
    private int upperBoundY, lowerBoundY;

    private ScorePanel scorePanel; // Reference to ScorePanel

    private int playerPosX;

    private GameOverListener gameOverListener;

    private boolean doesJump = false;
    public GameBase(PlayerScore playerScore, String currentDirectory, int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        this.backgroundImage = new ImageIcon(currentDirectory + "assets/background.jpg").getImage();

        this.viewModel = new GameViewModel(playerScore, currentDirectory, panelWidth, panelHeight);

        setPreferredSize(new Dimension(panelWidth, panelHeight));

        setFocusable(true); // Ensure the panel can receive keyboard events
        requestFocus(); // Request focus so that panel can receive key events
        addKeyListener(this);

        playerPosX = viewModel.getPlayer().getPosX();
        this.verticalVelocity = viewModel.getPlayer().getVelocity();

        // Initialize scorePanel and add to GameBase panel
        scorePanel = new ScorePanel(playerScore);
        scorePanel.setBounds(panelWidth / 100, panelHeight / 2 - 100, panelWidth, panelHeight);
        setLayout(null);
        add(scorePanel);

        Thread movementThread = new Thread(() -> {
            lowerBoundY = viewModel.getBlock(0).getPosY();
            while (true) {
                if(inputAD != 0) {
                    movePlayerHorizontal();
                }

                int lowerBoundPlayerPosY = viewModel.getPlayer().getPosY();
                lowerBoundPlayerPosY += viewModel.getPlayer().getHeight();
                if(lowerBoundPlayerPosY < lowerBoundY) {
                    viewModel.getPlayer().setPosY(viewModel.getPlayer().getPosY() + 1);
                } else if(lowerBoundPlayerPosY == lowerBoundY) {
                    if(lowerBoundY != panelHeight) updateScore();
                    else handleGameOver();
                } else {
                    handleGameOver();
                }

                if(verticalDirection != 0) {
                    int newPosY = viewModel.getPlayer().getPosY() + 3 * verticalDirection;

                    if(newPosY + viewModel.getPlayer().getHeight() >= lowerBoundY) verticalDirection = 0;
                    else viewModel.getPlayer().setPosY(newPosY);
                }

                try {
                    repaint();
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        movementThread.start();
    }

    private void handleGameOver() {
        // Remove current GameBase panel from its parent container
        Container parent = this.getParent();
        while(parent == null) parent = this.getParent();

        // Show a game-over message
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

        parent.remove(this);
        parent.revalidate();

        try {
            scorePanel.getPlayerScore().saveToDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        gameOverListener.onGameOver();
        parent.repaint();
    }

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    private void updateScore() {
        Block block = viewModel.getBlock(currentBlockIndex);
        scorePanel.updateScore(block.getScore(), block.isOnTop());
        block.setScore(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        drawBackground(g);
        drawBlocks(g);
        drawPlayer(g);
    }

    public void drawBackground(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, this);
    }

    public void drawBlocks(Graphics g) {
        List<Block> blockList = viewModel.getBlockList();
        int blockListSize = blockList.size();
        int itr = 0;
        boolean doesBlockDraw = true;
        while(doesBlockDraw) {
            Block block = blockList.get(itr);
            g.drawImage(block.getImage(), block.getPosX(), block.getPosY(), block.getWidth(), block.getHeight(), this);

            add(block.getScoreLabel());

            if(block.getPosX() < panelWidth) {
                if(itr < blockListSize) {
                    itr++;
                } else {
                    doesBlockDraw = false;
                }
            }
            else doesBlockDraw = false;
        }
    }

    public void drawPlayer(Graphics g) {
        Image playerImage;
        if(inputAD == -1) {
            playerImage = viewModel.getPlayer().getRunKeyFrame(keyFrameIndex);
            keyFrameIndex = (keyFrameIndex + 1) % 2;
        } else if(inputAD == 1) {
            playerImage = viewModel.getPlayer().getFlippedRunKeyFrame(keyFrameIndex);
            keyFrameIndex = (keyFrameIndex + 1) % 2;
        } else {
            playerImage = viewModel.getPlayer().getStayImage();
            keyFrameIndex = 0;
        }
        g.drawImage(playerImage, viewModel.getPlayer().getPosX(), viewModel.getPlayer().getPosY(), viewModel.getPlayer().getWidth(), viewModel.getPlayer().getHeight(), this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_D -> inputAD = -1;
            case KeyEvent.VK_A -> inputAD = 1;
            case KeyEvent.VK_W -> verticalDirection = -1;
            case KeyEvent.VK_S -> verticalDirection = 1;
            case KeyEvent.VK_SPACE -> handleGameOver();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_D -> inputAD = 0;
            case KeyEvent.VK_A -> inputAD = 0;
            case KeyEvent.VK_W -> verticalDirection = 0;
        }
    }

    public boolean isOnBlock(Block block) {
        int blockPosX = block.getPosX();
        int intolerantWidthVal = 20;
        boolean result = true;
        if(playerPosX + viewModel.getPlayer().getWidth() - intolerantWidthVal < blockPosX) {
            result = false;
        } else if(playerPosX > blockPosX + block.getWidth() - intolerantWidthVal) {
            result =false;
        }
        return result;
    }

    public void movePlayerHorizontal() {
        List<Block> blockList = viewModel.getBlockList();
        int blockListSize = blockList.size();

        int velocity = inputAD * blockList.getFirst().getVelocity();

        for (int i = 0; i < blockListSize; i++) {
            Block block = blockList.get(i);
            if(i == currentBlockIndex) {
                if(!isOnBlock(block)) {
                    verticalDirection = 1;
                    lowerBoundY = panelHeight;
                    changeFooting(blockList);
                }
            }
            block.setPosX(block.getPosX() + velocity);
        }
    }

    private void changeFooting(List<Block> blockList) {
        int shiftValue = 0;
        if(isOnBlock(blockList.get(currentBlockIndex + 1))) {
            shiftValue = 1;
        } else if(currentBlockIndex != 0) {
            if(isOnBlock(blockList.get(currentBlockIndex - 1))) {
                shiftValue = -1;
            }
        }

        if(shiftValue != 0) {
            currentBlockIndex += shiftValue;
            lowerBoundY = blockList.get(currentBlockIndex).getPosY();
        }
    }
}
