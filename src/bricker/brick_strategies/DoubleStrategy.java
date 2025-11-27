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

/**
 * DoubleStrategy is an extension of BasicCollisionStrategy.
 * In addition to the default behavior, it randomly selects and executes up to two
 * different collision strategies upon brick collision:
 * - ExtraPucksStrategy: Generates additional pucks in the game
 * - ExtraPaddleStrategy: Creates an extra paddle
 * - ExplodingBrickStrategy: Triggers a brick explosion
 * - HealthBonusStrategy: Provides additional health points
 * - DoubleStrategy itself: Applies more strategies
 * When DoubleStrategy itself is selected as one of the strategies, it is replaced
 * with up to two new randomly chosen strategies, while ensuring the total number
 * of applied strategies never exceeds three (MAX_FEATURED_STRATEGIES).
 */
public class DoubleStrategy extends BasicCollisionStrategy {

    private static final int MAX_FEATURED_STRATEGIES = 3;

    private Random rand;
    private final CollisionStrategy[] availableStrategies;

    /**
     * Constructs a DoubleStrategy instance with a set of collision strategies
     * for modifying gameplay behavior upon brick collisions.
     *
     * @param brickerGameManager the game manager responsible for managing the game state and logic
     * @param brickHandler       the handler responsible for managing bricks in the game
     * @param ballFactory        the factory for creating and initializing ball instances
     * @param paddleHandler      the handler responsible for managing paddles in the game
     * @param soundReader        the reader for managing and playing sound effects
     * @param imageReader        the reader for managing and rendering images
     */
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

    /**
     * Handles the collision between the current game object and another game object
     * by applying a combination of collision strategies.
     * This method selects up to two random sub-strategies from the set of available strategies
     * to define the behavior during a collision.
     * If the current strategy is selected, it is replaced by additional strategies.
     *
     * @param thisObj The current game object involved in the collision, expected to be a brick.
     *                If null, no action is performed.
     * @param otherObj The other game object involved in the collision.
     *                 If null, no action is performed.
     */
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
