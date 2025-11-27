package bricker.gameobjects.health_points;

import bricker.gameobjects.paddle.PaddleHandler;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Heart extends GameObject {

    private final BrickerGameManager brickerGameManager;

    public Heart(BrickerGameManager brickerGameManager,
                 Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);

        this.brickerGameManager = brickerGameManager;
    }

    private void terminate() {
        brickerGameManager.removeItem(this, Layer.DEFAULT);
    }

    private void terminateIfReachedBottom() {
        if(BrickerGameManager.windowDimensions.y() < getCenter().y()) {
            terminate();
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(PaddleHandler.MAIN_PADDLE_TAG);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        terminateIfReachedBottom();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        brickerGameManager.increaseHP();
        terminate();
    }
}
