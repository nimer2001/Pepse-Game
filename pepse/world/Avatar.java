package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.*;
import danogl.util.Vector2;
import pepse.PepseGameManger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
    private static final double MAXIMUM_ENERGY = 100;
    public boolean avatarIsJumping = false;
    private final UserInputListener inputListener;
    private List<AnimationRenderable> animationRenderableList;
    private ImageReader imageReader;
    private double energy;

    public Supplier<TextRenderable> createTextRenderable = () -> {
        TextRenderable textRenderable = new TextRenderable(String.valueOf(energy));
        textRenderable.setColor(Color.BLACK);
        return textRenderable;
    };
    public Runnable updateEnergy = () -> energy = Math.min(MAXIMUM_ENERGY, energy + 10);
    public Supplier<Boolean> changeToYellow
            = () -> avatarIsJumping;

    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos, Vector2.ONES.mult(50), imageReader.readImage("assets/idle_0.png", true));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        imageReader.readImage("assets/idle_0.png", true);
        this.energy = MAXIMUM_ENERGY;
        this.imageReader = imageReader;
        animationRenderableList = createAnimationRenderable();
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (getVelocity().equals(Vector2.ZERO)) {
            energy = Math.min(MAXIMUM_ENERGY, energy + 1);
            renderer().setRenderable(animationRenderableList.get(0));
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy >= 0.5) {
            xVel -= VELOCITY_X;
            energy -= 0.5;
            renderer().setRenderable(animationRenderableList.get(2));
            renderer().setIsFlippedHorizontally(true);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy >= 0.5) {
            xVel += VELOCITY_X;
            energy -= 0.5;
            renderer().setRenderable(animationRenderableList.get(2));
            renderer().setIsFlippedHorizontally(false);

        }
        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 && energy >= 10) {
            transform().setVelocityY(VELOCITY_Y);
            energy -= 10;
            renderer().setRenderable(animationRenderableList.get(1));
            if (avatarIsJumping){
                avatarIsJumping = false;

            }else{
                avatarIsJumping = true;
                PepseGameManger.trunkChangedColor = false;

            }
        }



    }


    private List<AnimationRenderable> createAnimationRenderable() {
        List<AnimationRenderable> animationRenderableList = new ArrayList<>();
        // avatar not moving
        Renderable[] avatarInPlace = new Renderable[4];
        for (int i = 0; i < avatarInPlace.length; i++) {
            avatarInPlace[i] = imageReader.readImage("assets/idle_" + i + ".png", true);
        }
        animationRenderableList.add(new AnimationRenderable(avatarInPlace, 0.2));

        // avatar is jumping
        Renderable[] avatarIsJump = new Renderable[4];
        for (int i = 0; i < avatarIsJump.length; i++) {
            avatarIsJump[i] = imageReader.readImage("assets/jump_" + i + ".png", true);
        }
        animationRenderableList.add(new AnimationRenderable(avatarIsJump, 0.2));

        // avatar is moving right/left
        Renderable[] avatarIsRun = new Renderable[6];
        for (int i = 0; i < avatarIsRun.length; i++) {
            avatarIsRun[i] = imageReader.readImage("assets/run_" + i + ".png", true);
        }
        animationRenderableList.add(new AnimationRenderable(avatarIsRun, 0.2));
        return animationRenderableList;
    }
}

