package bricker.brick_strategies;

import bricker.gameobjects.ball.Ball;
import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.ball.BallType;
import bricker.gameobjects.brick.BrickHandler;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class ExtraPucksStrategy extends BasicCollisionStrategy {

    private final BrickerGameManager brickerGameManager;
    private final BallFactory ballFactory;

    public ExtraPucksStrategy(BrickerGameManager brickerGameManager, BrickHandler brickHandler, BallFactory ballFactory) {
        super(brickHandler);

        this.brickerGameManager = brickerGameManager;
        this.ballFactory = ballFactory;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        Ball firstPuck = ballFactory.build(BallType.PUCK, thisObj.getCenter());
        Ball secondPuck = ballFactory.build(BallType.PUCK, thisObj.getCenter());

        brickerGameManager.addItem(firstPuck, Layer.DEFAULT);
        brickerGameManager.addItem(secondPuck, Layer.DEFAULT);
    }
}
