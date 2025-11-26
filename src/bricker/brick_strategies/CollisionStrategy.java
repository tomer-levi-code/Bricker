package bricker.brick_strategies;

import danogl.GameObject;

/**
 * CollisionStrategy is an interface defining the behavior for handling
 * collisions between game objects. Implementations of this interface
 * will specify the actions to be taken when two game objects collide.
 */
public interface CollisionStrategy {
    void onCollision(GameObject thisObj, GameObject otherObj);
}
