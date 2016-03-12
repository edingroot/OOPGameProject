package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.physics.Lib25D;
import tw.edu.ntut.csie.game.util.Constants;

public class BackgroundSet implements GameObject {
    public static final int OVERLAP_FOREGROUND = 20;
    public static final int WRAP_WIDTH = 1400;
    public static final int WRAP_HEIGHT = 220;
    private static final int[] DEPTH_TO_FRAME = {400, 200, 50};

    private MovingBitmap[] backImages = new MovingBitmap[3];
    private int[] initialX = new int[3];
    private int foreDeltaX = 0;



    public BackgroundSet() {
        backImages[0] = new MovingBitmap(R.drawable.background0);
        backImages[1] = new MovingBitmap(R.drawable.background1);
        backImages[2] = new MovingBitmap(R.drawable.background2);

        int px = -(WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center screen
        backImages[0].setLocation(px, 100);
        backImages[1].setLocation(px, 150);
        backImages[2].setLocation(px, 160);
    }

    public BackgroundSet setForeDeltaX(int foreDeltaX) {
        this.foreDeltaX = foreDeltaX;
        return this;
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
            backImages[i].setLocation(backImages[i].getX() + deltaX, backImages[i].getY());
        }
    }

    @Override
    public void show() {
        for (MovingBitmap image : backImages) {
            image.show();
        }
    }

    public void moveStarted() {
        for (int i = 0; i < backImages.length; i++) {
            initialX[i] = backImages[i].getX();
        }
    }

    public void dragMoved() {
        if (foreDeltaX == 0) return;

        for (int i = 0; i < backImages.length; i++) {
            int deltaX = (int) Lib25D.horizontalMoveAdj(
                    Constants.EYE_TO_FRAME_Y,
                    DEPTH_TO_FRAME[i],
                    foreDeltaX
            );
            backImages[i].setLocation(initialX[i] + deltaX, backImages[i].getY());
        }
    }

    public void dragReleased() {
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
