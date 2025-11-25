package bricker.gameobjects.brick;

import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.StrategyFactory;
import bricker.main.BrickerGameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class BrickHandler {

    private BrickerGameManager brickerGameManager;
    private ImageReader imageReader;
    private final Renderable brickImage;
    private Random random;

    private StrategyFactory strategyFactory;
    private Brick[][] grid;

    private final float EDGE_BUFFER = 17f;
    private final float BRICK_BUFFER = 3f;
    private float brickWidth;


    public BrickHandler(BrickerGameManager brickerGameManager,
                        ImageReader imageReader){
        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;

        strategyFactory = new StrategyFactory(this);

        brickImage = imageReader.readImage("assets/brick.png", true);
        random = new Random();
    }

    public void initBrickGrid(Vector2 windowDimensions, int cols, int rows) {
        grid = new Brick[rows][cols];
        brickWidth = ((brickerGameManager.windowDimensions.x() - (2 * EDGE_BUFFER)) / cols) - (2 * BRICK_BUFFER);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                buildBrick(col, row);
            }
        }

    }

    public void destroyBrick(int col, int row) {
        if(grid.length <= row ||
                grid[0].length <= col ||
                grid[row][col] == null) {
            System.err.println("One or more of the brick indexes" +
                    "are out of the grid bounds, or the requested " +
                    "cell is already empty.");
            return;
        }

        brickerGameManager.removeItem(grid[row][col], Layer.STATIC_OBJECTS);
        grid[row][col] = null;
        brickerGameManager.brickCount.decrement();
    }

    public void buildBrick(int col, int row){
        if(grid.length <= row ||
                grid[0].length <= col ||
                grid[row][col] != null) {
            System.err.println("One or more of the brick indexes" +
                    "are out of the grid bounds, or the requested " +
                    "cell is already taken by another brick.");
            return;
        }

        float topLeftX = EDGE_BUFFER + (col * (brickWidth + (2 * BRICK_BUFFER))) + BRICK_BUFFER;
        float topLeftY = EDGE_BUFFER + (row * (Brick.HEIGHT_IN_PX + (2 * BRICK_BUFFER))) + BRICK_BUFFER;
        Vector2 topLeftCorner = new Vector2(topLeftX, topLeftY);
        CollisionStrategy strategy = strategyFactory.generate();

        Brick brick = new Brick(topLeftCorner,
                col,
                row,
                brickWidth,
                brickImage,
                strategy);

        grid[row][col] = brick;
        brickerGameManager.brickCount.increment();
        brickerGameManager.addItem(brick, Layer.STATIC_OBJECTS);
    }
}
