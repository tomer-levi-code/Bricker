package bricker.main;

import bricker.gameobjects.health_points.HealthPointsPanel;
import bricker.gameobjects.ball.Ball;
import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.ball.BallType;
import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.PaddleHandler;
import bricker.gameobjects.paddle.PaddleType;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.paddle.Paddle;

import java.awt.event.KeyEvent;

/**
 * The BrickerGameManager class serves as the primary game manager for the Bricker game.
 * It is responsible for initializing the game components, managing gameplay mechanics,
 * and handling user input and game states. This class extends the GameManager class
 * and customizes its functionality for the Bricker game.
 */
public class BrickerGameManager extends GameManager {

    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final int DEFAULT_BRICK_COLS = 8;

    private UserInputListener inputListener;
    private WindowController windowController;
    public static Vector2 windowDimensions;
    private static int rows;
    private static int cols;

    public static HealthPointsPanel healthPointsPanel;
    private Ball mainBall;
    public static Counter brickCount = new Counter(0);

    /**
     * Constructs a new BrickerGameManager instance to manage the Bricker game.
     *
     * @param windowTitle The title of the game window.
     * @param windowDimensions The dimensions of the game window as a Vector2 object.
     * @param cols The number of columns of bricks in the game.
     * @param rows The number of rows of bricks in the game.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int cols, int rows) {
        super(windowTitle, windowDimensions);
        BrickerGameManager.rows = rows;
        BrickerGameManager.cols = cols;
    }

    /**
     * Initializes the Bricker game, setting up game objects, the game window, and
     * other resources required for the game operation. This method is responsible
     * for configuring the game environment, creating objects like the ball, paddle,
     * walls, background, and initializing their placement and behaviors.
     *
     * @param imageReader     Utility for reading image files to generate renderable
     *                        objects for game appearances.
     * @param soundReader     Utility for reading sound files to provide audio effects.
     * @param inputListener   Listener to capture and handle user input events such as
     *                        keyboard or mouse interactions.
     * @param windowController Controller for managing the game window, including
     *                         dimensions, frame rate, and window-related events.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.inputListener = inputListener;

        //Initialize window parameters.
        windowController.setTargetFramerate(80);
        windowDimensions = windowController.getWindowDimensions();
        Vector2 windowCenter = windowDimensions.mult(0.5f);

        //Initialize background
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", true);
        GameObject background = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(),
                        windowDimensions.y()),
                backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        //Initialize walls
        createWalls(windowDimensions);

        //Initialize main ball
        BallFactory ballFactory = BallFactory.getInstance(this,
                imageReader,
                soundReader);
        mainBall = ballFactory.build(BallType.MAIN, windowCenter);

        //Initialize user paddle
        PaddleHandler paddleHandler = new PaddleHandler(this,
                windowDimensions,
                inputListener,
                imageReader);
        Paddle userPaddle = paddleHandler.build(PaddleType.USER);

        //Initialize brick grid
        BrickHandler brickHandler = new BrickHandler(this,
                ballFactory,
                paddleHandler,
                soundReader,
                imageReader);
        brickHandler.initBrickGrid(cols, rows);

        //Initialize HP Panel
        healthPointsPanel = new HealthPointsPanel(this,
                windowDimensions,
                imageReader);
        healthPointsPanel.initHP(windowDimensions);


        addItem(background, Layer.BACKGROUND);
        addItem(mainBall, Layer.DEFAULT);
        addItem(userPaddle, Layer.DEFAULT);


    }

    /**
     * Updates the game state with each frame based on the elapsed time since last update.
     * The method handles changes in game logic such as determining the end of the game,
     * checking for the ball falling below the screen, and listening for specific user inputs.
     *
     * @param deltaTime The time, in seconds, that has elapsed since the last update call.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
        checkForStrike();
        easyWayOutListener();
    }

    /**
     * Monitors user input to check if the "W" key has been pressed. If detected,
     * triggers a reset of the game's brick count. This method is intended to provide
     * an optional shortcut within the game to reset the brick state.
     */
    private void easyWayOutListener() {
        if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            brickCount.reset();
        }
    }

    private void checkForGameEnd() {

        String prompt = "";
        if (brickCount.value() <= 0) {
            prompt = "You win!";
        }
        if (healthPointsPanel.getHP() == 0) {
            prompt = "You lose!";
        }
        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt)) {
                brickCount.reset();
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    private void createWalls(Vector2 windowDimensions) {
        final float WALL_THICKNESS = 100;
        GameObject leftWall = new GameObject(new Vector2(-WALL_THICKNESS, 0), new Vector2(WALL_THICKNESS, windowDimensions.y()), null),
                rightWall = new GameObject(new Vector2(windowDimensions.x(), 0), new Vector2(WALL_THICKNESS, windowDimensions.y()), null),
                topWall = new GameObject(new Vector2(0, -WALL_THICKNESS), new Vector2(windowDimensions.x(), WALL_THICKNESS), null);
        addItem(leftWall, Layer.STATIC_OBJECTS);
        addItem(rightWall, Layer.STATIC_OBJECTS);
        addItem(topWall, Layer.STATIC_OBJECTS);
    }

    /**
     * Adds a game object to the game in the specified rendering layer.
     *
     * @param item The GameObject to be added.
     * @param layer The rendering layer in which the GameObject should be placed.
     */
    public void addItem(GameObject item, int layer) {
        gameObjects().addGameObject(item, layer);
    }

    /**
     * Removes a specific game object from the game in the specified rendering layer.
     *
     * @param item The GameObject to be removed.
     * @param layer The rendering layer from which the GameObject should be removed.
     */
    public void removeItem(GameObject item, int layer) {
        gameObjects().removeGameObject(item, layer);
    }

    /**
     * Increases the player's health points (HP) in the Bricker game.
     * This method communicates with the healthPointsPanel object to visually
     * update and reflect the addition of a health point in the game’s UI.
     */
    public void increaseHP() {
        healthPointsPanel.increaseHP();
    }

    private void checkForStrike() {
        double ballHeight = mainBall.getCenter().y();

        if (ballHeight > windowDimensions.y()) {
            healthPointsPanel.decreaseHP();
            mainBall.reset();
        }
    }

    /**
     * The main method serves as the entry point for launching the Bricker game.
     * It initializes the game with default or user-specified dimensions for the brick rows
     * and columns, then starts the game using a GameManager instance.
     *
     * @param args Command-line arguments where:
     *             args[0] specifies the number of columns of bricks (optional).
     *             args[1] specifies the number of rows of bricks (optional).
     */
    public static void main(String[] args) {
        int rows = DEFAULT_BRICK_ROWS,
                cols = DEFAULT_BRICK_COLS;
        if (args.length == 2) {
            cols = Integer.parseInt(args[0]);
            rows = Integer.parseInt(args[1]);
        }
        GameManager manager = new BrickerGameManager("Bricker",
                new Vector2(700, 500),
                cols,
                rows);
        manager.run();
    }
}
