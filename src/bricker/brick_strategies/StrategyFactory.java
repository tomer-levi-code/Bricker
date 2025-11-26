package bricker.brick_strategies;

import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.PaddleFactory;
import bricker.main.BrickerGameManager;
import danogl.gui.ImageReader;

import java.util.Random;

public class StrategyFactory {

    private final CollisionStrategy[] strategies;

    private static Random random;

    public StrategyFactory(BrickerGameManager brickerGameManager,
                           BrickHandler brickHandler,
                           BallFactory ballFactory,
                           PaddleFactory paddleFactory,
                           ImageReader imageReader) {
        random = new Random();
        strategies = new CollisionStrategy[]{
                new BasicCollisionStrategy(brickHandler),
                new ExtraPucksStrategy(brickerGameManager,
                        brickHandler,
                        ballFactory),
                new ExtraPaddleStrategy(brickerGameManager,
                        brickHandler,
                        paddleFactory),
                new ExplodingBrickStrategy(brickHandler),
                new HealthBonusStrategy(brickHandler, imageReader),
        };
    }

//    public CollisionStrategy generate() {
//        int rand = random.nextInt(10) + 1;
//        return strategies[Math.max(0, rand - (strategies.length - 1))];
//    }

    public CollisionStrategy generate() {
        int rand = random.nextInt(1);
        return strategies[rand];
    }
}
