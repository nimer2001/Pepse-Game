package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    private static final int TERRAIN_DEPTH = 20;
    private Vector2 windowDimensions;
    private int seed;
    private int groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;
    private List<Block> blockList;
    public Function<Float, Float> function = this::groundHeightAt;
    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = (int) (windowDimensions.y() * ((float) 2 /3));
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed, groundHeightAtX0);

    }
    public float groundHeightAt(float x){
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);
        return groundHeightAtX0 + noise;
    }

    public List<Block> createInRange(int minX, int maxX){
        blockList = new ArrayList<>();
        for (int height = 0 ; height < windowDimensions.y() ; height += Block.SIZE) {
            for (int xCoordination = minX; xCoordination < maxX; xCoordination += Block.SIZE) {
                RectangleRenderable rectangleRenderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                float YCoordination = (float) (Math.floor(groundHeightAt(xCoordination) / Block.SIZE) * Block.SIZE);
                Block block = new Block(new Vector2(xCoordination, YCoordination + height), rectangleRenderable);
                blockList.add(block);
            }
        }
        return blockList;
    }
}
