package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.brick.BrickHandler;
import danogl.GameObject;

/**
 * BasicCollisionStrategy is a basic implementation of the CollisionStrategy interface.
 * It defines the behavior that occurs when a collision involves bricks in the game.
 */

public class BasicCollisionStrategy implements CollisionStrategy {

    /**
     * An instance of the game's BrickHandler, needed to handle removing the hit
     * brick from the screen.
     */
    protected final BrickHandler brickHandler;

    /**
     * Constructs a BasicCollisionStrategy with the given BrickHandler.
     * This strategy defines the basic behavior for handling collisions involving bricks.
     *
     * @param brickHandler The BrickHandler used to manage brick-related operations.
     */
    public BasicCollisionStrategy(BrickHandler brickHandler) {
        this.brickHandler = brickHandler;
    }

    /**
     * Handles the collision of a game object with another object in the game.
     * We fully expect the object involved in the collision is a brick. This method destroys the brick
     * by downcasting and giving the operation to the BrickHandler instance, based on the brick's row
     * and column position in the game grid.
     *
     * @param thisObj The current game object involved in the collision, expected to be a Brick.
     *                If null, no action is performed.
     * @param otherObj The other game object involved in the collision.
     *                 This parameter is unused in the method since the collision affects only the brick.
     *                 If null, no action is performed.
     */
    @Override
    public void onCollision(GameObject thisObj,GameObject otherObj) {

        if(thisObj == null || otherObj == null) {
            return;
        }

        Brick brick = (Brick) thisObj;
        brickHandler.destroyBrick(brick.getCol(), brick.getRow());
    }
}
