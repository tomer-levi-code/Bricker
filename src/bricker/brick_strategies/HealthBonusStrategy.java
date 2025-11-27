package bricker.brick_strategies;

import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.health_points.Heart;
import bricker.gameobjects.health_points.HeartFactory;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.util.Vector2;

public class HealthBonusStrategy extends BasicCollisionStrategy{

    private static HeartFactory heartFactory;
    private static BrickerGameManager brickerGameManager;
    private static Vector2 HEART_FALLING_VELOCITY;

    public HealthBonusStrategy(BrickHandler brickHandler, BrickerGameManager brickerGameManager, ImageReader imageReader) {
        super(brickHandler);
        HealthBonusStrategy.heartFactory = HeartFactory.getInstance(imageReader);
        HealthBonusStrategy.brickerGameManager = brickerGameManager;
        HEART_FALLING_VELOCITY = new Vector2(0,100);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        Heart heart = heartFactory.build(brickerGameManager, brickerGameManager.healthPointsPanel.getHealthPointsItemEdgeLength(), Vector2.ZERO);
        heart.setCenter(thisObj.getCenter());
        heart.setVelocity(HEART_FALLING_VELOCITY);

        brickerGameManager.addItem(heart, Layer.DEFAULT);
    }
}
