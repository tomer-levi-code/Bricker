package bricker.brick_strategies;

import bricker.gameobjects.brick.Brick;
import bricker.gameobjects.brick.BrickHandler;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy {

    private final BrickHandler brickHandler;

    public BasicCollisionStrategy(BrickHandler brickHandler) {
        this.brickHandler = brickHandler;
    }

    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        Brick brick = (Brick)firstObject;
        brickHandler.destroyBrick(brick.getCol(), brick.getRow());
    }
}
