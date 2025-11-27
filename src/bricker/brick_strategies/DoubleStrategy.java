package bricker.brick_strategies;

import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.PaddleHandler;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;

import java.util.ArrayList;
import java.util.Random;

public class DoubleStrategy extends BasicCollisionStrategy {

    private static final int MAX_FEATURED_STRATEGIES = 3;

    private Random rand;
    private final CollisionStrategy[] availableStrategies;

    public DoubleStrategy(BrickerGameManager brickerGameManager,
                          BrickHandler brickHandler,
                          BallFactory ballFactory,
                          PaddleHandler paddleHandler,
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
                        paddleHandler),
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

        //Count how many times we chose DoubleStrategy, and remove it
        // from the ArrayList in order to later replace it with (at most)
        // two actual strategies.
        int doubleStrategyCounter = 0;
        while (strategies.contains(this)) {
            strategies.remove(this);
            doubleStrategyCounter++;
        }

        //For each DoubleStrategy chosen before (and then counted and
        // removed), add to the ArrayList (at most) two actual strategies,
        // without exceeding the MAX_FEATURED_STRATEGIES.
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
