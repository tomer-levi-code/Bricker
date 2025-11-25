package bricker.brick_strategies;

import bricker.gameobjects.brick.BrickHandler;
import bricker.main.BrickerGameManager;

import java.util.Random;

public class StrategyFactory {

    private CollisionStrategy[] strategies;
    BrickerGameManager brickerGameManager;
    BrickHandler brickHandler;
    private static Random random;

    public StrategyFactory(BrickHandler brickHandler) {
        this.brickHandler = brickHandler;
        random = new Random();
        strategies = new CollisionStrategy[]{
                new BasicCollisionStrategy(brickHandler)
        };
    }

//    public CollisionStrategy generate() {
//        int rand = random.nextInt(10) + 1;
//        switch(rand) {
//            case 6:
//                return strategies[1];
//            case 7:
//                return strategies[2];
//            case 8:
//                return strategies[3];
//            case 9:
//                return strategies[4];
//            case 10:
//                return strategies[5];
//            case 1:
//            case 2:
//            case 3:
//            case 4:
//            case 5:
//            default:
//                return strategies[0];
//    }

    public CollisionStrategy generate() {
        return strategies[0];
    }
}
