package bricker.gameobjects.brick;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Brick extends GameObject {
    public static final float HEIGHT_IN_PX = 15f;
    private CollisionStrategy strategy;
    private final int row;
    private final int col;

    public Brick(Vector2 topLeftCorner, int col, int row, float width, Renderable renderable, CollisionStrategy strategy){
        super(topLeftCorner, new Vector2(width, HEIGHT_IN_PX), renderable);
        this.strategy = strategy;
        this.row = row;
        this.col = col;
    };

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        strategy.onCollision(this, other);
    }

    public CollisionStrategy getStrategy() {
        return strategy;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
