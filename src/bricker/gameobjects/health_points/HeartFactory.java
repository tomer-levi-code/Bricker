package bricker.gameobjects.health_points;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The HeartFactory class is a singleton factory responsible for creating instances of the Heart class.
 * It ensures the heart image is loaded once and reused for all hearts created.
 */
public class HeartFactory {

    private static HeartFactory instance;

    private static Renderable heartImage;

    private HeartFactory(ImageReader imageReader) {
        heartImage = imageReader.readImage("assets/heart.png", true);
    }

    /**
     * Returns a singleton instance of the HeartFactory. If the instance does not exist,
     * it initializes a new instance of the factory with the provided ImageReader.
     *
     * @param imageReader An ImageReader object used to load the heart image. It is required
     *                    to initialize the HeartFactory if it has not been instantiated already.
     * @return A singleton instance of the HeartFactory.
     */
    public static HeartFactory getInstance(ImageReader imageReader) {
        if (instance == null) {
            instance = new HeartFactory(imageReader);
        }
        return instance;
    }

    /**
     * Creates and returns a new instance of the Heart class.
     *
     * @param brickerGameManager The game manager responsible for managing game elements and state.
     * @param edgeLength Edge length of the bounding square of the Heart object.
     * @param topLeft The top-left coordinate where the Heart object will be placed.
     * @return A new instance of the Heart class with the specified properties.
     */
    public Heart build(BrickerGameManager brickerGameManager,
                            float edgeLength,
                            Vector2 topLeft) {
        return new Heart(brickerGameManager,
                topLeft,
                new Vector2(edgeLength, edgeLength),
                heartImage);
    }

}
