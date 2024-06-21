package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;

public class Leafs {
    private Random random;
    private GameObjectCollection gameObjects;

//    public Leafs(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjects) {
//        super(topLeftCorner, dimensions, renderable);
//        random = new Random();
//        this.gameObjects = gameObjects;
//    }

    public static GameObject createLeaf(Vector2 pos){
        RectangleRenderable rectangleRenderable = new RectangleRenderable(new Color(50, 200 , 30));
        GameObject leaf = new GameObject(pos, new Vector2(30,30), rectangleRenderable);
        return leaf;
    }


}
