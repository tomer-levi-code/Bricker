package bricker.gameobjects.health_points;


import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Stack;

/**
 * HealthPointsPanel is responsible for managing and displaying the player's health points (HP)
 * in the game. The panel shows the health points both as graphical icons (e.g., hearts) and as
 * numeric text. It allows for initialization of the HP panel, increasing or decreasing the player's HP,
 * and provides a mechanism to fetch the current HP level.
 */
public class HealthPointsPanel{

    private BrickerGameManager brickerGameManager;
    private ImageReader imageReader;
    private final float EDGE_BUFFER = 17f, OBJECT_BUFFER = 3f;
    private static final int DEFAULT_HP = 3;
    private float maxHeartWidth;
    private static float edgeLength;
    private final float HeartTopLeftY;
    private int healthPoints = 0;
    private static Stack<GameObject> HPStack;
    private TextRenderable HealthPointsText;

    /**
     * Constructs a HealthPointsPanel instance, which is responsible for managing
     * and displaying the health points (HP) in the game.
     *
     * @param brickerGameManager The game manager instance that owns and coordinates the game logic.
     * @param windowDimensions The dimensions of the game window used to calculate the position
     *                         and size of the health points display.
     * @param imageReader The image reader used for loading graphical assets, such as heart icons,
     *                    for the health points representation.
     */
    public HealthPointsPanel(BrickerGameManager brickerGameManager,
                             Vector2 windowDimensions,
                             ImageReader imageReader) {

        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;

        HPStack = new Stack<>();
        maxHeartWidth = ((windowDimensions.x() - (2 * EDGE_BUFFER)) / DEFAULT_HP) - (2 * OBJECT_BUFFER);
        edgeLength = Math.min(windowDimensions.y() / 20f, maxHeartWidth);
        HeartTopLeftY = windowDimensions.y() - (EDGE_BUFFER + edgeLength);
    }


    /**
     * Initializes a numeric health points (HP) display on the game window.
     * This method creates a game object representing the player's current HP as a numeric value
     * and adds it to the game's UI layer, positioned relative to the game window dimensions.
     *
     * @param windowDimensions The dimensions of the game window, used to position the numeric HP display.
     */
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

    /**
     * Updates the numeric health points (HP) display on the screen.
     * This method converts the current health points value to a string and sets it
     * as the text of the numeric HP display. It also adjusts the color of the
     * numeric HP display based on the current health points value:
     * - Green for health points above 2.
     * - Yellow for health points equal to 2.
     * - Red for health points equal to or below 1.
     * This method ensures that the numeric HP display reflects the player's current
     * health status both visually and numerically.
     */
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

    /**
     * Increases the player's health points (HP) by adding a visual heart representation
     * to the game's UI and updating the numeric health points display. This method
     * ensures that the additional HP is only added if the current health points are below 4.
     */
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

    /**
     * Reduces the player's health points (HP) by one, removes the corresponding heart
     * visual representation from the game's UI, and updates the numeric HP display.
     * This method ensures that the player's health points only decrease if the current
     * HP is greater than zero.
     */
    public void decreaseHP() {

        if(healthPoints > 0) {
            brickerGameManager.removeItem(HPStack.pop(), Layer.UI);
            healthPoints--;
            updateNumericHP();
        }

    }

    /**
     * Initializes the health points (HP) display for the game.
     * This method sets up the numeric representation of health points and
     * populates the initial number of hearts representing the default health points.
     *
     * @param windowDimensions The dimensions of the game window, used to position
     *                         the numeric HP display and the visual heart representations.
     */
    public void initHP(Vector2 windowDimensions) {

        initNumericHP(windowDimensions);

        for (int i = 0; i < DEFAULT_HP; i++) {
            increaseHP();
        }

    }

    /**
     * Retrieves the current health points (HP) of the player.
     *
     * @return The current health points (HP) as an integer.
     */
    public int getHP() {

        return healthPoints;

    }

    /**
     * Retrieves the edge length of the health points item's visual representation.
     *
     * @return A float representing the edge length of the health points item.
     */
    public float getHealthPointsItemEdgeLength() {
        return edgeLength;
    }

}
