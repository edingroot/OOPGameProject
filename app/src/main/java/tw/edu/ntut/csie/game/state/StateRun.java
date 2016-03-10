package tw.edu.ntut.csie.game.state;

import java.util.List;
import java.util.Map;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.Audio;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.extend.Animation;
import tw.edu.ntut.csie.game.extend.Integer;

public class StateRun extends GameState {
    private boolean isGrabbing;

    public StateRun(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {

        isGrabbing = false;
    }

    @Override
    public void move() {

    }

    @Override
    public void show() {

    }

    @Override
    public void release() {

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

        return true;
    }

    @Override
    public boolean pointerMoved(List<Pointer> pointers) {
        if (isGrabbing) {

        }

        return false;
    }

    @Override
    public boolean pointerReleased(List<Pointer> pointers) {
        isGrabbing = false;
        return false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
