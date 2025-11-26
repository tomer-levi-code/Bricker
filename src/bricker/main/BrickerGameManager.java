package bricker.main;

import bricker.gameobjects.health_points.HealthPointsPanel;
import bricker.gameobjects.ball.Ball;
import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.ball.BallType;
import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.PaddleFactory;
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

public class BrickerGameManager extends GameManager {

    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final int DEFAULT_BRICK_COLS = 8;
    public final int DEFAULT_HP = 3;

    private UserInputListener inputListener;
    private WindowController windowController;
    public Vector2 windowDimensions;
    private static int rows;
    private static int cols;

    public HealthPointsPanel healthPointsPanel;
    private Ball mainBall;
    public Counter brickCount = new Counter(0);

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int cols, int rows) {
        super(windowTitle, windowDimensions);
        BrickerGameManager.rows = rows;
        BrickerGameManager.cols = cols;
    }

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
        BallFactory ballFactory = BallFactory.getInstance(imageReader, soundReader);
        mainBall = ballFactory.build(BallType.MAIN, windowCenter);

        //Initialize user paddle
        PaddleFactory paddleFactory = new PaddleFactory(this,
                windowDimensions,
                inputListener,
                imageReader);
        Paddle userPaddle = paddleFactory.build(PaddleType.USER);
        userPaddle.reset();

        //Initialize brick grid
        BrickHandler brickHandler = new BrickHandler(this,
                ballFactory,
                paddleFactory,
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

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
        checkForStrike();
        easyWayOutListener();
    }

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

    public void addItem(GameObject item, int layer) {
        gameObjects().addGameObject(item, layer);
    }

    public void removeItem(GameObject item, int layer) {
        gameObjects().removeGameObject(item, layer);
    }

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
