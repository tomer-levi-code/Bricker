package bricker.brick_strategies;

import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.Paddle;
import bricker.gameobjects.paddle.PaddleFactory;
import bricker.gameobjects.paddle.PaddleType;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

/**
 * ExtraPaddleStrategy is an extension of BasicCollisionStrategy.
 * In addition to the default behavior, it adds another paddle
 * to the center of the screen once a collision occurs.
 * The newly added paddle is built using the PaddleFactory,
 * and the game state is updated using the BrickerGameManager.
 */
public class ExtraPaddleStrategy extends BasicCollisionStrategy {

    private static BrickerGameManager brickerGameManager;
    private static PaddleFactory paddleFactory;
    private static Paddle extraPaddle;

    /**
     * Constructs an ExtraPaddleStrategy.
     *
     * @param brickerGameManager the game manager responsible for overall game state and behavior.
     * @param brickHandler the handler responsible for managing brick-related actions.
     * @param paddleFactory the factory used to create paddle instances.
     */
    public ExtraPaddleStrategy(BrickerGameManager brickerGameManager,
                               BrickHandler brickHandler,
                               PaddleFactory paddleFactory) {

        super(brickHandler);

        ExtraPaddleStrategy.brickerGameManager = brickerGameManager;
        ExtraPaddleStrategy.paddleFactory = paddleFactory;

    }

    /**
     * Handles the collision between two game objects.
     * Specifically, we can expect that the first object is of type Brick.
     * If an extra paddle has not already been created, this method creates an additional paddle
     * at the center of the screen using the brickerGameManager and the paddleFactory.
     * Additionally, the paddle is reset to its default state upon creation.
     * @param thisObj The primary game object involved in the collision.
     *                    Expected to be an instance of Brick.
     * @param otherObj The secondary game object involved in the collision.
     *                     Could be any game object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {

        super.onCollision(thisObj, otherObj);

        if(extraPaddle == null) {

            extraPaddle = paddleFactory.build(PaddleType.STRATEGY);
            brickerGameManager.addItem(extraPaddle, Layer.DEFAULT);
            extraPaddle.reset();

        }

    }

}
