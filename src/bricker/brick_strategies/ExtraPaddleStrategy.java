package bricker.brick_strategies;

import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.paddle.Paddle;
import bricker.gameobjects.paddle.PaddleFactory;
import bricker.gameobjects.paddle.PaddleType;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class ExtraPaddleStrategy extends BasicCollisionStrategy {

    private static BrickerGameManager brickerGameManager;
    private static PaddleFactory paddleFactory;
    private static Paddle extraPaddle;

    public ExtraPaddleStrategy(BrickerGameManager brickerGameManager, BrickHandler brickHandler, PaddleFactory paddleFactory) {

        super(brickHandler);

        ExtraPaddleStrategy.brickerGameManager = brickerGameManager;
        ExtraPaddleStrategy.paddleFactory = paddleFactory;

    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {

        super.onCollision(thisObj, otherObj);

        if(extraPaddle == null) {

            extraPaddle = paddleFactory.build(PaddleType.STRATEGY);
            brickerGameManager.addItem(extraPaddle, Layer.DEFAULT);
            extraPaddle.reset();

        }

    }

}
