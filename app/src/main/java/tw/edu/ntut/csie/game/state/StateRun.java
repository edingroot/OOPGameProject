package tw.edu.ntut.csie.game.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.object.BackgroundSet;
import tw.edu.ntut.csie.game.object.Stone;
import tw.edu.ntut.csie.game.object.Tree;
import tw.edu.ntut.csie.game.util.Common;
import tw.edu.ntut.csie.game.util.Constants;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class StateRun extends GameState {
    private final int MAP_LEFT_MARGIN = 180 + Constants.FRAME_LEFT_MARGIN;
    private final int MAP_RIGHT_MARGIN = 150 + Constants.FRAME_RIGHT_MARGIN;

    private MovingBitmap staticBackground;
    private BackgroundSet backgroundSet;
    private MovingBitmap imgFloor;
    private List<MovableGameObject> foreObjects = new ArrayList<>();

    private boolean isGrabbingMap = false;
    private int initForeX = 0;
    private int initPointerX = 0;

    public StateRun(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        // ---------- set back images ----------
        staticBackground = new MovingBitmap(R.drawable.background);
        int initialX = -(BackgroundSet.WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center screen
        staticBackground.setLocation(initialX, 0);

        backgroundSet = new BackgroundSet();

        imgFloor = new MovingBitmap(R.drawable.floor);
        imgFloor.setLocation(
                -(imgFloor.getWidth() - Game.GAME_FRAME_WIDTH) / 2,
                BackgroundSet.WRAP_HEIGHT - BackgroundSet.OVERLAP_FOREGROUND
        );

        // ---------- game objects ----------
        // stones
        Stone stone = new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 10, 300);
        foreObjects.add(stone);
        // trees
        Tree tree = new Tree(imgFloor.getX() + MAP_LEFT_MARGIN + 100, 320);
        foreObjects.add(tree);
    }

    @Override
    public void move() {
        if (!isGrabbingMap) {
            int foreDeltaX = 0;

            // if user grab the map over left or right margin, roll back automatically
            if (imgFloor.getX() + MAP_LEFT_MARGIN > 0) {
                foreDeltaX = -20;
            } else if (imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN < Game.GAME_FRAME_WIDTH) {
                foreDeltaX = 20;
            }
            backgroundSet.setForeDeltaX(foreDeltaX).move();
            imgFloor.setLocation(imgFloor.getX() + foreDeltaX, imgFloor.getY());

            // move foreground objects with map
            for (MovableGameObject gameObject : foreObjects) {
                gameObject.setLocation(gameObject.getX() + foreDeltaX, gameObject.getY());
            }
        }
    }

    @Override
    public void show() {
        // show back image
        staticBackground.show();
        imgFloor.show();
        backgroundSet.show();

        // show objects in foreObjectLists
        for (MovableGameObject gameObject : foreObjects) {
            gameObject.show();
        }
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void keyReleased(int keyCode) {

    }

    @Override
    public void orientationChanged(float pitch, float azimuth, float roll) {

    }

    @Override
    public void accelerationChanged(float dX, float dY, float dZ) {

    }

    @Override
    public boolean pointerPressed(List<Pointer> pointers) {
        if (pointers.size() == 1) {
            Pointer singlePointer = pointers.get(0);

            // check is dragging of objects in foreObjectLists
            for (MovableGameObject gameObject : foreObjects) {
                gameObject.moveStarted(singlePointer);
                if (Common.isInObjectScope(singlePointer, gameObject))
                    gameObject.dragPressed(singlePointer);
            }

            // for moving map
            backgroundSet.moveStarted(singlePointer);
            initForeX = imgFloor.getX();
            initPointerX = singlePointer.getX();
            isGrabbingMap = true;
        }
        return true;
    }

    @Override
    public boolean pointerMoved(List<Pointer> pointers) {
        Pointer singlePointer = pointers.get(0);

        // trigger dragMoved event on dragging objects in foreObjectLists
        for (MovableGameObject gameObject : foreObjects) {
            if (gameObject.isDragging())
                gameObject.dragMoved(singlePointer);
        }

        // move background
        if (isGrabbingMap) {
            int foreDeltaX = singlePointer.getX() - initPointerX;

            int newForeX = initForeX + foreDeltaX;
            if (newForeX < 0 - Constants.FRAME_LEFT_MARGIN &&
                newForeX + imgFloor.getWidth() > Game.GAME_FRAME_WIDTH + Constants.FRAME_RIGHT_MARGIN) {
                backgroundSet.setForeDeltaX(foreDeltaX).dragMoved(singlePointer);
                imgFloor.setLocation(newForeX, imgFloor.getY());

                // move foreground objects with foreground
                for (MovableGameObject gameObject : foreObjects) {
                    gameObject.setLocation(gameObject.getInitialX() + foreDeltaX, gameObject.getY());
                }
            }
        }

        return false;
    }

    @Override
    public boolean pointerReleased(List<Pointer> pointers) {
        Pointer singlePointer = pointers.get(0);

        // trigger dragReleased event on dragging objects in foreObjectLists
        for (MovableGameObject gameObject : foreObjects) {
            gameObject.dragReleased(singlePointer);
        }

        isGrabbingMap = false;

        return false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void release() {
        backgroundSet.release();
        backgroundSet = null;

        for (MovableGameObject gameObject : foreObjects) {
            gameObject.release();
        }
        foreObjects.clear();
        foreObjects = null;
    }
}
