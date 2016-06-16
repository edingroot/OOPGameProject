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
    private MovingBitmap _background, _backgroundAbout;
    private BitmapButton _startButton, _aboutButton, _returnButton, _exitButton;
    private Audio bgm;

    public StateReady(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        bgm = new Audio(R.raw.menu_loop);
        bgm.setRepeating(true);
        bgm.play();

        _background = new MovingBitmap(R.drawable.state_ready_bg);
        _backgroundAbout = new MovingBitmap(R.drawable.about_game);
        addGameObject(_background);
        addGameObject(_backgroundAbout);
        initializeStartButton();
        initializeAboutButton();
        initializeReturnButton();
        initializeExitButton();
        activateAboutMode(false);
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
                activateAboutMode(true);
            }
        });
        addPointerEventHandler(_aboutButton);
    }

    private void initializeReturnButton() {
        addGameObject(_returnButton = new BitmapButton(R.drawable.return_button, R.drawable.return_button, 560, 300));
        _returnButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                activateAboutMode(false);
            }
        });
        addPointerEventHandler(_returnButton);
    }

    private void initializeExitButton() {
        addGameObject(_exitButton = new BitmapButton(R.drawable.exit_button, R.drawable.exit_button, 590, 10));
        _exitButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                _engine.exit();
            }
        });
        addPointerEventHandler(_exitButton);
    }

    @Override
    public void pause() {
        bgm.pause();
    }

    @Override
    public void resume() {
        bgm.resume();
    }

    @Override
    public void release() {
        super.release();
        bgm.release();
    }
    
    private void activateAboutMode(boolean active) {
        _background.setVisible(!active);
        _backgroundAbout.setVisible(active);

        _startButton.setVisible(!active);
        _aboutButton.setVisible(!active);
        _returnButton.setVisible(active);
        _exitButton.setVisible(!active);
    }
}
