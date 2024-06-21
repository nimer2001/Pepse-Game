package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo extends GameObject {
    private static final Color SUN_HALO_COLOR = new Color(255,255,0,20);

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public SunHalo(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }
    public static GameObject create(GameObject sun){
        OvalRenderable ovalRenderable = new OvalRenderable(SUN_HALO_COLOR);
        Vector2 topLeftCorner = new Vector2(sun.getTopLeftCorner().x() - 25 , sun.getTopLeftCorner().y() - 25);
        GameObject sunHalo = new GameObject(topLeftCorner, new Vector2(200, 200), ovalRenderable);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag("sunHalo");
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }




}
