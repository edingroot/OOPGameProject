package tw.edu.ntut.csie.game.state;

import java.util.Map;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.Audio;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.extend.BitmapButton;
import tw.edu.ntut.csie.game.extend.ButtonEventHandler;

public class StateReady extends AbstractGameState {

    private MovingBitmap _background;
    private BitmapButton _startButton, _aboutButton, _exitButton;
    private Audio bgm = new Audio(R.raw.menu_loop);
    public StateReady(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        bgm.resume();
        addGameObject(_background = new MovingBitmap(R.drawable.state_ready_bg));
        initializeStartButton();
        initializeAboutButton();
        initializeExitButton();
    }

    private void initializeStartButton() {
        addGameObject(_startButton = new BitmapButton(R.drawable.btn_start, R.drawable.btn_start_pressed, 370, 108));
        _startButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                bgm.stop();
                bgm.release();
                changeState(Game.RUNNING_STATE);
            }
        });
        addPointerEventHandler(_startButton);
    }

    private void initializeAboutButton() {
        addGameObject(_aboutButton = new BitmapButton(R.drawable.btn_about, R.drawable.btn_about_pressed, 355, 233));
        _aboutButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {

            }
        });
        addPointerEventHandler(_aboutButton);
    }

    private void initializeExitButton() {
//        addGameObject(_exitButton = new BitmapButton(R.drawable.btn_exit, R.drawable.btn_exit_pressed, 388, 243));
//        _exitButton.addButtonEventHandler(new ButtonEventHandler() {
//            @Override
//            public void perform(BitmapButton button) {
//                _engine.exit();
//            }
//        });
//        addPointerEventHandler(_exitButton);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}

