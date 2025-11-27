package bricker.gameobjects.health_points;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class HeartFactory {

    private static HeartFactory instance;

    private static Renderable heartImage;

    private HeartFactory(ImageReader imageReader) {
        heartImage = imageReader.readImage("assets/heart.png", true);
    }

    public static HeartFactory getInstance(ImageReader imageReader) {
        if (instance == null) {
            instance = new HeartFactory(imageReader);
        }
        return instance;
    }

    public Heart build(BrickerGameManager brickerGameManager,
                            float edgeLength,
                            Vector2 topLeft) {
        return new Heart(brickerGameManager,
                topLeft,
                new Vector2(edgeLength, edgeLength),
                heartImage);
    }

}
