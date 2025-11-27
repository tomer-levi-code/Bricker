package bricker.brick_strategies;

import bricker.gameobjects.ball.BallFactory;
import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.PaddleFactory;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;

import java.util.ArrayList;
import java.util.Random;

public class DoubleStrategy extends BasicCollisionStrategy{

    private Random rand;
    private CollisionStrategy[] availableStrategies;
    private ArrayList<CollisionStrategy> strategies;

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

        strategies = new ArrayList<>();

        while(strategies.size() < 2) {
            int index =  rand.nextInt(availableStrategies.length);
            //Prevent choosing DoubleStrategy twice.
            if(index != 4 || !strategies.contains(availableStrategies[index])) {
                strategies.add(availableStrategies[index]);
            }
        }

        //If one of our choices was DoubleStrategy, replace it with two actual
        // strategies.
        if(strategies.contains(availableStrategies[4])) {
            strategies.remove(availableStrategies[4]);
            while(strategies.size() < 3) {
                int index =  rand.nextInt(availableStrategies.length - 1);
                strategies.add(availableStrategies[index]);
            }
        }

        //After choosing strategies, we activate them.
        for(CollisionStrategy strategy : strategies) {
            strategy.onCollision(thisObj, otherObj);
        }
    }
}
