package bricker.brick_strategies;

import bricker.gameobjects.brick.Brick;
import bricker.gameobjects.brick.BrickHandler;
import danogl.GameObject;

/**
 * ExplodingBrickStrategy is an extension of BasicCollisionStrategy.
 * In addition to the default behavior, it triggers the destruction of adjacent bricks in the grid.
 * This strategy also has a chain-reaction effect where the collision with one brick
 * causes nearby exploding bricks to also be destroyed.
 */
public class ExplodingBrickStrategy extends BasicCollisionStrategy{

    /**
     * Constructs an ExplodingBrickStrategy.
     *
     * @param brickHandler The BrickHandler used to manage and interact with the grid
     *                     of bricks. Needed to access neighboring bricks and
     *                     update their states during an explosion.
     */
    public ExplodingBrickStrategy(BrickHandler brickHandler) {
        super(brickHandler);
    }

    /**
     * Handles the collision between two game objects.
     * Specifically, we can expect that the first object is of type Brick.
     * This method triggers additional collisions for
     * neighboring bricks in the grid around the collided brick, invoking their collision strategies.
     *
     * @param thisObj The primary game object involved in the collision.
     *                    Expected to be an instance of Brick.
     * @param otherObj The secondary game object involved in the collision.
     *                     Could be any game object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        Brick brick = (Brick) thisObj;
        int col = brick.getCol();
        int row = brick.getRow();

        // top, bottom, right, left.
        int[] rowIndexes = {-1, 1, 0, 0};
        int[] colIndexes = {0, 0, 1, -1};

        for (int i = 0; i < rowIndexes.length; i++) {
            Brick curBrick = brickHandler.getBrick(col + colIndexes[i], row + rowIndexes[i]);
            if(curBrick != null) {
                curBrick.getStrategy().onCollision(curBrick, thisObj);
            }
        }
    }
}