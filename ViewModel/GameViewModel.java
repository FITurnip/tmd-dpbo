package ViewModel;
import Model.Block;
import Model.Player;
import Model.PlayerScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameViewModel {
    // block and player properties
    private final String rootDirectory;
    private final ArrayList<Block> blockList;
    private Player player;
    private final Random random;
    int panelWidth, panelHeight;
    int blockWidth, blockHeight;
    int velocity;

    public GameViewModel(PlayerScore playerScore, String currentDirectory, int panelWidth, int panelHeight) {
        // get val
        this.rootDirectory = currentDirectory;
        this.blockList = new ArrayList<>();
        this.random = new Random();
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        // set blocks
        initializeBlocks();

        // save new player data
        this.player = new Player(
                currentDirectory,
                blockList.getFirst().getPosX(),
                blockList.getFirst().getPosY() - 100,
                playerScore);


        // produce blocks
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
        // init val
        blockWidth = 200;
        blockHeight = 500;
        velocity = 5;

        // make 5 blocks
        for (int i = 0; i < 6; i++) {
            makeBlock(velocity);
        }
    }

    private void makeBlock(int velocity) {
        // set rand val to set up hanging block the height
        boolean topBlockCond = random.nextBoolean();
        int newPosY = (int) ((double) panelHeight / 2 + Math.random() * 2 * blockHeight / 3);

        // set last position of block
        int lastPosX = blockWidth;

        if (blockList.isEmpty()) {
            // first block must not hang on
            topBlockCond = false;
        } else {
            // reset last position of block
            lastPosX = blockList.get(blockList.size() - 1).getPosX() + lastPosX * 2;

            // set height
            if(topBlockCond) {
                newPosY = panelHeight - newPosY;
                blockHeight = 100;
            } else {
                blockHeight = 500;
            }
        }

        // make block
        blockList.add(new Block(rootDirectory, blockWidth, blockHeight, lastPosX, newPosY, velocity, topBlockCond));
    }

    /**
     * Setter and getter
     */
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
