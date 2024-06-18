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

    private boolean isFall = false;
    private int currentBlockIndex = 0;
    private int inputAD = 0;
    private int verticalDirection = 0;
    private int jumpPos = 0;

    private int keyFrameIndex = 0;

    private int verticalVelocity;
    private int upperBoundY, lowerBoundY;

    public GameBase(String currentDirectory, int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.backgroundImage = new ImageIcon(currentDirectory + "assets/background.jpg").getImage();

        this.viewModel = new GameViewModel(currentDirectory, panelWidth, panelHeight);

        setPreferredSize(new Dimension(panelWidth, panelHeight));

        setFocusable(true); // Ensure the panel can receive keyboard events
        requestFocus(); // Request focus so that panel can receive key events
        addKeyListener(this);

        verticalVelocity = viewModel.getPlayer().getVelocity();

        Thread movementThread = new Thread(() -> {
            while (true) {
                if(inputAD != 0) {
                    movePlayerHorizontal();
                }

                if(verticalDirection != 0) {
                    if(verticalDirection == -1) {
                        if(viewModel.getPlayer().getPosY() <= upperBoundY) {
                            verticalDirection = 1;
                        }
                    }

                    if(verticalDirection == 1) {
                        System.out.println("jalan 1");
                        if(viewModel.getPlayer().getPosX() + viewModel.getPlayer().getWidth() >= viewModel.getBlock(currentBlockIndex + 1).getPosX()) {
                            System.out.println("jalan 2");
                            currentBlockIndex++;
                            lowerBoundY = viewModel.getBlock(currentBlockIndex).getPosY() - viewModel.getPlayer().getHeight();
                            verticalVelocity = 0;
                        }

                        if(viewModel.getPlayer().getPosY() >= lowerBoundY) {
                            verticalDirection = 0;
                        }
                    }
                    movePlayerVertical();
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

        for (Block block : blockList) {
            g.drawImage(block.getImage(), block.getPosX(), block.getPosY(), block.getWidth(), block.getHeight(), this);
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
                if (verticalDirection != 1) {
                    verticalDirection = -1;
                    upperBoundY = viewModel.getBlock(currentBlockIndex).getPosY() - 400;
                    if(upperBoundY < 0) upperBoundY = 0;
                    lowerBoundY = viewModel.getBlock(currentBlockIndex).getPosY() - viewModel.getPlayer().getHeight();
                }
            }
            case KeyEvent.VK_S -> {
                if (verticalDirection != 1) verticalDirection = 1;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_D -> inputAD = 0;
            case KeyEvent.VK_A -> inputAD = 0;
        }
    }

    public boolean isOnBlock(Block block) {
        int blockPosX = block.getPosX();
        int playerPosX = viewModel.getPlayer().getPosX();
        boolean isOnBlockCond = true;

        if(inputAD == 1) {
            playerPosX += viewModel.getPlayer().getWidth();
            if(blockPosX > playerPosX) {
                isOnBlockCond = false;
            }
        } else if(inputAD == -1) {
            blockPosX += block.getWidth();
            if(playerPosX > blockPosX) {
                isOnBlockCond = false;
            }
        }

        return isOnBlockCond;
    }

    public void movePlayerHorizontal() {
        List<Block> blockList = viewModel.getBlockList();
        int blockListSize = blockList.size();

        int velocity = inputAD * blockList.getFirst().getVelocity();

        for (int i = 0; i < blockListSize; i++) {
            Block block = blockList.get(i);
            if(i == currentBlockIndex) {
                isFall = !isOnBlock(block);
                if(isFall) {
                    verticalDirection = 1;
                    lowerBoundY = panelHeight - viewModel.getPlayer().getHeight();
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
