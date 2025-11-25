package bricker.gameobjects.ball;

import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BallFactory {

    private final Vector2 mainBallDimensions = new Vector2(50f, 50f);
    private final Vector2 puckBallDimensions = new Vector2(37.5f, 37.5f);
    private final Renderable mainBallImage, puckBallImage;
    private final Sound collisionSound;


    public BallFactory(ImageReader imageReader, SoundReader soundReader) {
        mainBallImage = imageReader.readImage("assets/ball.png", true);
        puckBallImage = imageReader.readImage("assets/mockBall.png", true);
        collisionSound = soundReader.readSound("assets/blop.wav");
    }

    public Ball build(BallType ballType) {
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
       return new Ball(Vector2.ZERO,
               ballDimensions,
               ballImage,
               collisionSound);
    }
}
