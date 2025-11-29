package bricker.gameobjects.paddle;

/**
 * Enum representing the type of Paddle in the game.
 * The PaddleType is used to differentiate between types of paddles in the game:
 * user-controlled and strategy-based.
 * This determines how the paddle behaves and its starting position.
 */
public enum PaddleType {
    /**
     * Represents a player-controlled paddle at the bottom of the screen.
     */
    USER,
    /**
     * Represents a paddle, also player-controlled, that spawns in the center of the screen
     * after a collision with a special strategy brick and lasts for 4 hits.
     */
    STRATEGY
}
