package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Function;
import java.util.function.Supplier;

public class NumericEnergy extends GameObject {
    private final Supplier<TextRenderable> function  ;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public NumericEnergy(Vector2 topLeftCorner, Vector2 dimensions, Supplier<TextRenderable> function) {
        super(topLeftCorner, dimensions, function.get());
        this.function = function;

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderer().setRenderable(function.get());
    }
}
