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
import tw.edu.ntut.csie.game.util.DraggableGameObject;

public class StateRun extends GameState {
    private final int MAP_LEFT_MARGIN = 230;
    private final int MAP_RIGHT_MARGIN = 200;

    private MovingBitmap imgMap;
    private List<List<DraggableGameObject>> foreObjectLists = new ArrayList<>();
    private List<DraggableGameObject> objStones = new ArrayList<>();

    private boolean isGrabbingMap = false;
    private int initMapX = 0;
    private int initPointerX = 0;

    public StateRun(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        imgMap = new MovingBitmap(R.drawable.map);

        Stone stone = new Stone(100, 100);
        stone.initialize();
        objStones.add(stone);

        foreObjectLists.add(objStones);

        imgMap.setLocation(-(imgMap.getWidth() - Game.GAME_FRAME_WIDTH) / 2, 0);
    }

    @Override
    public void move() {
        if (!isGrabbingMap) {
            // if user grab the map over left or right margin, roll back automatically
            if (imgMap.getX() + MAP_LEFT_MARGIN > 0) {
                imgMap.setLocation(imgMap.getX() - 20, 0);
            } else if (imgMap.getX() + imgMap.getWidth() - MAP_RIGHT_MARGIN < Game.GAME_FRAME_WIDTH) {
                imgMap.setLocation(imgMap.getX() + 20, 0);
            }
        }
    }

    @Override
    public void show() {
        // show background image
        imgMap.show();

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
                    if (Common.isInImageScope(singlePointer, gameObject))
                        gameObject.dragPressed(singlePointer);
                }
            }

            // to move background
            initMapX = imgMap.getX();
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
            int newX = initMapX + singlePointer.getX() - initPointerX;
            if (newX <= 0 && newX + imgMap.getWidth() >= Game.GAME_FRAME_WIDTH) {
                imgMap.setLocation(newX, imgMap.getY());
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
                if (gameObject.isDragging()) {
                    gameObject.dragReleased(singlePointer);
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
        imgMap.release();
        imgMap = null;
    }
}
