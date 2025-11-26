package bricker.gameobjects.paddle;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private static final int COLLISIONS_TO_KILL = 4;
    private final Vector2 CENTER_START_COORDINATES;

    private final BrickerGameManager brickerGameManager;
    private final UserInputListener inputListener;
    private static Vector2 windowDimensions;

    private int collisionCounter;


    public Paddle(Vector2 topLeftCorner,
                  Vector2 CENTER_START_COORDINATES,
                  Vector2 dimensions,
                  int  collisionCounter,
                  BrickerGameManager brickerGameManager,
                  Renderable renderable,
                  UserInputListener inputListener,
                  Vector2 windowDimensions) {

        super(topLeftCorner, dimensions, renderable);

        this.brickerGameManager = brickerGameManager;
        this.inputListener = inputListener;
        Paddle.windowDimensions = windowDimensions;

        this.CENTER_START_COORDINATES = CENTER_START_COORDINATES;

        this.collisionCounter = collisionCounter;

    }

    public void reset() {
        setCenter(CENTER_START_COORDINATES);
    }

    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        Vector2 topLeft = getTopLeftCorner();
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && topLeft.x() > 0) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && topLeft.x() + getDimensions().x() < windowDimensions.x()) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {

        super.onCollisionEnter(other, collision);

        if(collisionCounter != PaddleFactory.MAIN_PADDLE_COUNTER) {

            collisionCounter++;

            if(collisionCounter == COLLISIONS_TO_KILL) {

                brickerGameManager.removeItem(this, Layer.DEFAULT);

            }


        }

    }
}
