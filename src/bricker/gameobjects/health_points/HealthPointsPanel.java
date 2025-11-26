package bricker.gameobjects.health_points;


import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Stack;

public class HealthPointsPanel{

    private BrickerGameManager brickerGameManager;
    private ImageReader imageReader;
    private final float EDGE_BUFFER = 17f, OBJECT_BUFFER = 3f;
    private float maxHeartWidth;
    private static float edgeLength;
    private final float HeartTopLeftY;
    private int healthPoints = 0;
    private static Stack<GameObject> HPStack;
    private TextRenderable HealthPointsText;

    public HealthPointsPanel(BrickerGameManager brickerGameManager,
                             Vector2 windowDimensions,
                             ImageReader imageReader) {

        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;

        HPStack = new Stack<>();
        maxHeartWidth = ((windowDimensions.x() - (2 * EDGE_BUFFER)) / brickerGameManager.DEFAULT_HP) - (2 * OBJECT_BUFFER);
        edgeLength = Math.min(windowDimensions.y() / 20f, maxHeartWidth);
        HeartTopLeftY = windowDimensions.y() - (EDGE_BUFFER + edgeLength);
    }


    private void initNumericHP(Vector2 windowDimensions) {

        String stringHP = Integer.toString(healthPoints);
        HealthPointsText = new TextRenderable(stringHP);
        HealthPointsText.setColor(Color.decode("#24c538"));
        GameObject numericHP = new GameObject(
                new Vector2(EDGE_BUFFER + OBJECT_BUFFER,
                        windowDimensions.y() - (2 * (EDGE_BUFFER + edgeLength))),
                new Vector2(edgeLength,
                        edgeLength),
                HealthPointsText);
        brickerGameManager.addItem(numericHP, Layer.UI);

    }

    private void updateNumericHP() {

        String stringHP = Integer.toString(healthPoints);
        HealthPointsText.setString(stringHP);
        switch (healthPoints) {
            case 2:
                HealthPointsText.setColor(Color.decode("#dcea24"));
                break;
            case 1:
            case 0:
                HealthPointsText.setColor(Color.decode("#fa000a"));
                break;
            case 3:
            default:
                HealthPointsText.setColor(Color.decode("#24c538"));
        }

    }

    public void decreaseHP() {

        if(healthPoints > 0) {
            brickerGameManager.removeItem(HPStack.pop(), Layer.UI);
            healthPoints--;
            updateNumericHP();
        }

    }

    public void increaseHP() {
        if(healthPoints < 4) {
            HeartFactory heartFactory = HeartFactory.getInstance(imageReader);
            float nextHeartTopLeftX = EDGE_BUFFER + (healthPoints * (edgeLength + (2 * OBJECT_BUFFER))) + OBJECT_BUFFER;
            GameObject heart = heartFactory.build(brickerGameManager, edgeLength, new Vector2(nextHeartTopLeftX, HeartTopLeftY));
            HPStack.push(heart);
            brickerGameManager.addItem(heart, Layer.UI);
            healthPoints++;
            updateNumericHP();
        }
    }

    public void initHP(Vector2 windowDimensions) {

        initNumericHP(windowDimensions);

        for (int i = 0; i < brickerGameManager.DEFAULT_HP; i++) {
            increaseHP();
        }

    }

    public int getHP() {

        return healthPoints;

    }

    public float getEdgeLength() {
        return edgeLength;
    }

}
