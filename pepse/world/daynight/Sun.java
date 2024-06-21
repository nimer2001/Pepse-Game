package pepse.world.daynight;

import danogl.GameManager;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;

public class Sun extends GameObject {
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Sun(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        float groundHeight = windowDimensions.y() * ((float) 2 /3);
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, groundHeight);
        Vector2 topLeftCorner = new Vector2((windowDimensions.x() / 2) - 75, (windowDimensions.y() / 2) - 75);
        OvalRenderable ovalRenderable = new OvalRenderable(Color.yellow);
        GameObject sun = new GameObject(topLeftCorner, new Vector2(150,150), ovalRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");
        Vector2 initialSunCenter = windowDimensions.mult(0.5f);
        new Transition<Float>(sun, (Float angle)->sun.setCenter(initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                (float)0, (float)360, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength, Transition.TransitionType.TRANSITION_LOOP,null);
        return sun;

    }
}
