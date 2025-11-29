package bricker.gameobjects.ball;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A factory class for creating instances of Ball objects. This class
 * implements the Singleton design pattern to ensure that only one instance of
 * BallFactory exists throughout the game. The factory is responsible for providing
 * pre-configured Ball objects based on the specified BallType.
 * The BallFactory encapsulates the setup of ball characteristics such as
 * dimensions, renderable images, and collision sounds. Ball objects created
 * by this factory are initialized with these shared resources.
 */
public class BallFactory {

    private static BallFactory instance;

    private static BrickerGameManager brickerGameManager;

    private static final Vector2 mainBallDimensions = new Vector2(50f, 50f);
    private static final Vector2 puckBallDimensions = new Vector2(mainBallDimensions.x() * 3 / 4,
            mainBallDimensions.y() * 3 / 4);
    private static Renderable mainBallImage;
    private static Renderable puckBallImage;
    private static Sound collisionSound;


    private BallFactory(BrickerGameManager brickerGameManager,
                        ImageReader imageReader,
                        SoundReader soundReader) {
        this.brickerGameManager = brickerGameManager;
        mainBallImage = imageReader.readImage("assets/ball.png", true);
        puckBallImage = imageReader.readImage("assets/mockBall.png", true);
        collisionSound = soundReader.readSound("assets/blop.wav");
    }

    /**
     * Retrieves the single instance of the BallFactory class. This method ensures that
     * the BallFactory follows the Singleton pattern, creating the instance only if it
     * does not already exist.
     *
     * @param imageReader A utility for reading image files, used to initialize renderable
     *                    resources for the factory.
     * @param soundReader A utility for reading sound files, used to initialize sound
     *                    resources for the factory.
     * @return The single instance of the BallFactory class.
     */
    public static BallFactory getInstance(BrickerGameManager brickerGameManager,
                                          ImageReader imageReader,
                                          SoundReader soundReader) {
        if (instance == null) {
            instance = new BallFactory(brickerGameManager,
                    imageReader,
                    soundReader);
        }
        return instance;
    }

    /**
     * Creates a new instance of a Ball object with pre-defined characteristics based
     * on the specified ball type and starting center coordinates.
     *
     * @param ballType               The type of ball to create, determining its dimensions and visual
     *                               representation as defined in BallType.
     * @param centerStartCoordinates The initial center of the ball coordinates: where the ball will
     *                               spawn in the game world.
     * @return A new Ball instance configured with the specified type and starting position.
     */
    public static Ball build(BallType ballType, Vector2 centerStartCoordinates) {
        Vector2 ballDimensions;
        Renderable ballImage;
        switch (ballType) {
            case PUCK:
                ballDimensions = puckBallDimensions;
                ballImage = puckBallImage;
                break;
            case MAIN:
            default:
                ballDimensions = mainBallDimensions;
                ballImage = mainBallImage;
        }
        return new Ball(brickerGameManager,
                ballType,
                Vector2.ZERO,
                centerStartCoordinates,
                ballDimensions,
                ballImage,
                collisionSound);
    }
}
