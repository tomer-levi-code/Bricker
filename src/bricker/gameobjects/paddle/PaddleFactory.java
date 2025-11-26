package bricker.gameobjects.paddle;

import bricker.main.BrickerGameManager;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * A factory class responsible for constructing instances of the {@code Paddle} object in the game.
 * The {@code PaddleFactory} is made to create paddles
 * of different types with specific configurations based on the game.
 */
public class PaddleFactory {

    private static PaddleFactory instance;

    private final BrickerGameManager brickerGameManager;

    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;

    private final Renderable paddleImage;

    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(200, 15);
    public static final int MAIN_PADDLE_COUNTER = -1;
    public static final String MAIN_PADDLE_TAG = "PADDLE";

    /**
     * Constructs a new paddleFactory instance to create paddle objects for the game.
     *
     * @param brickerGameManager the game manager responsible for handling the game's core functionality
     * @param windowDimensions the dimensions of the game window
     * @param inputListener the listener capturing user input events
     * @param imageReader the utility responsible for reading and loading images
     */
    public PaddleFactory(BrickerGameManager brickerGameManager,
                          Vector2 windowDimensions,
                          UserInputListener inputListener,
                          ImageReader imageReader) {

        this.brickerGameManager = brickerGameManager;
        paddleImage = imageReader.readImage("assets/paddle.png", true);
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;

    }

    /**
     * Builds a new Paddle object of the specified PaddleType.
     * The paddle's starting position, collision counter, and other properties
     * are determined based on the given PaddleType (USER or STRATEGY).
     *
     * @param paddleType the type of paddle to be created. It determines the paddle's
     *                   starting position and behavior. Acceptable values are:
     *                   - PaddleType.USER: A player-controlled paddle initialized at the
     *                     bottom of the game window for the entire duration of the game.
     *                   - PaddleType.STRATEGY: A player-controlled paddle initialized
     *                     at the center of the game window with a specific life duration
     *                     based on collisions.
     * @return a new Paddle instance constructed with the specified type's configurations.
     */
    public Paddle build(PaddleType paddleType) {

        Vector2 startCenter;
        int paddleCounter;

        switch (paddleType) {
            case STRATEGY:
                startCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
                paddleCounter = 0;
                break;
            case USER:
            default:
                startCenter = new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30);
                paddleCounter = MAIN_PADDLE_COUNTER;
        }

        return new Paddle(Vector2.ZERO,
                startCenter,
                PADDLE_DIMENSIONS,
                paddleCounter,
                brickerGameManager,
                paddleImage,
                inputListener,
                windowDimensions);

    }

}
