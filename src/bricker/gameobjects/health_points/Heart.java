package bricker.gameobjects.health_points;

import bricker.gameobjects.paddle.PaddleHandler;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Heart class represents a game object that appears both in the heart panel
 * to display player health and as a collectible item in the game that can interact
 * with the paddle to affect the player's health points. It extends the GameObject
 * class and adds behavior particular to the game dynamics such as being removed
 * when it exits the screen or upon collision with the paddle.
 */
public class Heart extends GameObject {

    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new Heart object, which can serve two purposes:
     * 1. As a display item in the health points panel to show current player health
     * 2. As a collectible item in the game that can interact with the paddle to increase health points
     * When used as a collectible, the Heart's behavior includes being removed when it exits the screen
     * or upon collision with the paddle.
     *
     * @param brickerGameManager the game manager instance managing the game logic and objects
     * @param topLeftCorner      the top-left corner of the Heart object when instantiated
     * @param dimensions         the width and height of the Heart object
     * @param renderable         the renderable defining the visual appearance of the Heart object
     */
    public Heart(BrickerGameManager brickerGameManager,
                 Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);

        this.brickerGameManager = brickerGameManager;
    }

    private void terminate() {
        brickerGameManager.removeItem(this, Layer.DEFAULT);
    }

    private void terminateIfReachedBottom() {
        if(BrickerGameManager.windowDimensions.y() < getCenter().y()) {
            terminate();
        }
    }

    /**
     * Determines if the current object should collide with the specified object.
     *
     * @param other The GameObject to check for collision eligibility. This object is compared
     *              based on its tag to determine if it represents the main paddle.
     * @return True if the specified object has a tag equal to MAIN_PADDLE_TAG, indicating
     *         it is the main paddle; false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(PaddleHandler.MAIN_PADDLE_TAG);
    }

    /**
     * Updates the state of the Heart object. Checks if the object has moved beyond
     * the bottom boundary of the game window and terminates the instance if so.
     *
     * @param deltaTime The elapsed time (in seconds) since the last frame update.
     *                  This parameter allows the method to account for time-based
     *                  updates on the object's state.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        terminateIfReachedBottom();
    }

    /**
     * Handles the collision event when the Heart object collides with another game object.
     * Increases the player's health points and removes the Heart object from the game.
     *
     * @param other The GameObject that this Heart collides with. This object is used
     *              to determine the interaction behavior upon collision.
     * @param collision The Collision object containing details about the collision,
     *                  including the collision direction and contact point.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        brickerGameManager.increaseHP();
        terminate();
    }
}
