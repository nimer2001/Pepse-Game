package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

public class Trunk {
    private static final int MAXIMUM_TREE_HEIGHT = 400;

    public static GameObject createTrunk(float x, float groundHeightAtX){
        Random random = new Random();
        double treeHeight = random.nextDouble(MAXIMUM_TREE_HEIGHT);
        RectangleRenderable rectangleRenderable = new RectangleRenderable(new Color(100,50,20));
        GameObject trunk = new GameObject(new Vector2(x, (int) (groundHeightAtX - treeHeight)),new Vector2(30, (float) treeHeight), rectangleRenderable);
        trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        trunk.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        trunk.setTag("trunk");
        return trunk;
    }

}
