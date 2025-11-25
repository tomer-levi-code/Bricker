package bricker.brick_strategies;

import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.brick.BrickHandler;
import danogl.GameObject;

public class ExtraPucksStrategy extends BasicCollisionStrategy{

    private BallFactory ballFactory;

    public ExtraPucksStrategy(BrickHandler brickHandler, BallFactory ballFactory) {
        super(brickHandler);

        this.ballFactory = ballFactory;
    }

    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        super.onCollision(firstObject, secondObject);

//        ballFactory.
    }
}
