package bricker.gameobjects.brick;

import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.StrategyHandler;
import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.paddle.PaddleHandler;
import bricker.main.BrickerGameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The BrickHandler class is responsible for managing the grid of bricks within a game.
 * It handles the initialization, creation, destruction,
 * and retrieval of bricks within the game environment.
 */
public class BrickHandler {

    private BrickerGameManager brickerGameManager;
    private final Renderable brickImage;

    private final StrategyHandler strategyHandler;
    private Brick[][] grid;

    private final float EDGE_BUFFER = 17f;
    private final float BRICK_BUFFER = 3f;
    private final float BRICK_HEIGHT_IN_PX = 15f;
    private float brickWidth;


    /**
     * Constructs a BrickHandler object, responsible for managing the brick grid in the game,
     * including creating, destroying, and handling interactions with bricks.
     * Initializes the necessary resources such as the strategy factory and brick images.
     *
     * @param brickerGameManager the game manager responsible for managing the game's functionality.
     * @param ballFactory the factory responsible for creating new ball objects.
     * @param paddleHandler the factory responsible for creating new paddle objects.
     * @param imageReader an ImageReader instance used for reading and loading images.
     */
    public BrickHandler(BrickerGameManager brickerGameManager,
                        BallFactory ballFactory,
                        PaddleHandler paddleHandler,
                        SoundReader soundReader,
                        ImageReader imageReader) {
        this.brickerGameManager = brickerGameManager;

        strategyHandler = new StrategyHandler(brickerGameManager,
                this,
                ballFactory,
                paddleHandler,
                soundReader,
                imageReader);

        brickImage = imageReader.readImage("assets/brick.png", true);
    }

    /**
     * Initializes the grid of bricks with the specified number of columns and rows.
     * This method calculates the brick dimensions and positions based on the game's window dimensions
     * and initializes each cell in the grid with an appropriate brick.
     *
     * @param cols the number of columns in the brick grid.
     * @param rows the number of rows in the brick grid.
     */
    public void initBrickGrid(int cols, int rows) {
        grid = new Brick[rows][cols];
        brickWidth =
                ((brickerGameManager.windowDimensions.x() - (2 * EDGE_BUFFER)) / cols) - (2 * BRICK_BUFFER);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                buildBrick(col, row);
            }
        }

    }

    /**
     * Builds a brick object at the specified column and row in the game grid.
     * This method checks the provided indices for boundaries and ensures the
     * cell is not taken by another brick before creating a new brick. The brick is
     * then added to the game manager and the corresponding grid cell is updated.
     *
     * @param col The column index where the brick will be placed in the grid.
     * @param row The row index where the brick will be placed in the grid.
     */
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
        float topLeftY = EDGE_BUFFER + (row * (BRICK_HEIGHT_IN_PX + (2 * BRICK_BUFFER))) + BRICK_BUFFER;
        Vector2 topLeftCorner = new Vector2(topLeftX, topLeftY);
        CollisionStrategy strategy = strategyHandler.generate();

        Brick brick = new Brick(topLeftCorner,
                col,
                row,
                brickWidth,
                BRICK_HEIGHT_IN_PX,
                brickImage,
                strategy);

        grid[row][col] = brick;
        brickerGameManager.addItem(brick, Layer.STATIC_OBJECTS);
        brickerGameManager.brickCount.increment();
    }

    /**
     * Removes a brick from the game grid at the specified column and row indices.
     * This method performs bounds checking to ensure the provided indices are valid
     * and the cell is not already empty. If valid, the brick is removed from the
     * game objects, and the corresponding grid cell is set to null.
     * Additionally, it decrements the brick count in the game manager.
     *
     * @param col The column index of the brick to be destroyed in the grid.
     * @param row The row index of the brick to be destroyed in the grid.
     */
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
    }

    /**
     * @param col the column index of the desired brick in the grid.
     * @param row the row index of the desired brick in the grid.
     * @return the Brick object located at the specified indices, or null if the indices are out of bounds.
     */
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
