package bricker.brick_strategies;

import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.PaddleFactory;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DoubleStrategy extends BasicCollisionStrategy {

    private static final int MAX_FEATURED_STRATEGIES = 3;

    private Random rand;
    private final CollisionStrategy[] availableStrategies;

    public DoubleStrategy(BrickerGameManager brickerGameManager,
                          BrickHandler brickHandler,
                          BallFactory ballFactory,
                          PaddleFactory paddleFactory,
                          SoundReader soundReader,
                          ImageReader imageReader) {
        super(brickHandler);

        rand = new Random();
        availableStrategies = new CollisionStrategy[]{
                new ExtraPucksStrategy(brickerGameManager,
                        brickHandler,
                        ballFactory),
                new ExtraPaddleStrategy(brickerGameManager,
                        brickHandler,
                        paddleFactory),
                new ExplodingBrickStrategy(brickHandler, soundReader),
                new HealthBonusStrategy(brickHandler,
                        brickerGameManager,
                        imageReader),
                this
        };
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        ArrayList<CollisionStrategy> strategies = new ArrayList<>();

        //Randomly choose both sub-strategies of the DoubleStrategy.
        while (strategies.size() < 2) {
            int index = rand.nextInt(availableStrategies.length);
            strategies.add(availableStrategies[index]);
        }

        //For each choice of DoubleStrategy, replace it with two actual
        // strategies if not exceeding the MAX_FEATURED_STRATEGIES.
        int doubleStrategyCounter = 0;
        while (strategies.contains(this)) {
            strategies.remove(this);
            doubleStrategyCounter++;
        }
        for (int i = 0; i < doubleStrategyCounter; i++) {
                int curSize = strategies.size();
                while (strategies.size() < MAX_FEATURED_STRATEGIES &&
                        strategies.size() < curSize + 2) {
                    int index = rand.nextInt(availableStrategies.length - 1);
                    strategies.add(availableStrategies[index]);
                }
        }

        //After choosing strategies, we activate them.
        for (CollisionStrategy strategy : strategies) {
            strategy.onCollision(thisObj, otherObj);
        }
    }
}
