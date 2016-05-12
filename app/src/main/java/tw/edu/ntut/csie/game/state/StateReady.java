package tw.edu.ntut.csie.game.state;

import java.util.Map;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.extend.BitmapButton;
import tw.edu.ntut.csie.game.extend.ButtonEventHandler;

public class StateReady extends AbstractGameState {

    private MovingBitmap _background;
    private BitmapButton _startButton;
    private BitmapButton _exitButton;

    public StateReady(GameEngine engine) {
        super(engine);
    }

    @Override
    public void initialize(Map<String, Object> data) {
        addGameObject(_background = new MovingBitmap(R.drawable.state_ready_bg));
        initializeStartButton();
        initializeExitButton();
    }

    private void initializeStartButton() {
        addGameObject(_startButton = new BitmapButton(R.drawable.btn_start, R.drawable.btn_start_pressed, 370, 108));
        _startButton.addButtonEventHandler(new ButtonEventHandler() {
            @Override
            public void perform(BitmapButton button) {
                changeState(Game.RUNNING_STATE);
            }
        });
        addPointerEventHandler(_startButton);
    }

    private void initializeExitButton() {
        addGameObject(_exitButton = new BitmapButton(R.drawable.btn_exit, R.drawable.btn_exit_pressed, 388, 243));
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
    }

    @Override
    public void resume() {
    }
}

