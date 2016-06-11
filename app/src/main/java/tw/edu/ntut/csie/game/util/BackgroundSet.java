package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.physics.Lib25D;

public abstract class BackgroundSet implements GameObject {
    public static final int OVERLAP_FOREGROUND = 20;
    public static final int WRAP_WIDTH = 1100;
    public static final int WRAP_HEIGHT = 220;
    public MovingBitmap imgGround;
    protected MovingBitmap staticBackground; // the deepest background_static
    protected static final int[] DEPTH_TO_FRAME = {400, 250, 220}; // shouldn't lower than WRAP_HEIGHT
    protected MovingBitmap[] backImages = new MovingBitmap[3];
    protected int[] initialX = new int[3];
    protected int foreDeltaX = 0;
    protected static int level = 0; // ** NEEDED TO BE SET **

    public BackgroundSet() {
        /*
        Constructor sample:
            level = 1;

            int initialX = -(WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center of screen
            staticBackground = new MovingBitmap(R.drawable.background_static);
            staticBackground.setLocation(initialX, 0);

            imgGround = new MovingBitmap(R.drawable.ground);
            imgGround.setLocation(
                    -(imgGround.getWidth() - Game.GAME_FRAME_WIDTH) / 2,
                    BackgroundLevel1.WRAP_HEIGHT - BackgroundLevel1.OVERLAP_FOREGROUND
            );

            backImages[0] = new MovingBitmap(R.drawable.background0);
            backImages[1] = new MovingBitmap(R.drawable.background1);
            backImages[2] = new MovingBitmap(R.drawable.background2);

            int px = -(WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center screen
            backImages[0].setLocation(px, 120);
            backImages[1].setLocation(px, 170);
            backImages[2].setLocation(px, 180);
        */
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
                    DEPTH_TO_FRAME[i],
                    foreDeltaX
            );
            backImages[i].setLocation(backImages[i].getX() + deltaX, backImages[i].getY());
        }
    }

    @Override
    public void show() {
        staticBackground.show();
        imgGround.show();
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
        staticBackground.release();
        imgGround.release();
        for (MovingBitmap image : backImages) {
            image.release();
        }
        backImages = null;
    }

    public int getLevel() {
        return level;
    }
}
