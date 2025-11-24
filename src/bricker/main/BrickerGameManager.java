package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.StrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.Brick;
import gameobjects.Paddle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final float BALL_SPEED = 300;
    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final int DEFAULT_BRICK_COLS = 8;
    private static int rows;
    private static int cols;
    private GameObject ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private static int brickCount = DEFAULT_BRICK_ROWS * DEFAULT_BRICK_COLS;
    private static int DEFAULT_HP = 3;
    private int healthPoints;
    private static Queue<GameObject> HPQueue;
    private Vector2 windowCenter;
    private TextRenderable textRenderable;
    private UserInputListener inputListener;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int cols, int rows) {
        super(windowTitle, windowDimensions);
        this.rows = rows;
        this.cols = cols;
        HPQueue = new LinkedList<>();
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

        //assets.
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        Renderable ballImage =
                imageReader.readImage("assets/ball.png", true);
        Renderable paddleImage =
                imageReader.readImage("assets/paddle.png", true);
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", true);
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);

        GameObject background = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()), backgroundImage);

        gameObjects().addGameObject(background, Layer.BACKGROUND);

        //creating ball.
        ball = new Ball(Vector2.ZERO, new Vector2(50, 50), ballImage, collisionSound);
        startBall();

        gameObjects().addGameObject(ball);


        //create user paddle
        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(200, 15), paddleImage, inputListener, windowDimensions);
        userPaddle.setCenter(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30));


        gameObjects().addGameObject(userPaddle);

        createWalls(windowDimensions);

        createBrickGrid(windowDimensions, cols, rows, brickImage);

        initHP(DEFAULT_HP, windowDimensions, heartImage);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
        decreaseHP();
        easyWayOutListener();
    }

    private void easyWayOutListener() {
        if(inputListener.isKeyPressed(KeyEvent.VK_W)) {
            brickCount = 0;
        }
    }

    private void checkForGameEnd() {

        String prompt = "";
        if (brickCount == 0) {
            prompt = "You win!";
        }
        if (HPQueue.isEmpty()) {
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

    private void createBrickGrid(Vector2 windowDimensions, int cols, int rows, Renderable brickImage) {
        final float EDGE_BUFFER = 17f;
        final float BRICK_BUFFER = 3f;
        float brickWidth = ((windowDimensions.x() - (2 * EDGE_BUFFER)) / cols) - (2 * BRICK_BUFFER);
        brickCount = rows * cols;

        StrategyFactory factory = new StrategyFactory();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float topLeftX = EDGE_BUFFER + (col * (brickWidth + (2 * BRICK_BUFFER))) + BRICK_BUFFER;
                float topLeftY = EDGE_BUFFER + (row * (Brick.HEIGHT_IN_PX + (2 * BRICK_BUFFER))) + BRICK_BUFFER;
                CollisionStrategy[] strategies = new CollisionStrategy[]{
                        new BasicCollisionStrategy(this)
                };
                CollisionStrategy strategy = factory.generate(strategies);
                GameObject brick = new Brick(
                        new Vector2(topLeftX, topLeftY),
                        brickWidth,
                        brickImage,
                        strategy);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    public void removeBrick(GameObject brick) {
        gameObjects().removeGameObject(brick, Layer.STATIC_OBJECTS);
        brickCount--;
    }

    private void initHearts(int HP,
                            Vector2 windowDimensions,
                            Renderable renderable,
                            float edgeLength,
                            float edgeBuffer,
                            float heartBuffer) {
        for (int i = HP - 1; i >= 0; i--) {
            float topLeftX = edgeBuffer + (i * (edgeLength + (2 * heartBuffer))) + heartBuffer;
            float topLeftY = windowDimensions.y() - (edgeBuffer + edgeLength);
            GameObject heart = new GameObject(new Vector2(topLeftX, topLeftY),
                    new Vector2(edgeLength, edgeLength),
                    renderable);
            HPQueue.add(heart);
            gameObjects().addGameObject(heart, Layer.UI);
        }
    }

    private void initNumericHP(int HP,
                               Vector2 windowDimensions,
                               float edgeLength,
                               float edgeBuffer,
                               float stringBuffer) {
        String stringHP = Integer.toString(HP);
        textRenderable = new TextRenderable(stringHP);
        textRenderable.setColor(Color.decode("#24c538"));
        GameObject numericHP = new GameObject(
                new Vector2(edgeBuffer + stringBuffer,
                        windowDimensions.y() - (2 * (edgeBuffer + edgeLength))),
                new Vector2(edgeLength,
                        edgeLength),
                textRenderable);
        gameObjects().addGameObject(numericHP, Layer.UI);
    }

    private void initHP(int HP, Vector2 windowDimensions, Renderable renderable) {
        this.healthPoints = HP;
        final float EDGE_BUFFER = 17f;
        final float OBJECT_BUFFER = 3f;
        float maxHeartWidth = ((windowDimensions.x() - (2 * EDGE_BUFFER)) / HP) - (2 * OBJECT_BUFFER);
        float edgeLength = Math.min(windowDimensions.y() / 20f, maxHeartWidth);

        initHearts(HP, windowDimensions, renderable, edgeLength, EDGE_BUFFER, OBJECT_BUFFER);
        initNumericHP(HP, windowDimensions, edgeLength, EDGE_BUFFER, OBJECT_BUFFER);
    }

    private void updateNumericHP() {
        String stringHP = Integer.toString(healthPoints);
        textRenderable.setString(stringHP);
        switch (healthPoints)
        {
            case 2:
                textRenderable.setColor(Color.decode("#dcea24"));
                break;
            case 1:
            case 0:
                textRenderable.setColor(Color.decode("#fa000a"));
                break;
            case 3:
            default:
                textRenderable.setColor(Color.decode("#24c538"));
        }
    }

    private void decreaseHP() {
        double ballHeight = ball.getCenter().y();

        if (ballHeight > windowDimensions.y()) {
            gameObjects().removeGameObject(HPQueue.remove(), Layer.UI);
            healthPoints --;
            updateNumericHP();
            startBall();
        }
    }

    public void startBall(){
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
        GameManager manager = new BrickerGameManager("Bouncing Ball",
                new Vector2(700, 500),
                cols,
                rows);
        manager.run();
    }
}
