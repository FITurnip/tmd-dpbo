package View;

import Model.Block;
import ViewModel.GameViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    private boolean doesJump = false;
    public GameBase(String currentDirectory, int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        this.backgroundImage = new ImageIcon(currentDirectory + "assets/background.jpg").getImage();

        this.viewModel = new GameViewModel(currentDirectory, panelWidth, panelHeight);

        setPreferredSize(new Dimension(panelWidth, panelHeight));

        setFocusable(true); // Ensure the panel can receive keyboard events
        requestFocus(); // Request focus so that panel can receive key events
        addKeyListener(this);

        playerPosX = viewModel.getPlayer().getPosX();
        this.verticalVelocity = viewModel.getPlayer().getVelocity();

        // Initialize scorePanel and add to GameBase panel
        scorePanel = new ScorePanel(viewModel);
        scorePanel.setBounds(panelWidth / 100, panelHeight / 2 - 100, panelWidth, panelHeight);
        setLayout(null);
        add(scorePanel);

        Thread movementThread = new Thread(() -> {
            while (true) {
                if(inputAD != 0) {
                    movePlayerHorizontal();
                }

                if(verticalDirection == -1) {
                    int playerPosY = viewModel.getPlayer().getPosY();
                    if(playerPosY > upperBoundY) movePlayerVertical();
                    else {
                        doesJump = false;
                        verticalDirection = 1;
                    }
                }

                if(verticalDirection == 1) {
                    int playerPosY = viewModel.getPlayer().getPosY();
                    playerPosY += viewModel.getPlayer().getHeight();
                    if(playerPosY < lowerBoundY) {
                        movePlayerVertical();
                    } else {
                        verticalDirection = 0;
                        if(playerPosY > panelHeight - 150) handleGameOver();
                        else updateScore();
                    }
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
        // Show a game-over message
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        // Optionally, reset the game or exit
    }
    private void updateScore() {
        Block block = viewModel.getBlock(currentBlockIndex);
        scorePanel.updateScore(block.getScore());
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
            case KeyEvent.VK_W -> {
                if(!doesJump) {
                    upperBoundY = viewModel.getBlock(currentBlockIndex).getPosY() - viewModel.getPlayer().getHeight() - 100;
                }

                upperBoundY -= 20;
                doesJump = true;
                verticalDirection = -1;

                if(upperBoundY < 0) upperBoundY = 0;
                if (verticalDirection != 1) {
                    lowerBoundY = viewModel.getBlock(currentBlockIndex).getPosY() - viewModel.getPlayer().getHeight();
                }
            }
            case KeyEvent.VK_S -> {
                verticalDirection = 1;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_D -> inputAD = 0;
            case KeyEvent.VK_A -> inputAD = 0;
            case KeyEvent.VK_W -> verticalDirection = -1;
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
                    if(isOnBlock(blockList.get(currentBlockIndex + 1))) {
                        currentBlockIndex++;
                        lowerBoundY = blockList.get(currentBlockIndex).getPosY();
                    } else if(currentBlockIndex != 0) {
                        if(isOnBlock(blockList.get(currentBlockIndex - 1))) {
                            currentBlockIndex--;
                            lowerBoundY = blockList.get(currentBlockIndex).getPosY();
                        }
                    }
                }
            }
            block.setPosX(block.getPosX() + velocity);
        }
    }

    public void movePlayerVertical() {
        int velocity = viewModel.getPlayer().getVelocity();
        if(verticalDirection == 1) velocity /= 2;
        int finalVelocity = verticalDirection * velocity;
        viewModel.getPlayer().setPosY(viewModel.getPlayer().getPosY() + finalVelocity);
    }
}
