package bricker.brick_strategies;

import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.PaddleFactory;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;

import java.util.Random;

public class DoubleStrategy extends BasicCollisionStrategy{

    private static StrategyFactory strategyFactory;

    public DoubleStrategy(BrickHandler brickHandler,
                          StrategyFactory strategyFactory) {
        super(brickHandler);

        DoubleStrategy.strategyFactory = strategyFactory;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        CollisionStrategy[] collisionStrategies = strategyFactory.generateUnique();
        for(CollisionStrategy collisionStrategy : collisionStrategies){
            collisionStrategy.onCollision(thisObj, otherObj);
        }
    }
}
