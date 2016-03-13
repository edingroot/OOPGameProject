package tw.edu.ntut.csie.game.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.object.BackgroundSet;
import tw.edu.ntut.csie.game.object.Tree;
import tw.edu.ntut.csie.game.physics.Lib25D;
import tw.edu.ntut.csie.game.util.Common;
import tw.edu.ntut.csie.game.util.Constants;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class StateRun extends GameState {
    private final int MAP_LEFT_MARGIN = 180 + Constants.FRAME_LEFT_MARGIN;
    private final int MAP_RIGHT_MARGIN = 150 + Constants.FRAME_RIGHT_MARGIN;

    private MovingBitmap staticBackground;
    private BackgroundSet backgroundSet;
    private MovingBitmap imgFloor;
    // NavigableMap foreObjectTable: index = y-axis of object
    private NavigableMap<Integer, List<MovableGameObject>> foreObjectTable;

    private boolean isGrabbingMap = false;
    private int initForeX = 0;
    private int initPointerX = 0;

    public StateRun(GameEngine engine) {
        super(engine);
        foreObjectTable = new TreeMap<>();
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
        // addToForeObjectTable(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 10, 300));
        // trees
        addToForeObjectTable(new Tree(imgFloor.getX() + MAP_LEFT_MARGIN + 100, 320));
        addToForeObjectTable(new Tree(imgFloor.getX() + MAP_LEFT_MARGIN + 100, 330));
        addToForeObjectTable(new Tree(imgFloor.getX() + MAP_LEFT_MARGIN + 130, 230));
        addToForeObjectTable(new Tree(imgFloor.getX() + MAP_LEFT_MARGIN + 200, 300));
    }

    @Override
    public void move() {
        if (!isGrabbingMap) {
            int foreDeltaX = 0;

            // if user grab the map over left or right margin, roll back automatically
            if (imgFloor.getX() + MAP_LEFT_MARGIN > 0) {
                foreDeltaX = -50;
            } else if (imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN < Game.GAME_FRAME_WIDTH) {
                foreDeltaX = 50;
            }
            backgroundSet.setForeDeltaX(foreDeltaX).move();
            imgFloor.setLocation(imgFloor.getX() + foreDeltaX, imgFloor.getY());

            // move foreground objects with map
            for (MovableGameObject gameObject : getAllForeObjects()) {
                int deltaX25D = calForeObjectHorizontalMove(foreDeltaX, gameObject.getY());
                setForeObjectLocation(gameObject, gameObject.getX() + deltaX25D, gameObject.getY());
            }
        }
    }

    @Override
    public void show() {
        // show back image
        staticBackground.show();
        imgFloor.show();
        backgroundSet.show();

        // show objects in foreObjectLists ordering by Y-axis
        for (MovableGameObject gameObject : getAllForeObjects()) {
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
            for (MovableGameObject gameObject : getAllForeObjects()) {
                gameObject.moveStarted(singlePointer);
                if (Common.isInObjectScope(singlePointer, gameObject))
                    gameObject.dragPressed(singlePointer);
            }

            // for moving map
            backgroundSet.moveStarted();
            initForeX = imgFloor.getX();
            initPointerX = singlePointer.getX();
            isGrabbingMap = true;
        }
        return true;
    }

    @Override
    public boolean pointerMoved(List<Pointer> pointers) {
        Pointer singlePointer = pointers.get(0);
        List<MovableGameObject> foreObjects = getAllForeObjects();

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
                backgroundSet.setForeDeltaX(foreDeltaX).dragMoved();
                imgFloor.setLocation(newForeX, imgFloor.getY());

                // move foreground objects with foreground
                for (MovableGameObject gameObject : foreObjects) {
                    int deltaX25D = calForeObjectHorizontalMove(foreDeltaX, gameObject.getY());
                    setForeObjectLocation(gameObject, gameObject.getInitialX() + deltaX25D, gameObject.getY());
                }
            }
        }

        return false;
    }

    @Override
    public boolean pointerReleased(List<Pointer> pointers) {
        Pointer singlePointer = pointers.get(0);

        backgroundSet.dragReleased();

        // trigger dragReleased event on dragging objects in foreObjectLists
        for (MovableGameObject gameObject : getAllForeObjects()) {
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

        for (Map.Entry<Integer, List<MovableGameObject>> entry : foreObjectTable.entrySet()) {
            List<MovableGameObject> list = entry.getValue();
            for (MovableGameObject gameObject : list) {
                gameObject.release();
            }
            list.clear();
        }
        foreObjectTable.clear();
    }

    private List<MovableGameObject> getAllForeObjects() {
        List<MovableGameObject> listAll = new ArrayList<>();
        for (Map.Entry<Integer, List<MovableGameObject>> entry : foreObjectTable.entrySet()) {
            List<MovableGameObject> list = entry.getValue();
            for (MovableGameObject gameObject : list) {
                listAll.add(gameObject);
            }
        }
        return listAll;
    }

    private void addToForeObjectTable(MovableGameObject gameObject) {
        int y = gameObject.getY();
        // put new item
        List<MovableGameObject> list = foreObjectTable.get(y);
        if (list == null)
            list = new ArrayList<>();
        list.add(gameObject);
        foreObjectTable.put(y, list);
    }

    /**
     * To set location of ANY OBJECT in foreground MUST use this method!
     *
     * @param gameObject
     * @param x
     * @param y
     */
    public void setForeObjectLocation(MovableGameObject gameObject, int x, int y) {
        if (y == gameObject.getY()) {
            gameObject.setLocation(x, y);
            return;
        }
        List<MovableGameObject> list;

        // remove old item
        int py = gameObject.getY();
        list = foreObjectTable.get(py);
        if (list != null)
            list.remove(gameObject);
        foreObjectTable.put(py, list);

        // put new item
        gameObject.setLocation(x, y);
        list = foreObjectTable.get(y);
        if (list == null)
            list = new ArrayList<>();
        list.add(gameObject);
        foreObjectTable.put(y, list);
    }

    private int calForeObjectHorizontalMove(int deltaX, int y) {
        double E = Constants.EYE_TO_FRAME_Y;
        double D = Game.GAME_FRAME_HEIGHT - y;
        return (int) Lib25D.horizontalMoveAdj(E, D, deltaX);
    }
}
