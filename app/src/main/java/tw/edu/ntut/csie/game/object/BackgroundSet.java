package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.physics.Lib25D;
import tw.edu.ntut.csie.game.util.Constants;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class BackgroundSet extends MovableGameObject {
    public static final int OVERLAP_FOREGROUND = 20;
    public static final int WRAP_WIDTH = 1400;
    public static final int WRAP_HEIGHT = 220;
    private static final int[] DEPTH_TO_FRAME = {400, 200, 50};

    private MovingBitmap[] backImages = new MovingBitmap[3];
    private int foreDeltaX = 0;

    public BackgroundSet() {
        this.width = WRAP_WIDTH;
        this.height = WRAP_HEIGHT;

        backImages[0] = new MovingBitmap(R.drawable.background0);
        backImages[1] = new MovingBitmap(R.drawable.background1);
        backImages[2] = new MovingBitmap(R.drawable.background2);

        int initialX = -(WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center screen
        backImages[0].setLocation(initialX, 100);
        backImages[1].setLocation(initialX, 150);
        backImages[2].setLocation(initialX, 160);
    }

    public BackgroundSet setForeDeltaX(int foreDeltaX) {
        this.foreDeltaX = foreDeltaX;
        return this;
    }

    @Override
    public int getX() {
        return backImages[0].getX();
    }

    @Override
    public int getY() {
        return backImages[0].getY();
    }

    @Override
    public void move() {
        if (foreDeltaX == 0) return;

        for (int i = 0; i < backImages.length; i++) {
            int deltaX = (int) Lib25D.horizontalMoveAdj(
                    Constants.EYE_TO_FRAME_Y,
                    DEPTH_TO_FRAME[i],
                    foreDeltaX
            );
            backImages[i].setLocation(deltaX, backImages[i].getY());
        }
    }

    @Override
    public void show() {
        for (MovingBitmap image : backImages) {
            image.show();
        }
    }

    @Override
    public void dragPressed(Pointer pointer) {

    }

    @Override
    public void dragMoved(Pointer pointer) {
        this.move();
    }

    @Override
    public void dragReleased(Pointer pointer) {
        foreDeltaX = 0;
    }

    @Override
    public void release() {
        for (MovingBitmap image : backImages) {
            image.release();
        }
        backImages = null;
    }
}
