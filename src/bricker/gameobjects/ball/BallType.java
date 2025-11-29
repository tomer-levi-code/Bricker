package bricker.gameobjects.ball;

/**
 * An enum representing the type of ball in the game.
 * This is used to differentiate between various ball types with distinct
 * behaviors and attributes, such as the MAIN type and the PUCK type.
 */
public enum BallType {
    /**
     * Represents the standard ball used in the game.
     */
    MAIN,
    /**
     * Represents a smaller ball which doesn't strike the player if it leaves the screen.
     */
    PUCK
}
