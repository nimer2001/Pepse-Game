package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.TimerTask;
import java.util.function.Supplier;

public class Apple extends GameObject{
    private GameObjectCollection gameObjects;
    private  Vector2 topLeftCorner;
    private Vector2 dimensions;
    private OvalRenderable renderable;
    private final Runnable updateEnergy;
    private final Supplier<Boolean> changeToYellow;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Apple(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable ,  GameObjectCollection gameObjects, Runnable updateEnergy, Supplier<Boolean> avatarJump) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.renderable = (OvalRenderable)renderable;
        this.updateEnergy = updateEnergy;
        this.changeToYellow = avatarJump;
    }


//    public GameObject createApples(){
//        GameObject apple = new GameObject(this.topLeftCorner, this.dimensions, this.renderable);
//        apple.setTag("apple");
//        return apple;
//    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("avatar")) {
            GameObject r = this;
            gameObjects.removeGameObject(this, Layer.STATIC_OBJECTS);
//            float delayInSeconds = 3.0f;
//            long delayInMillis = (long) (delayInSeconds * 1000);
//
//            new ScheduledTask(r,de,true,()->gameObjects.addGameObject(r,Layer.STATIC_OBJECTS));
            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    gameObjects.addGameObject(r, Layer.STATIC_OBJECTS);
                }
            }, 3000);
            this.updateEnergy.run();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (changeToYellow.get()){
            renderer().setRenderable(new OvalRenderable(Color.yellow));
        }
        else {
            renderer().setRenderable(new OvalRenderable(Color.RED));
        }

    }

}
