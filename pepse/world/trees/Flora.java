package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Avatar;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class Flora extends GameObject {
    private final Function<Float,Float> function;
    private Random random;
    private List<GameObject> gameObjectList;
    private GameObjectCollection gameObjects;
    private Runnable updateEnergy;
    private Supplier<Boolean> avatarJump;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Flora(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Function<Float,Float> function, Runnable updateEnergy,Supplier<Boolean> avatarJump,  GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.function = function;
        random = new Random();
        gameObjectList = new ArrayList<>();
        this.gameObjects = gameObjects;
        this.updateEnergy = updateEnergy;
        this.avatarJump = avatarJump;
    }
    public List<GameObject> createInRange(int minX, int maxX){
        for (float x = minX; x < maxX; x += Block.SIZE*5) {
            float groundHeightAtX = function.apply(x);
            double treeProbability = random.nextDouble(1);
            if (treeProbability < 0.5){
                System.out.println(x);
                GameObject trunk = Trunk.createTrunk(x , groundHeightAtX);
                gameObjectList.add(trunk);
                Vector2 topLeftCorner = new Vector2((int)trunk.getTopLeftCorner().x() - 35 , (int)trunk.getTopLeftCorner().y() - 35);
                GameObject trunkSquare = new GameObject(topLeftCorner, new Vector2(100, 100), new RectangleRenderable(Color.yellow));
                addLeafToTrunk(trunkSquare, trunk);
            }
        }
        return gameObjectList;
    }



    private void addLeafToTrunk(GameObject trunkSquare, GameObject trunk){
        for (int i = 0; i < trunkSquare.getDimensions().x(); i += 30) {
            float x = 0.2f;
            for (int j = 0; j < trunkSquare.getDimensions().y(); j += 30) {
               double leafProbability = random.nextDouble(1);
                Vector2 leafPos = new Vector2(trunkSquare.getTopLeftCorner().x() + i, (int)trunkSquare.getTopLeftCorner().y() + j);
                GameObject leaf = Leafs.createLeaf(leafPos);
                if (leafProbability < 0.75){

                    leaf.setTag("leaf");
                    Runnable func = () ->{
                        new Transition<Vector2>(leaf,leaf::setDimensions,leaf.getDimensions(),leaf.getDimensions(),Transition.LINEAR_INTERPOLATOR_VECTOR,0.5F,Transition.TransitionType.TRANSITION_LOOP,null);
//                        new Transition<Float>(leaf,leaf.renderer()::setRenderableAngle,0f,0f,Transition.LINEAR_INTERPOLATOR_FLOAT,5,Transition.TransitionType.TRANSITION_BACK_AND_FORTH,null);
                    };
                    x += 1f;
                    new ScheduledTask(leaf, x, false, func);
                    gameObjectList.add(leaf);
                }else if((leafPos.x() + 15  < trunk.getTopLeftCorner().x()
                        || (leafPos.x() > trunk.getTopLeftCorner().x()+trunk.getDimensions().x() + 15)
                        ||(leafPos.y() + 15 < getTopLeftCorner().y())) && leafPos.y() < function.apply(leafPos.x())-30){
                    OvalRenderable ovalRenderable = new OvalRenderable(Color.RED);
                    Apple apple  = new Apple(leafPos, new Vector2(15, 15), ovalRenderable, gameObjects, updateEnergy, avatarJump);
                    apple.setCenter(leaf.getCenter());

                    apple.setTag("apple");
                    gameObjectList.add(apple);
                }
            }
        }
    }
}
