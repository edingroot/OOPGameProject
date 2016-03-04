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
    public static final int DEFAULT_SCORE_DIGITS = 4;
    private MovingBitmap _background;
    private MovingBitmap _android;
    private MovingBitmap _cloud;
    private MovingBitmap _door;
    private MovingBitmap _message;

    private Animation _flower;

    private Integer _scores;

    private boolean _grab;
    private int _cx, _cy;

    private Audio _music;

    public StateRun(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        _background = new MovingBitmap(R.drawable.background);
        _message = new MovingBitmap(R.drawable.message, 130, 150);

        _android = new MovingBitmap(R.drawable.android_green);
        _android.setLocation(100, 200);

        _cloud = new MovingBitmap(R.drawable.cloud);
        _cx = 100;
        _cy = 50;
        _cloud.setLocation(_cx, _cy);

        _door = new MovingBitmap(R.drawable.door);
        _door.setLocation(300, 200);

        _scores = new Integer(DEFAULT_SCORE_DIGITS, 50, 550, 10);

        _flower = new Animation();
        _flower.setLocation(560, 310);
        _flower.addFrame(R.drawable.flower1);
        _flower.addFrame(R.drawable.flower2);
        _flower.addFrame(R.drawable.flower3);
        _flower.addFrame(R.drawable.flower4);
        _flower.addFrame(R.drawable.flower5);
        _flower.setDelay(2);

        _music = new Audio(R.raw.ntut);
        _music.setRepeating(true);
        _music.play();

        _grab = false;
    }

    @Override
    public void move() {
        _flower.move();
        _cloud.setLocation(_cx, _cy);
    }

    @Override
    public void show() {
        // �I�s���Ǭ��K�϶���
        _background.show();
        _scores.show();
        _flower.show();
        _message.show();
        _cloud.show();
        _door.show();
        _android.show();
    }

    @Override
    public void release() {
        _background.release();
        _scores.release();
        _android.release();
        _flower.release();
        _message.release();
        _cloud.release();
        _music.release();
        _door.release();

        _background = null;
        _scores = null;
        _android = null;
        _flower = null;
        _message = null;
        _cloud = null;
        _music = null;
        _door = null;
    }

    @Override
    public void keyPressed(int keyCode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(int keyCode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void orientationChanged(float pitch, float azimuth, float roll) {
        if (roll > 15 && roll < 60 && _cx > 50)
            _cx -= 2;
        if (roll < -15 && roll > -60 && _cx + _cloud.getWidth() < 500)
            _cx += 2;
    }

    @Override
    public void accelerationChanged(float dX, float dY, float dZ) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean pointerPressed(List<Pointer> pointers) {
        _message.setVisible(false);
        if (pointers.size() == 1) {
            int touchX = pointers.get(0).getX();
            int touchY = pointers.get(0).getY();
            if (touchX > _android.getX() && touchX < _android.getX() + _android.getWidth() &&
                    touchY > _android.getY() && touchY < _android.getY() + _android.getHeight()) {
                _grab = true;
            } else {
                _grab = false;
            }
        }
        return true;
    }

    @Override
    public boolean pointerMoved(List<Pointer> pointers) {
        if (_grab)
            _android.setLocation(pointers.get(0).getX() - _android.getWidth() / 2, pointers.get(0).getY() - _android.getHeight() / 2);
        int moveX = _android.getX();
        int moveY = _android.getY();
        if (moveX + _android.getWidth() / 2 > _door.getX() && moveX < _door.getX() + _door.getWidth() / 2 &&
                moveY + _android.getHeight() / 2 > _door.getY() && moveY < _door.getY() + _door.getHeight() / 2)
            changeState(Game.OVER_STATE);
        return false;
    }

    @Override
    public boolean pointerReleased(List<Pointer> pointers) {
        _grab = false;
        return false;
    }

    @Override
    public void pause() {
        _music.pause();
    }

    @Override
    public void resume() {
        _music.resume();
    }
}
