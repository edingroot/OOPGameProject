package tw.edu.ntut.csie.game.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.Audio;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.object.BackgroundLevel1;
import tw.edu.ntut.csie.game.object.BackgroundLevel2;
import tw.edu.ntut.csie.game.object.Cloud;
import tw.edu.ntut.csie.game.object.Grass;
import tw.edu.ntut.csie.game.object.LevelObjectSet1;
import tw.edu.ntut.csie.game.object.LevelObjectSet2;
import tw.edu.ntut.csie.game.object.RightNav;
import tw.edu.ntut.csie.game.object.ScoreBoard;
import tw.edu.ntut.csie.game.object.Sheep;
import tw.edu.ntut.csie.game.physics.Lib25D;
import tw.edu.ntut.csie.game.util.BackgroundSet;
import tw.edu.ntut.csie.game.util.Common;
import tw.edu.ntut.csie.game.util.Constants;
import tw.edu.ntut.csie.game.util.LevelObjectSet;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class StateRun extends GameState {
    public BackgroundSet backgroundSet;
    private LevelObjectSet levelObjectSet;
    public Grass grass;
    public ScoreBoard scoreBoard;
    public boolean isGrabbingMap = false;

    private final int MAP_LEFT_MARGIN = 180 + Constants.FRAME_LEFT_MARGIN;
    private final int MAP_RIGHT_MARGIN = 150 + Constants.FRAME_RIGHT_MARGIN;
    private final int MAP_AUTO_ROLL_RATE = 50;
    private final int SWITCH_LEVEL_DELAY = 3000;
    // NavigableMap foreObjectTable: index = lower-left y-axis of object
    private final Map<Integer, List<MovableGameObject>> foreObjectTable;
    private RightNav rightNav;
    private MovingBitmap imgSwitchingLevel;
    private int initForeX = 0;
    private int initPointerX = 0;
    private int sheepIdCounter = 0;
    private long lastGenCloudTime = 0;
    private long switchingLevelStart = 0; // timestamp of start switching level, when not equals 0: switching level.
    private Audio bgm = new Audio(R.raw.ambience_mountains);

    public StateRun(GameEngine engine) {
        super(engine);
        foreObjectTable = new TreeMap<>();
    }

    @Override
    public void initialize(Map<String, Object> data) {
        // ---------- set back objects ----------
        backgroundSet = new BackgroundLevel1();
        levelObjectSet = new LevelObjectSet1(this, MAP_LEFT_MARGIN, MAP_RIGHT_MARGIN);
        scoreBoard = new ScoreBoard();
        rightNav = new RightNav(scoreBoard.getHeight());

        // ---------- game objects ----------
        levelObjectSet.addObjects();
        addToForeObjectTable(new Cloud(this, 10, 0, Cloud.TYPE_GRAY, Cloud.LEVEL_BIG, true));
        addToForeObjectTable(new Cloud(this, 100, 10, Cloud.TYPE_WHITE, Cloud.LEVEL_MEDIUM, true));
        //grass
        grass = new Grass(backgroundSet.imgGround.getX() + MAP_LEFT_MARGIN + 380, 280);
        addToForeObjectTable(grass);

        imgSwitchingLevel = new MovingBitmap(R.drawable.changing_level);
    }

    @Override
    public void move() {
        bgm.resume();
        genCloudsRandomly();

        for (MovableGameObject gameObject : getAllForeObjects()) {
            gameObject.move();
        }

        if (!isGrabbingMap) {
            int foreDeltaX = 0;

            // if user grab the map over left or right margin, roll back automatically
            if (backgroundSet.imgGround.getX() + MAP_LEFT_MARGIN > 0) {
                int diff = backgroundSet.imgGround.getX() + MAP_LEFT_MARGIN;
                foreDeltaX = -(diff >= MAP_AUTO_ROLL_RATE ? MAP_AUTO_ROLL_RATE : diff);
            } else if (backgroundSet.imgGround.getX() + backgroundSet.imgGround.getWidth() - MAP_RIGHT_MARGIN < Game.GAME_FRAME_WIDTH) {
                int diff = Game.GAME_FRAME_WIDTH - (backgroundSet.imgGround.getX() + backgroundSet.imgGround.getWidth() - MAP_RIGHT_MARGIN);
                foreDeltaX = diff >= MAP_AUTO_ROLL_RATE ? MAP_AUTO_ROLL_RATE : diff;
            }
            backgroundSet.setForeDeltaX(foreDeltaX).move();
            backgroundSet.imgGround.setLocation(backgroundSet.imgGround.getX() + foreDeltaX, backgroundSet.imgGround.getY());

            // move foreground objects with map
            Iterator<MovableGameObject> it = getAllForeObjects().iterator();
            while (it.hasNext()) {
                MovableGameObject gameObject = it.next();
                int deltaX25D = calForeObjectHorizontalMove(foreDeltaX, gameObject.getY());
                int x = gameObject.getX() + deltaX25D;
                gameObject.setLocation(x, gameObject.getY());

                // release fore objects if it's out of map bounds
                if (Common.isOutOfBounds(gameObject, backgroundSet.imgGround.getX(), BackgroundLevel1.WRAP_WIDTH, Game.GAME_FRAME_HEIGHT)) {
                    if (gameObject.getClass().getName().equals("Cloud")) {
                        System.out.println("Release out of bounds cloud: " + gameObject.getClass().getSimpleName());
                        removeFromForeObjectTable(gameObject);
                        gameObject.release();
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
        backgroundSet.show();

        // show objects in foreObjectLists ordering by Y-axis
        for (MovableGameObject gameObject : getAllForeObjects()) {
            gameObject.show();
        }

        scoreBoard.show();
        rightNav.show();

        if (switchingLevelStart != 0) {
            if (System.currentTimeMillis() - switchingLevelStart > SWITCH_LEVEL_DELAY) {
                switchingLevelStart = 0;
            } else {
                imgSwitchingLevel.show();
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
            isGrabbingMap = true;

            List<MovableGameObject> inScopeObjects = new ArrayList<>();
            // check is dragging of objects in foreObjectLists
            for (MovableGameObject gameObject : getAllForeObjects()) {
                gameObject.moveStarted(singlePointer);
                if (gameObject.isDraggable() && Common.isInObjectScope(singlePointer, gameObject)) {
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
            initForeX = backgroundSet.imgGround.getX();
            initPointerX = singlePointer.getX();
        }
        return true;
    }

    @Override
    public boolean pointerMoved(List<Pointer> pointers) {
        Pointer singlePointer = pointers.get(0);
        int deltaX = singlePointer.getX() - initPointerX;

        // trigger dragMoved event on dragging objects in foreObjectLists
        for (MovableGameObject gameObject : getAllForeObjects()) {
            if (gameObject.isDragging())
                gameObject.dragMoved(singlePointer);
        }

        // moving background_static
        if (isGrabbingMap) {
            int newForeX = initForeX + deltaX;
            if (newForeX < 0 - Constants.FRAME_LEFT_MARGIN &&
                    newForeX + backgroundSet.imgGround.getWidth() > Game.GAME_FRAME_WIDTH + Constants.FRAME_RIGHT_MARGIN) {
                backgroundSet.setForeDeltaX(deltaX).dragMoved();
                backgroundSet.imgGround.setLocation(newForeX, backgroundSet.imgGround.getY());

                // move foreground objects with foreground
                for (MovableGameObject gameObject : getAllForeObjects()) {
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
        levelObjectSet.release();

        for (Map.Entry<Integer, List<MovableGameObject>> entry : foreObjectTable.entrySet()) {
            List<MovableGameObject> list = entry.getValue();
            for (MovableGameObject gameObject : list) {
                gameObject.release();
            }
            list.clear();
        }
        foreObjectTable.clear();
        bgm.release();
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
        boolean found = false;
        int py = gameObject.getY25D() + gameObject.getHeight();
        List<MovableGameObject> list = foreObjectTable.get(py);

        if (list != null) {
            Iterator<MovableGameObject> it = list.iterator();
            while (it.hasNext()) {
                MovableGameObject obj = it.next();
                if (obj == gameObject) {
                    it.remove();
                    found = true;
                }
            }
            if (list.size() == 0)
                foreObjectTable.remove(py);
        }

        if (!found) {
            System.out.printf("removeFromForeObjectTable: object not found, className=%s\n", gameObject.getClass().getName());
        }
    }

    /**
     * To set location of ANY OBJECT in foreground MUST use this method AFTER updated self-location
     *
     * @param gameObject
     */
    public void updateForeObjectLocation(MovableGameObject gameObject) {
        synchronized (foreObjectTable) {
            // resize
            double ratio = Lib25D.sizeAdj(gameObject.getY25D());
            gameObject.resize(ratio);

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
        }
    }

    public void addScore(int amount) {
        scoreBoard.addScore(amount);
        if (scoreBoard.score >= 100) {
            switch (backgroundSet.getLevel()) {
                case 1:
                    switchToLevel2();
                    break;
                case 2:
                    changeState(Game.OVER_STATE);
                    break;
            }
        }
    }

    private void switchToLevel2() {
        // remove all sheep
        for (MovableGameObject gameObject : getAllForeObjects()) {
            if (gameObject.getClass().getSimpleName().equals("Sheep")) {
                removeFromForeObjectTable(gameObject);
                gameObject.release();
            }
        }
        backgroundSet.release();
        backgroundSet = new BackgroundLevel2();
        levelObjectSet.release();
        levelObjectSet = new LevelObjectSet2(this, MAP_LEFT_MARGIN, MAP_RIGHT_MARGIN);
        levelObjectSet.addObjects();
        scoreBoard.score = 0;

        switchingLevelStart = System.currentTimeMillis();
    }

    private int calForeObjectHorizontalMove(int deltaX, int y) {
        double D = Game.GAME_FRAME_HEIGHT - y;
        return (int) Lib25D.horizontalMoveAdj(D, deltaX);
    }

    private void genCloudsRandomly() {
        if (System.currentTimeMillis() - lastGenCloudTime > (10 + Math.random() * 6) * 1000) {
            boolean direction = (Math.random() * 2 > 1);
            addToForeObjectTable(new Cloud(
                    this,
                    backgroundSet.imgGround.getX() + (direction ? -120 : backgroundSet.imgGround.getWidth() + 120),
                    (int) (Math.random() * 20),
                    (int) (Math.random() * 3) + 1,
                    (int) (Math.random() * 2) + 1,
                    direction
            ));
            lastGenCloudTime = System.currentTimeMillis();
        }
    }
}
