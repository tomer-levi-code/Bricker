package bricker.gameobjects.brick;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a Brick object in the game, extending the GameObject class.
 * A brick is a static object in the game grid that can interact with
 * other game objects through collisions. Each brick is assigned a specific
 * row and column in the game's grid, along with a collision strategy
 * defining its behavior when a collision occurs.
 */
public class Brick extends GameObject {

    private CollisionStrategy strategy;
    private final int row;
    private final int col;

    /**
     * Constructs a new Brick object with the specified properties.
     *
     * @param topLeftCorner The top-left corner position of the brick, represented as a Vector2.
     * @param col The column index of the brick in the game grid.
     * @param row The row index of the brick in the game grid.
     * @param width The width of the brick in pixels.
     * @param renderable The visual representation of the brick as a renderable object.
     * @param strategy The collision strategy defining the behavior of the brick when a collision occurs.
     */
    public Brick(Vector2 topLeftCorner,
                 int col,
                 int row,
                 float width,
                 float height,
                 Renderable renderable,
                 CollisionStrategy strategy){
        super(topLeftCorner,
                new Vector2(width, height),
                renderable);
        this.strategy = strategy;
        this.row = row;
        this.col = col;
    };

    /**
     * Handles the behavior that occurs when the brick collides with another GameObject.
     * When a collision is detected, this method invokes the defined collision strategy
     * associated with the brick.
     *
     * @param other The GameObject that this brick collides with.
     * @param collision The collision information detailing the interaction between the objects.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        strategy.onCollision(this, other);
    }

    /**
     * @return The CollisionStrategy that defines the behavior of the Brick
     *         upon collision with other game objects.
     */
    public CollisionStrategy getStrategy() {
        return strategy;
    }

    /**
     * @return The row index of the Brick.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return The column index of the Brick.
     */
    public int getCol() {
        return col;
    }
}
