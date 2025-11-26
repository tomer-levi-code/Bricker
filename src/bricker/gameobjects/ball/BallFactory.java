package bricker.gameobjects.ball;

import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BallFactory {

    private static BallFactory instance;

    private static final Vector2 mainBallDimensions = new Vector2(50f, 50f);
    private static final Vector2 puckBallDimensions = new Vector2(37.5f, 37.5f);
    private static Renderable mainBallImage;
    private static Renderable puckBallImage;
    private static Sound collisionSound;


    private BallFactory(ImageReader imageReader, SoundReader soundReader) {
        mainBallImage = imageReader.readImage("assets/ball.png", true);
        puckBallImage = imageReader.readImage("assets/mockBall.png", true);
        collisionSound = soundReader.readSound("assets/blop.wav");
    }

    public static BallFactory getInstance(ImageReader imageReader, SoundReader soundReader) {
        if (instance == null) {
            instance = new BallFactory(imageReader, soundReader);
        }
        return instance;
    }

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
       return new Ball(ballType,
               Vector2.ZERO,
               centerStartCoordinates,
               ballDimensions,
               ballImage,
               collisionSound);
    }
}
