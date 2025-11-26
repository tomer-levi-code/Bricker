package bricker.brick_strategies;

import bricker.gameobjects.brick.Brick;
import bricker.gameobjects.brick.BrickHandler;
import danogl.GameObject;

public class ExplodingBrickStrategy extends BasicCollisionStrategy{

    public ExplodingBrickStrategy(BrickHandler brickHandler) {
        super(brickHandler);
    }

    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        super.onCollision(firstObject, secondObject);
        Brick brick = (Brick) firstObject;
        int col = brick.getCol();
        int row = brick.getRow();

        int[] rowIndexes = {-1, 1, 0, 0};
        int[] colIndexes = {0, 0, 1, -1};

        for (int i = 0; i < rowIndexes.length; i++) {
            Brick curBrick = brickHandler.getBrick(col + colIndexes[i], row + rowIndexes[i]);
            if(curBrick != null) {
                curBrick.getStrategy().onCollision(curBrick, firstObject);
            }
        }
    }
}