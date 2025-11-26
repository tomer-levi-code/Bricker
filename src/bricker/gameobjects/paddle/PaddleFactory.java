package bricker.gameobjects.paddle;

import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Vector;

public class PaddleFactory {

    private static PaddleFactory instance;

    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;

    private final Renderable paddleImage;

    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(200, 15);

    private PaddleFactory(Vector2 windowDimensions, UserInputListener inputListener, ImageReader imageReader) {

        paddleImage = imageReader.readImage("assets/paddle.png", true);
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;

    }

    public static PaddleFactory getInstance(Vector2 windowDimensions, UserInputListener inputListener, ImageReader imageReader) {

        if (instance == null) {
            instance = new PaddleFactory(windowDimensions, inputListener, imageReader);
        }
        return instance;

    }

    public Paddle build(PaddleType paddleType) {

        Vector2 startCenter;

        switch (paddleType) {
            case STRATEGY:
                startCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
                break;
            case USER:
            default:
                startCenter = new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30);
        }

        Paddle res = new Paddle(Vector2.ZERO,
                startCenter,
                PADDLE_DIMENSIONS,
                paddleImage,
                inputListener,
                windowDimensions);

        return res;

    }

}
