package tw.edu.ntut.csie.game.state;

import java.util.List;
import java.util.Map;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;

public class StateRun extends GameState {
    private final int MAP_LEFT_MARGIN = 230;
    private final int MAP_RIGHT_MARGIN = 200;

    private MovingBitmap imgMap;

    private boolean isGrabbingMap = false;
    private int initMapX = 0;
    private int initPointerX = 0;

    public StateRun(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        imgMap = new MovingBitmap(R.drawable.map);
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
        imgMap.show();
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
            int touchX = pointers.get(0).getX();
            int touchY = pointers.get(0).getY();

            initMapX = imgMap.getX();
            initPointerX = touchX;
            isGrabbingMap = true;
        }
        return true;
    }

    @Override
    public boolean pointerMoved(List<Pointer> pointers) {
        if (isGrabbingMap) {
            int newX = initMapX + pointers.get(0).getX() - initPointerX;
            if (newX <= 0 && newX + imgMap.getWidth() >= Game.GAME_FRAME_WIDTH) {
                imgMap.setLocation(newX, imgMap.getY());
            }
        }

        return false;
    }

    @Override
    public boolean pointerReleased(List<Pointer> pointers) {
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
