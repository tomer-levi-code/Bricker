package bricker.gameobjects.ball;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Represents a ball in the game, which is a type of game object. The Ball class
 * handles its type, behavior upon collision, and reset logic.
 * The Ball objects have predefined characteristics including:
 * - Initial direction of movement.
 * - Collision handling that reverses the ball's direction upon contact.
 * - Reset behavior to reposition and reset the speed of the ball.
 * The BALL_SPEED constant defines the base speed of the ball.
 * Based on the BallType, the behavior of ball velocity differs:
 * - MAIN: Allows random velocity directions on the diagonals.
 * - PUCK: Assigns a random angle within a semicircular arc.
 */
public class Ball extends GameObject {

    private BrickerGameManager brickerGameManager;

    private static final Random rand = new Random();

    private static final float BALL_SPEED = 300;
    private final BallType ballType;
    private final Sound collisionSound;
    private final Vector2 centerStartCoordinates;

    /**
     * Constructs a new Ball instance with the specified parameters.
     * The Ball is a type of game object that is defined by
     * its type, position, initial direction, dimensions, visual appearance, and collision behavior.
     * The ball initializes in a default reset state upon creation.
     *
     * @param ballType The type of the ball, defined by the BallType enum, which determines
     *                 its behavior and attributes (like MAIN or PUCK).
     * @param topLeftCorner The top-left corner coordinates of the ball's bounding rectangle,
     *                      used to position the ball in the game world.
     * @param centerStartCoordinates The initial center coordinates of the ball in the game
     *                                world. The ball will be reset to this position when required.
     * @param dimensions The width and height of the ball, determining its size in the game world.
     * @param renderable The renderable that defines the ball's visual appearance.
     * @param collisionSound The sound effect to be played when the ball collides with
     *                       another object.
     */
    public Ball(BrickerGameManager brickerGameManager,
            BallType ballType,
                Vector2 topLeftCorner,
                Vector2 centerStartCoordinates,
                Vector2 dimensions,
                Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner,
                dimensions,
                renderable);
        this.brickerGameManager = brickerGameManager;
        this.ballType = ballType;
        this.collisionSound = collisionSound;
        this.centerStartCoordinates = centerStartCoordinates;
        reset();
    }

    /**
     * Handles logic when the ball collides with another game object.
     * Upon collision, the ball's velocity is reversed, creating a 'bounce' effect.
     * Additionally, a collision sound is played to provide feedback for the event.
     *
     * @param other The game object that this ball has collided with.
     * @param collision The collision information, including details such as the collision normal
     *                  used to calculate the new velocity of the ball.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(ballType == BallType.PUCK) {
            terminateIfReachedBottom();
        }
    }

    /**
     * Resets the ball to its initial state.
     * MAIN - one of the four diagonals is chosen at random.
     * PUCK - a random angle within a semicircular arc is chosen.
     */
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

    private void terminate() {
        brickerGameManager.removeItem(this, Layer.DEFAULT);
    }

    private void terminateIfReachedBottom() {
        if(BrickerGameManager.windowDimensions.y() < getCenter().y()) {
            terminate();
        }
    }
}
