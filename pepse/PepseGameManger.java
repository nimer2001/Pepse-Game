package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Supplier;

public class PepseGameManger extends GameManager {
    private static final float CYCLE_LENGTH = 30;
    public static boolean trunkChangedColor = false;
    private List<GameObject> gameObjectList;
    private Supplier<Boolean> changeToYellow;
    private UserInputListener inputListener;

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimension = windowController.getWindowDimensions();
        this.inputListener = inputListener;
        // create sky
        GameObject sky = Sky.create(windowDimension);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        // create terrain
        Terrain terrain = new Terrain(windowDimension, 100);
        List<Block> blockList = terrain.createInRange(0, (int) windowDimension.x());
        for (Block block : blockList) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }

        // create sun
        GameObject sun = Sun.create(windowDimension, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);

        // create sunHalo
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);

        // create night
        GameObject night = Night.create(windowDimension, CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.UI);

        // create avatar
        Vector2 pos = new Vector2(0, windowDimension.y() * ((float) 2 /3) - Block.SIZE);
        Avatar avatar = new Avatar(pos, inputListener, imageReader);
        avatar.setTag("avatar");
        gameObjects().addGameObject(avatar);
        windowController.setTargetFramerate(80);
        // create numeric energy
        NumericEnergy numericEnergy = new NumericEnergy(Vector2.ZERO, new Vector2(25, 25), avatar.createTextRenderable);
        gameObjects().addGameObject(numericEnergy, Layer.BACKGROUND);
        this.changeToYellow = avatar.changeToYellow;
        Flora flora = new Flora(Vector2.ZERO, Vector2.ZERO, null, terrain.function ,avatar.updateEnergy, avatar.changeToYellow, gameObjects());
        gameObjectList = flora.createInRange(60, (int) windowDimension.x());
        for (GameObject game: gameObjectList){
            gameObjects().addGameObject(game, Layer.STATIC_OBJECTS);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (changeToYellow.get() && !trunkChangedColor){
            trunkChangedColor = true;
            for( GameObject game: gameObjectList){
                if (game.getTag().equals("trunk")){
                    game.renderer().setRenderable(new RectangleRenderable(ColorSupplier.approximateColor(new Color(100,50,20))));
                }
            }
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)){
            float x = 0.2f;
            for( GameObject game: gameObjectList){
                if (game.getTag().equals("leaf")){
                    Runnable func = () ->
                            new Transition<Float>(game,game.renderer()::setRenderableAngle,0f,90f,Transition.LINEAR_INTERPOLATOR_FLOAT,5,Transition.TransitionType.TRANSITION_ONCE,null);
                    x += 0.1f;
                    new ScheduledTask(game, x, false, func);
                }
            }
        }

    }
    public static void main(String[] args) {
        new PepseGameManger().run();
    }
}
