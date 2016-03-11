package tw.edu.ntut.csie.game.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.object.Stone;
import tw.edu.ntut.csie.game.util.Common;
import tw.edu.ntut.csie.game.util.Constants;
import tw.edu.ntut.csie.game.util.DraggableGameObject;

public class StateRun extends GameState {
    private final int MAP_LEFT_MARGIN = 180 + Constants.FRAME_LEFT_MARGIN;
    private final int MAP_RIGHT_MARGIN = 150 + Constants.FRAME_RIGHT_MARGIN;
    private final int SKYLINE_Y = 230;
    private final double BACK_MOVE_RATIO = 0.6; // ratio relative to foreground

    private MovingBitmap imgBackground;
    private MovingBitmap imgFloor;
    private List<List<DraggableGameObject>> foreObjectLists = new ArrayList<>();
    private List<DraggableGameObject> objStones = new ArrayList<>();

    private boolean isGrabbingMap = false;
    private int initBackX = 0, initForeX = 0;
    private int initPointerX = 0;

    public StateRun(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        // set back images
        imgBackground = new MovingBitmap(R.drawable.background);
        imgFloor = new MovingBitmap(R.drawable.floor);
        imgBackground.setLocation(
                -(imgBackground.getWidth() - Game.GAME_FRAME_WIDTH) / 2,
                SKYLINE_Y - imgBackground.getWidth()
        );
        imgFloor.setLocation(
                -(imgFloor.getWidth() - Game.GAME_FRAME_WIDTH) / 2,
                SKYLINE_Y
        );

        // game objects
        Stone stone = new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 10, 300);
        stone.initialize();
        objStones.add(stone);

        // add lists to foreObjectLists
        foreObjectLists.add(objStones);

    }

    @Override
    public void move() {
        if (!isGrabbingMap) {
            int backDeltaX, foreDeltaX = 0;

            // if user grab the map over left or right margin, roll back automatically
            if (imgFloor.getX() + MAP_LEFT_MARGIN > 0) {
                foreDeltaX = -20;
            } else if (imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN < Game.GAME_FRAME_WIDTH) {
                foreDeltaX = 20;
            }
            backDeltaX = (int) (foreDeltaX * BACK_MOVE_RATIO);
            imgBackground.setLocation(imgBackground.getX() + backDeltaX, 0);
            imgFloor.setLocation(imgFloor.getX() + foreDeltaX, imgFloor.getY());

            // move foreground objects with map
            for (List<DraggableGameObject> objList : foreObjectLists) {
                for (DraggableGameObject gameObject : objList) {
                    gameObject.setLocation(gameObject.getX() + foreDeltaX, gameObject.getY());
                }
            }
        }
    }

    @Override
    public void show() {
        // show back image
        imgBackground.show();
        imgFloor.show();

        // show objects in foreObjectLists
        for (List<DraggableGameObject> objList : foreObjectLists) {
            for (DraggableGameObject gameObject : objList) {
                gameObject.show();
            }
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
            for (List<DraggableGameObject> objList : foreObjectLists) {
                for (DraggableGameObject gameObject : objList) {
                    gameObject.dragPressed(singlePointer);
                    if (Common.isInImageScope(singlePointer, gameObject))
                        gameObject.setDragging(true);
                }
            }

            // to move background
            initBackX = imgBackground.getX();
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
        for (List<DraggableGameObject> objList : foreObjectLists) {
            for (DraggableGameObject gameObject : objList) {
                if (gameObject.isDragging())
                    gameObject.dragMoved(singlePointer);
            }
        }

        // move background
        if (isGrabbingMap) {
            int foreDeltaX = singlePointer.getX() - initPointerX;

            int newForeX = initForeX + foreDeltaX;
            int newBackX = (int) (initBackX + foreDeltaX * BACK_MOVE_RATIO);
            if (newForeX < 0 - Constants.FRAME_LEFT_MARGIN &&
                newForeX + imgFloor.getWidth() > Game.GAME_FRAME_WIDTH + Constants.FRAME_RIGHT_MARGIN) {
                imgBackground.setLocation(newBackX, imgBackground.getY());
                imgFloor.setLocation(newForeX, imgFloor.getY());

                // move foreground objects with foreground
                for (List<DraggableGameObject> objList : foreObjectLists) {
                    for (DraggableGameObject gameObject : objList) {
                        gameObject.setLocation(gameObject.getInitialX() + foreDeltaX, gameObject.getY());
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean pointerReleased(List<Pointer> pointers) {
        Pointer singlePointer = pointers.get(0);

        // trigger dragReleased event on dragging objects in foreObjectLists
        for (List<DraggableGameObject> objList : foreObjectLists) {
            for (DraggableGameObject gameObject : objList) {
                gameObject.dragReleased(singlePointer);
                if (gameObject.isDragging()) {
                    gameObject.setDragging(false);
                }
            }
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
        imgBackground.release();
        imgBackground = null;

        for (List<DraggableGameObject> objList : foreObjectLists) {
            for (DraggableGameObject gameObject : objList) {
                gameObject.release();
            }
            objList.clear();
        }
        foreObjectLists.clear();
    }
}
