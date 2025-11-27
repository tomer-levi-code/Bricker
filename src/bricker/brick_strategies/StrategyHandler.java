package bricker.brick_strategies;

import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.PaddleHandler;
import bricker.main.BrickerGameManager;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;

import java.util.Random;

public class StrategyHandler {

    private static CollisionStrategy[] strategies;

    private static Random random;


    public StrategyHandler(BrickerGameManager brickerGameManager,
                           BrickHandler brickHandler,
                           BallFactory ballFactory,
                           PaddleHandler paddleHandler,
                           SoundReader soundReader,
                           ImageReader imageReader) {
        random = new Random();
        strategies = new CollisionStrategy[]{
                new BasicCollisionStrategy(brickHandler),
                new ExtraPucksStrategy(brickerGameManager,
                        brickHandler,
                        ballFactory),
                new ExtraPaddleStrategy(brickerGameManager,
                        brickHandler,
                        paddleHandler),
                new ExplodingBrickStrategy(brickHandler, soundReader),
                new HealthBonusStrategy(brickHandler,
                        brickerGameManager,
                        imageReader),
                new DoubleStrategy(brickerGameManager,
                        brickHandler,
                        ballFactory,
                        paddleHandler,
                        soundReader,
                        imageReader)
        };
    }

    public CollisionStrategy generate() {
        //Generate a random index in the range (0,5) as following:
        // * Generate a random integer in the range (1,10)
        // * Substruct 5 from the chosen integer
        // * If the result is less then or equal to 0, it means the original chosen integer
        //   was in the range (1-5), and the chosen index is 0
        // * Otherwise, the chosen index is the index we've got after the substraction.
        //This way, the final index is 0 half (5 out of 10) of the times,
        // and any other integer one out of 10 times, as the instructions
        // required.
        int rand = random.nextInt(10) + 1;
        return strategies[Math.max(0, rand - (strategies.length - 1))];
    }

}
