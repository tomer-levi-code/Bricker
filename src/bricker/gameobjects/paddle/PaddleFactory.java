package bricker.gameobjects.paddle;

import bricker.main.BrickerGameManager;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


public class PaddleFactory {

    private static PaddleFactory instance;

    private final BrickerGameManager brickerGameManager;

    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;

    private final Renderable paddleImage;

    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(200, 15);
    public static final int MAIN_PADDLE_COUNTER = -1;

    public PaddleFactory(BrickerGameManager brickerGameManager,
                          Vector2 windowDimensions,
                          UserInputListener inputListener,
                          ImageReader imageReader) {

        this.brickerGameManager = brickerGameManager;
        paddleImage = imageReader.readImage("assets/paddle.png", true);
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;

    }

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
