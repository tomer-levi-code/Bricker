package bricker.gameobjects;

import bricker.gameobjects.paddle.PaddleHandler;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The Paddle class represents a paddle in the game, which the player
 * can control to interact with other game objects such as the ball.
 * The paddle responds to user input for left and right movement
 * and can handle collisions with other game objects.
 * The paddle's behavior includes:
 * - Moving horizontally based on user input within the game window boundaries.
 * - Resetting to its initial position when required.
 * - Handling collisions with other objects and triggering specific game logic,
 *   such as removing the paddle after reaching a collision limit or increasing
 *   health points upon interaction with certain objects.
 */
public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private static final int COLLISIONS_TO_KILL = 4;
    private final Vector2 CENTER_START_COORDINATES;

    private final BrickerGameManager brickerGameManager;
    private final UserInputListener inputListener;
    private static Vector2 windowDimensions;

    private int collisionCounter;


    /**
     * Constructs a Paddle instance. The paddle can be used to deflect game
     * balls and interact within the Bricker game environment.
     *
     * @param topLeftCorner The initial top-left corner coordinates of the paddle.
     * @param CENTER_START_COORDINATES The default center start coordinates for the paddle,
     *                                 used for resetting its position.
     * @param dimensions The dimensions (width and height) of the paddle.
     * @param collisionCounter The number of collisions the paddle is allowed
     *                         before it's removed in strategy-based paddles.
     * @param brickerGameManager The game manager instance that controls the game logic
     *                           and handles interactions with the paddle.
     * @param renderable The renderable object used to visually represent the paddle.
     * @param inputListener The listener responsible for capturing player input and controlling
     *                      paddle movement during gameplay.
     * @param windowDimensions The dimensions of the game window, used for boundary constraints
     *                         and position handling.
     */
    public Paddle(Vector2 topLeftCorner,
                  Vector2 CENTER_START_COORDINATES,
                  Vector2 dimensions,
                  int  collisionCounter,
                  BrickerGameManager brickerGameManager,
                  Renderable renderable,
                  UserInputListener inputListener,
                  Vector2 windowDimensions) {

        super(topLeftCorner, dimensions, renderable);

        this.brickerGameManager = brickerGameManager;
        this.inputListener = inputListener;
        Paddle.windowDimensions = windowDimensions;

        this.CENTER_START_COORDINATES = CENTER_START_COORDINATES;

        this.collisionCounter = collisionCounter;

        if(this.collisionCounter == PaddleHandler.MAIN_PADDLE_COUNTER) {
            setTag(PaddleHandler.MAIN_PADDLE_TAG);
        }

        setCenter(CENTER_START_COORDINATES);

    }

    /**
     * Handles the movement of the Paddle.
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        Vector2 topLeft = getTopLeftCorner();
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && topLeft.x() > 0) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)
                && topLeft.x() + getDimensions().x() < windowDimensions.x()) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));

    }

    /**
     * Handles the collision event when the Paddle interacts with another game object.
     * This method defines specific behaviors based on the type of collision.
     *
     * If the collision counter matches the specific threshold for paddle removal,
     * the paddle is removed from the game. Additionally, if the paddle interacts
     * with a specified "heart bonus" object, the player's health points are increased.
     *
     * @param other The GameObject that this paddle collided with.
     * @param collision The Collision object containing details about the collision event.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {

        super.onCollisionEnter(other, collision);

        if(collisionCounter != PaddleHandler.MAIN_PADDLE_COUNTER) {
            collisionCounter++;
            if(collisionCounter == COLLISIONS_TO_KILL) {
                brickerGameManager.removeItem(this, Layer.DEFAULT);
            }
        }

    }
}
