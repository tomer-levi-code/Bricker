package bricker.gameobjects.ball;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class Ball extends GameObject {

    private static final Random rand = new Random();

    private static final float BALL_SPEED = 300;
    private final BallType ballType;
    private final Sound collisionSound;
    private final Vector2 centerStartCoordinates;

    public Ball(BallType ballType, Vector2 topLeftCorner, Vector2 centerStartCoordinates, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.ballType = ballType;
        this.collisionSound = collisionSound;
        this.centerStartCoordinates = centerStartCoordinates;
        reset();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }

    public void reset() {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;

        switch (ballType) {
            case PUCK:
                double angle = rand.nextDouble() * Math.PI;
                ballVelX *= (float) Math.cos(angle);
                ballVelY *= (float) Math.sin(angle);
                break;
            case MAIN:
            default:
                if (rand.nextBoolean()) {
                    ballVelX *= -1;
                }
                if (rand.nextBoolean()) {
                    ballVelY *= -1;
                }
        }

        setCenter(centerStartCoordinates);
        setVelocity(new Vector2(ballVelX, ballVelY));
    }
}
