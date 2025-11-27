package bricker.brick_strategies;

import bricker.gameobjects.brick.BrickHandler;
import bricker.gameobjects.health_points.Heart;
import bricker.gameobjects.health_points.HeartFactory;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.util.Vector2;

/**
 * HealthBonusStrategy is an extension of BasicCollisionStrategy.
 * In addition to the default behavior, a heart bonus is generated upon a brick collision.
 * The heart bonus falls down the screen when a brick is hit.
 * If it's collected by the player (using the paddle),
 * the heart provides additional health points.
 * The heart bonus appears at the location of the destroyed brick.
 */
public class HealthBonusStrategy extends BasicCollisionStrategy{

    private static HeartFactory heartFactory;
    private static BrickerGameManager brickerGameManager;
    private static Vector2 HEART_FALLING_VELOCITY;

    /**
     * Constructs a HealthBonusStrategy.
     *
     * @param brickHandler BrickHandler instance responsible for managing the game’s bricks.
     * @param brickerGameManager the game manager responsible for overall game state and behavior.
     * @param imageReader ImageReader instance used to load the heart image asset.
     */
    public HealthBonusStrategy(BrickHandler brickHandler,
                               BrickerGameManager brickerGameManager,
                               ImageReader imageReader) {
        super(brickHandler);
        HealthBonusStrategy.heartFactory = HeartFactory.getInstance(imageReader);
        HealthBonusStrategy.brickerGameManager = brickerGameManager;
        HEART_FALLING_VELOCITY = new Vector2(0,100);
    }

    /**
     * Handles the collision of a game object with another object in the game.
     * In addition to the default collision behavior, this method generates a heart bonus
     * at the location of the current game object involved in the collision.
     * The heart falls down the screen and adds health points to the player if collected.
     *
     * @param thisObj The current game object involved in the collision, expected to be a brick.
     *                The heart bonus will appear at its location.
     * @param otherObj The other game object involved in the collision. This parameter
     *                 does not directly influence the behavior in this method.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        Heart heart = heartFactory.build(brickerGameManager,
                brickerGameManager.healthPointsPanel.getHealthPointsItemEdgeLength(),
                Vector2.ZERO);
        heart.setCenter(thisObj.getCenter());
        heart.setVelocity(HEART_FALLING_VELOCITY);

        brickerGameManager.addItem(heart, Layer.DEFAULT);
    }
}
