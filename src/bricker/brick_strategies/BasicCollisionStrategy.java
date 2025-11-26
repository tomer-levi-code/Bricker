package bricker.brick_strategies;

import bricker.gameobjects.brick.Brick;
import bricker.gameobjects.brick.BrickHandler;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy {

    protected final BrickHandler brickHandler;

    public BasicCollisionStrategy(BrickHandler brickHandler) {
        this.brickHandler = brickHandler;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {

        if(thisObj == null || otherObj == null) {
            return;
        }

        Brick brick = (Brick) thisObj;
        brickHandler.destroyBrick(brick.getCol(), brick.getRow());
    }
}
