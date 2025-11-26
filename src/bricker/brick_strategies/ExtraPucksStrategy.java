package bricker.brick_strategies;

import bricker.gameobjects.ball.Ball;
import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.ball.BallType;
import bricker.gameobjects.brick.BrickHandler;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;


/**
 * ExtraPucksStrategy is an extension of BasicCollisionStrategy.
 * In addition to the default behavior, it generates two additional pucks in the game
 * upon brick collision. These pucks are added to the game at the position of the
 * destroyed brick and behave like smaller additional balls in the game.
 */
public class ExtraPucksStrategy extends BasicCollisionStrategy {

    private final BrickerGameManager brickerGameManager;
    private final BallFactory ballFactory;

    /**
     * Constructs an ExtraPucksStrategy instance.
     *
     * @param brickerGameManager the game manager that handles the overall game logic,
     *                           including managing objects like the pucks.
     * @param brickHandler       the handler responsible for managing the bricks in the game.
     * @param ballFactory        the factory used to create new ball objects (including extra pucks).
     */
    public ExtraPucksStrategy(BrickerGameManager brickerGameManager,
                              BrickHandler brickHandler,
                              BallFactory ballFactory) {
        super(brickHandler);

        this.brickerGameManager = brickerGameManager;
        this.ballFactory = ballFactory;
    }

    /**
     * Handles the collision between two game objects.
     * Specifically, we can expect that the first object is of type Brick.
     * When a collision occurs, this method creates two additional puck balls
     * at the position of the collided brick and adds them to the game's default layer.
     * @param thisObj The primary game object involved in the collision.
     *                Expected to be an instance of Brick.
     * @param otherObj The secondary game object involved in the collision.
     *                 Could be any game object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        Ball firstPuck = ballFactory.build(BallType.PUCK, thisObj.getCenter());
        Ball secondPuck = ballFactory.build(BallType.PUCK, thisObj.getCenter());

        brickerGameManager.addItem(firstPuck, Layer.DEFAULT);
        brickerGameManager.addItem(secondPuck, Layer.DEFAULT);
    }
}
