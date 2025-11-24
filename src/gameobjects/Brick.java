package gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Brick extends GameObject {
    public static final float HEIGHT_IN_PX = 15f;
    private float width;
    private CollisionStrategy strategy;

    public Brick(Vector2 topLeftCorner, float width, Renderable renderable, CollisionStrategy strategy){
        super(topLeftCorner, new Vector2(width, HEIGHT_IN_PX), renderable);
        this.width = width;
        this.strategy = strategy;
    };

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        strategy.onCollision(this, other);
    }
}
