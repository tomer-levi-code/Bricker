package bricker.main;

import bricker.gameobjects.HealthPointsPanel;
import bricker.gameobjects.ball.Ball;
import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.ball.BallType;
import bricker.gameobjects.brick.BrickHandler;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.Paddle;

import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final float BALL_SPEED = 300;
    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final int DEFAULT_BRICK_COLS = 8;
    public final int DEFAULT_HP = 3;

    private UserInputListener inputListener;
    private WindowController windowController;
    public Vector2 windowDimensions;
    private Vector2 windowCenter;
    private static int rows;
    private static int cols;

    private HealthPointsPanel healthPointsPanel;
    private Ball mainBall;
    public Counter brickCount = new Counter(0);

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int cols, int rows) {
        super(windowTitle, windowDimensions);
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.inputListener = inputListener;

        //window parameters.
        windowController.setTargetFramerate(80);
        windowDimensions = windowController.getWindowDimensions();
        windowCenter = windowDimensions.mult(0.5f);

        //Initialize background
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", true);
        GameObject background = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()), backgroundImage);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

        //Initialize brick grid
        BrickHandler brickHandler = new BrickHandler(this, imageReader);
        brickHandler.initBrickGrid(windowDimensions, cols, rows);

        //Initialize HP Panel
        healthPointsPanel = new HealthPointsPanel(this,
                windowDimensions,
                imageReader);
        healthPointsPanel.initHP(windowDimensions);

        //Initialize ball
        BallFactory ballFactory = new BallFactory(imageReader, soundReader);
        mainBall = ballFactory.build(BallType.MAIN);
        resetBall(mainBall);
        gameObjects().addGameObject(mainBall);


        //Initialize user paddle
        Renderable paddleImage =
                imageReader.readImage("assets/paddle.png", true);
        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(200, 15), paddleImage, inputListener, windowDimensions);
        userPaddle.setCenter(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30));


        gameObjects().addGameObject(userPaddle);

        createWalls(windowDimensions);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
        strike();
        easyWayOutListener();
    }

    private void easyWayOutListener() {
        if(inputListener.isKeyPressed(KeyEvent.VK_W)) {
            brickCount.reset();
        }
    }

    private void checkForGameEnd() {

        String prompt = "";
        if (brickCount.value() == 0) {
            prompt = "You win!";
        }
        if (healthPointsPanel.getHP() == 0) {
            prompt = "You lose!";
        }
        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt)) {
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
        gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(topWall, Layer.STATIC_OBJECTS);
    }

    public void addItem(GameObject item, int layer) {
        gameObjects().addGameObject(item, layer);
    }

    public void removeItem(GameObject item, int layer) {
        gameObjects().removeGameObject(item, layer);
    }

    private void strike() {
        double ballHeight = mainBall.getCenter().y();

        if (ballHeight > windowDimensions.y()) {
            healthPointsPanel.decreaseHP();
            resetBall(mainBall);
        }
    }

    public void resetBall(Ball ball){
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }

        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setCenter(windowCenter);
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
