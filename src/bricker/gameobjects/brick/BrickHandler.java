package bricker.gameobjects.brick;

import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.StrategyFactory;
import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.paddle.PaddleFactory;
import bricker.main.BrickerGameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class BrickHandler {

    private BrickerGameManager brickerGameManager;
    private final Renderable brickImage;

    private StrategyFactory strategyFactory;
    private BallFactory ballFactory;
    private Brick[][] grid;

    private final float EDGE_BUFFER = 17f;
    private final float BRICK_BUFFER = 3f;
    private float brickWidth;


    public BrickHandler(BrickerGameManager brickerGameManager,
                        BallFactory ballFactory,
                        PaddleFactory paddleFactory,
                        ImageReader imageReader) {
        this.brickerGameManager = brickerGameManager;
        this.ballFactory = ballFactory;

        strategyFactory = new StrategyFactory(brickerGameManager,
                this,
                ballFactory,
                paddleFactory);

        brickImage = imageReader.readImage("assets/brick.png", true);
    }

    public void initBrickGrid(int cols, int rows) {
        grid = new Brick[rows][cols];
        brickWidth = ((brickerGameManager.windowDimensions.x() - (2 * EDGE_BUFFER)) / cols) - (2 * BRICK_BUFFER);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                buildBrick(col, row);
            }
        }

    }

    public void destroyBrick(int col, int row) {
        if (row < 0 ||
                grid.length <= row ||
                col < 0 ||
                grid[0].length <= col) {
            System.err.println("One or more of the brick indexes " +
                    "are out of the grid bounds, or the requested " +
                    "cell is already empty.");
            return;
        }

        if (grid[row][col] == null) {
            return;
        }

        brickerGameManager.removeItem(grid[row][col], Layer.STATIC_OBJECTS);
        grid[row][col] = null;
        brickerGameManager.brickCount.decrement();
        System.out.println(brickerGameManager.brickCount.value());
    }

    public void buildBrick(int col, int row) {
        if (row < 0 ||
                grid.length <= row ||
                col < 0 ||
                grid[0].length <= col) {
            System.err.println("One or more of the brick indexes" +
                    "are out of the grid bounds, or the requested " +
                    "cell is already taken by another brick.");
            return;
        }
        if (grid[row][col] != null) {
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
        brickerGameManager.addItem(brick, Layer.STATIC_OBJECTS);
        brickerGameManager.brickCount.increment();
        System.out.println(brickerGameManager.brickCount.value());
    }

    public Brick getBrick(int col, int row) {
        if (row < 0 ||
                grid.length <= row ||
                col < 0 ||
                grid[0].length <= col) {
            return null;
        }
        return grid[row][col];
    }
}
