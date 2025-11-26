package bricker.brick_strategies;

import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.brick.BrickHandler;
import bricker.main.BrickerGameManager;

import java.util.Random;

public class StrategyFactory {

    private CollisionStrategy[] strategies;
    BrickerGameManager brickerGameManager;
    BallFactory ballFactory;
    BrickHandler brickHandler;
    private static Random random;

    public StrategyFactory(BrickerGameManager brickerGameManager, BrickHandler brickHandler, BallFactory ballFactory) {
        this.brickerGameManager = brickerGameManager;
        this.brickHandler = brickHandler;
        this.ballFactory = ballFactory;
        random = new Random();
        strategies = new CollisionStrategy[]{
                new BasicCollisionStrategy(brickHandler),
                new ExtraPucksStrategy(brickerGameManager, brickHandler, ballFactory),
        };
    }

//    public CollisionStrategy generate() {
//        int rand = random.nextInt(10) + 1;
//        return strategies[Math.max(0, rand - (strategies.length - 1))];
//    }

    public CollisionStrategy generate() {
        int rand = random.nextInt(4) + 1;
        return strategies[Math.max(0, rand - 3)];
    }
}
