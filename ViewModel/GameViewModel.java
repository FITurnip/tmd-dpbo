package ViewModel;
import Model.Block;
import Model.Player;
import Model.PlayerScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameViewModel {
    private final String rootDirectory;
    private final ArrayList<Block> blockList;
    private Player player;
    private final Random random;

    int panelWidth, panelHeight;
    int blockWidth, blockHeight;
    int velocity;

    public GameViewModel(PlayerScore playerScore, String currentDirectory, int panelWidth, int panelHeight) {
        this.rootDirectory = currentDirectory;
        this.blockList = new ArrayList<>();
        this.random = new Random();
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        initializeBlocks();
        this.player = new Player(
                currentDirectory,
                blockList.getFirst().getPosX(),
                blockList.getFirst().getPosY() - 100,
                playerScore);

        Thread threadBlock = new Thread(() -> {
            while(true) {
                makeBlock(velocity);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        threadBlock.start();
    }

    private void initializeBlocks() {
        blockWidth = 200;
        blockHeight = 500;
        velocity = 5;

        makeBlock(velocity);

        for (int i = 0; i < 5; i++) {
            makeBlock(velocity);
        }
    }

    private void makeBlock(int velocity) {
        boolean topBlockCond = random.nextBoolean();
        int newPosY = (int) ((double) panelHeight / 2 + Math.random() * 2 * blockHeight / 3);
        int lastPosX = blockWidth;

        if (blockList.isEmpty()) {
            topBlockCond = false;
        } else {
            lastPosX = blockList.get(blockList.size() - 1).getPosX() + lastPosX * 2;
            if(topBlockCond) {
                newPosY = panelHeight - newPosY;
                blockHeight = 100;
            } else {
                blockHeight = 500;
            }
        }

        blockList.add(new Block(rootDirectory, blockWidth, blockHeight, lastPosX, newPosY, velocity, topBlockCond));
    }

    public synchronized List<Block> getBlockList() {
        return new ArrayList<>(blockList);
    }

    public Block getBlock(int index) {
        return blockList.get(index);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
