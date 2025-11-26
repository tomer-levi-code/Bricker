package bricker.gameobjects.health_points;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class HeartFactory {

    private static HeartFactory instance;

    Renderable heartImage;

    private HeartFactory(ImageReader imageReader) {
        heartImage = imageReader.readImage("assets/heart.png", true);
    }

    public static HeartFactory getInstance(ImageReader imageReader) {
        if(instance == null) {
            instance = new HeartFactory(imageReader);
        }
        return instance;
    }

    public GameObject build(float edgeLength,
                            Vector2 topLeft) {
        return  new GameObject(topLeft,
                new Vector2(edgeLength, edgeLength),
                heartImage);
    }

}
