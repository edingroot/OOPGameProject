package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.Audio;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.state.StateRun;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class NotifyBoard extends MovableGameObject {
    private StateRun appStateRun;
    private MovingBitmap image;
    private boolean active = false;
    private Audio activeSound;

    public NotifyBoard(StateRun appStateRun) {
        this.appStateRun = appStateRun;

        image = new MovingBitmap(R.drawable.next);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.setLocation(400, 300);

        activeSound = new Audio(R.raw.sign_appear);
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        image.setLocation(x, y);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            activeSound.play();
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void show() {
        if (active) {
            image.show();
        }
    }

    @Override
    public void clicked(Pointer pointer) {
        if (active) {
            appStateRun.switchToLevel2();
        }
    }

    @Override
    public void release() {
        image.release();
        activeSound.release();
    }
}
