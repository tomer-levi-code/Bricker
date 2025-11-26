package bricker.brick_strategies;

import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.health_points.HeartFactory;
import danogl.GameObject;
import danogl.gui.ImageReader;

public class HealthBonusStrategy extends BasicCollisionStrategy{

    HeartFactory heartFactory;

    public HealthBonusStrategy(BrickHandler brickHandler, ImageReader imageReader) {
        super(brickHandler);
        this.heartFactory = HeartFactory.getInstance(imageReader);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        //Calculate edgeLength
//        GameObject heart = heartFactory.build();
    }
}
