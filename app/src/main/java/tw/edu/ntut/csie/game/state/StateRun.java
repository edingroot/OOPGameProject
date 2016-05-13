package tw.edu.ntut.csie.game.state;

import java.util.ArrayList;
import java.util.Iterator;
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
import tw.edu.ntut.csie.game.object.Bush;
import tw.edu.ntut.csie.game.object.Cloud;
import tw.edu.ntut.csie.game.object.Grass;
import tw.edu.ntut.csie.game.object.RightNav;
import tw.edu.ntut.csie.game.object.Rock;
import tw.edu.ntut.csie.game.object.ScoreBoard;
import tw.edu.ntut.csie.game.object.Sheep;
import tw.edu.ntut.csie.game.object.Stone;
import tw.edu.ntut.csie.game.object.Tree;
import tw.edu.ntut.csie.game.physics.Lib25D;
import tw.edu.ntut.csie.game.util.Common;
import tw.edu.ntut.csie.game.util.Constants;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class StateRun extends GameState {
    private final int MAP_LEFT_MARGIN = 180 + Constants.FRAME_LEFT_MARGIN;
    private final int MAP_RIGHT_MARGIN = 150 + Constants.FRAME_RIGHT_MARGIN;
    private final int MAP_AUTO_ROLL_RATE = 50;

    public boolean isGrabbingMap = false;

    private MovingBitmap staticBackground;
    private BackgroundSet backgroundSet;
    private MovingBitmap imgFloor;
    // NavigableMap foreObjectTable: index = lower-left y-axis of object
    private final NavigableMap<Integer, List<MovableGameObject>> foreObjectTable;
    private ScoreBoard scoreBoard;
    private RightNav rightNav;

    public Grass grass;

    private int initForeX = 0;
    private int initPointerX = 0;
    private int sheepIdCounter = 0;

    public StateRun(GameEngine engine) {
        super(engine);

        foreObjectTable = new TreeMap<>();
    }

    @Override
    public void initialize(Map<String, Object> data) {
        // ---------- set back images ----------
        int initialX = -(BackgroundSet.WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center screen
        staticBackground = new MovingBitmap(R.drawable.background);
        staticBackground.setLocation(initialX, 0);

        backgroundSet = new BackgroundSet();
        imgFloor = new MovingBitmap(R.drawable.floor);
        imgFloor.setLocation(
                -(imgFloor.getWidth() - Game.GAME_FRAME_WIDTH) / 2,
                BackgroundSet.WRAP_HEIGHT - BackgroundSet.OVERLAP_FOREGROUND
        );

        scoreBoard = new ScoreBoard();
        rightNav = new RightNav(scoreBoard.getHeight());


        // ---------- game objects ----------
        // clouds
        addToForeObjectTable(new Cloud(this, 10, 0, Cloud.TYPE_GRAY, Cloud.LEVEL_BIG));
        addToForeObjectTable(new Cloud(this, 100, 10, Cloud.TYPE_WHITE, Cloud.LEVEL_MEDIUM));
        // stones
        addToForeObjectTable(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 45, 240));
        addToForeObjectTable(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 30, 280));
        addToForeObjectTable(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 15, 320));
        addToForeObjectTable(new Stone(imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN - 95, 240));
        addToForeObjectTable(new Stone(imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN - 80, 280));
        addToForeObjectTable(new Stone(imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN - 65, 320));
        // trees
        addToForeObjectTable(new Rock(imgFloor.getX() + MAP_LEFT_MARGIN + 200, 170));
        addToForeObjectTable(new Bush(imgFloor.getX() + MAP_LEFT_MARGIN + 240, 250));
        addToForeObjectTable(new Tree(imgFloor.getX() + MAP_LEFT_MARGIN + 600, 190));
        //addToForeObjectTable(new Tree(imgFloor.getX() + MAP_LEFT_MARGIN + 410, 220));
        // sheep
        addToForeObjectTable(new Sheep(this, imgFloor.getX() + MAP_LEFT_MARGIN + 500, 250, sheepIdCounter++));
        addToForeObjectTable(new Sheep(this, imgFloor.getX() + MAP_LEFT_MARGIN + 100, 211, sheepIdCounter++));
        //grass
        grass = new Grass(imgFloor.getX() + MAP_LEFT_MARGIN + 380, 280);
        addToForeObjectTable(grass);


    }

    @Override
    public void move() {
        List<MovableGameObject> foreObjects = getAllForeObjects();
        for (MovableGameObject gameObject : foreObjects) {
            gameObject.move();
        }

        if (!isGrabbingMap) {
            int foreDeltaX = 0;

            // if user grab the map over left or right margin, roll back automatically
            if (imgFloor.getX() + MAP_LEFT_MARGIN > 0) {
                int diff = imgFloor.getX() + MAP_LEFT_MARGIN;
                foreDeltaX = -(diff >= MAP_AUTO_ROLL_RATE ? MAP_AUTO_ROLL_RATE : diff);
            } else if (imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN < Game.GAME_FRAME_WIDTH) {
                int diff = Game.GAME_FRAME_WIDTH - (imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN);
                foreDeltaX = diff >= MAP_AUTO_ROLL_RATE ? MAP_AUTO_ROLL_RATE : diff;
            }
            backgroundSet.setForeDeltaX(foreDeltaX).move();
            imgFloor.setLocation(imgFloor.getX() + foreDeltaX, imgFloor.getY());

            // move foreground objects with map
            Iterator<MovableGameObject> it = foreObjects.iterator();
            while (it.hasNext()) {
                MovableGameObject gameObject = it.next();
                int deltaX25D = calForeObjectHorizontalMove(foreDeltaX, gameObject.getY());
                int x = gameObject.getX() + deltaX25D;
                gameObject.setLocation(x, gameObject.getY());

                // release fore objects if it's out of map bounds
                if (Common.isOutOfBounds(gameObject, imgFloor.getX(), BackgroundSet.WRAP_WIDTH, Game.GAME_FRAME_HEIGHT)) {
                    if (gameObject.getClass().getName().equals("Cloud")) {
                        System.out.println("Release out of bounds cloud: " + gameObject.getClass().getSimpleName());
                        removeFromForeObjectTable(gameObject);
                        it.remove();
                    }
                }
            }
        }

        scoreBoard.move();
        rightNav.move();
    }

    @Override
    public void show() {
        // show back image
        staticBackground.show();
        imgFloor.show();
        backgroundSet.show();

        // show objects in foreObjectLists ordering by Y-axis
        for (MovableGameObject gameObject : getAllForeObjects()){

            gameObject.show();
        }

        scoreBoard.show();
        rightNav.show();
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
            isGrabbingMap = true;

            List<MovableGameObject> inScopeObjects = new ArrayList<>();
            // check is dragging of objects in foreObjectLists
            for (MovableGameObject gameObject : getAllForeObjects()) {
                gameObject.moveStarted(singlePointer);
                if (gameObject.isDraggable() && Common.isInObjectScope(singlePointer, gameObject)){
                    inScopeObjects.add(gameObject);
                }
            }

            if (Common.isInObjectScope(singlePointer, rightNav)) {
                isGrabbingMap = false;
                if (rightNav.isExpanded() && Common.isInObjectScope(
                        singlePointer, rightNav.getX(), rightNav.getY(),
                        rightNav.getWidth(), rightNav.getHeight() - 20)) {
                    // handle drag sheep out from right nav
                    Sheep sheep = new Sheep(this, singlePointer.getX(), singlePointer.getY(), sheepIdCounter++);
                    addToForeObjectTable(sheep);
                    sheep.dragPressed(singlePointer);
                } else {
                    // toggle right nav expand state
                    rightNav.dragPressed(singlePointer);
                }
            } else {
                if (inScopeObjects.size() > 0) {
                    // trigger dragPressed on the most top object
                    inScopeObjects.get(inScopeObjects.size() - 1).dragPressed(singlePointer);
                    isGrabbingMap = false;
                }
            }

            // for moving map
            backgroundSet.moveStarted();
            initForeX = imgFloor.getX();
            initPointerX = singlePointer.getX();
        }
        return true;
    }

    @Override
    public boolean pointerMoved(List<Pointer> pointers) {
        Pointer singlePointer = pointers.get(0);
        List<MovableGameObject> foreObjects = getAllForeObjects();
        int deltaX = singlePointer.getX() - initPointerX;

        // trigger dragMoved event on dragging objects in foreObjectLists
        for (MovableGameObject gameObject : foreObjects) {
            if (gameObject.isDragging())
                gameObject.dragMoved(singlePointer);
        }

        // moving background
        if (isGrabbingMap) {
            int newForeX = initForeX + deltaX;
            if (newForeX < 0 - Constants.FRAME_LEFT_MARGIN &&
                    newForeX + imgFloor.getWidth() > Game.GAME_FRAME_WIDTH + Constants.FRAME_RIGHT_MARGIN) {
                backgroundSet.setForeDeltaX(deltaX).dragMoved();
                imgFloor.setLocation(newForeX, imgFloor.getY());

                // move foreground objects with foreground
                for (MovableGameObject gameObject : foreObjects) {
                    int deltaX25D = calForeObjectHorizontalMove(deltaX, gameObject.getY());
                    int x = gameObject.getInitialX() + deltaX25D + gameObject.getDeltaX();
                    gameObject.setLocation(x, gameObject.getY());
                }
            }
        }

        rightNav.dragMoved(singlePointer);
        return false;
    }

    @Override
    public boolean pointerReleased(List<Pointer> pointers) {
        Pointer singlePointer = pointers.get(0);

        backgroundSet.dragReleased();

        // trigger dragReleased event on dragging objects in foreObjectLists
        for (MovableGameObject gameObject : getAllForeObjects()) {
            if (gameObject.isDragging())
                gameObject.dragReleased(singlePointer);
        }

        rightNav.dragReleased(singlePointer);
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

        scoreBoard.release();
        rightNav.release();
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

    public void addToForeObjectTable(MovableGameObject gameObject) {
        updateForeObjectLocation(gameObject);
     }

    public void removeFromForeObjectTable(MovableGameObject gameObject) {
        int py = gameObject.getY25D() + gameObject.getHeight();
        List<MovableGameObject> list = foreObjectTable.get(py);
        if (list != null) {
            Iterator<MovableGameObject> it = list.iterator();
            while (it.hasNext()) {
                MovableGameObject obj = it.next();
                if (obj == gameObject)
                    it.remove();
            }
            if (list.size() == 0)
                foreObjectTable.remove(py);
        }
        gameObject.release();
        gameObject = null;
    }

    /**
     * To set location of ANY OBJECT in foreground MUST use this method AFTER updated self-location
     *
     * @param gameObject
     */
    public void updateForeObjectLocation(MovableGameObject gameObject) {
        synchronized (foreObjectTable) {
            int py = gameObject.getY25D() + gameObject.getHeight();
            int originalY = -1;
            for (Map.Entry<Integer, List<MovableGameObject>> entry : foreObjectTable.entrySet()) {
                List<MovableGameObject> list = entry.getValue();
                for (MovableGameObject object : list) {
                    if (object == gameObject) {
                        originalY = entry.getKey();
                    }
                }
                if (originalY != -1)
                    break;
            }

            if (originalY == py)
                return;

            // remove old item if exists
            if (originalY != -1) {
                List<MovableGameObject> oldList = foreObjectTable.get(originalY);
                oldList.remove(gameObject);
                if (oldList.size() == 0)
                    foreObjectTable.remove(originalY);
                else
                    foreObjectTable.put(originalY, oldList);
            }

            // put new item
            List<MovableGameObject> newList = foreObjectTable.get(py);
            if (newList == null)
                newList = new ArrayList<>();
            newList.add(gameObject);
            foreObjectTable.put(py, newList);

            // resize
            double ratio = Lib25D.sizeAdj(gameObject.getY25D());
            gameObject.resize(ratio);
        }
    }

    private int calForeObjectHorizontalMove(int deltaX, int y) {
        double D = Game.GAME_FRAME_HEIGHT - y;
        return (int) Lib25D.horizontalMoveAdj(D, deltaX);
    }
}
