package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.physics.Lib25D;

public abstract class MovableGameObject implements GameObject {
    protected int x = 0, y = 0;
    protected int initialX, initialY;
    protected int width, height;
    private boolean dragging = false;

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;

        int newHeight = this.height;
//
//        if (y <= Game.GAME_FRAME_HEIGHT) {
//            newHeight = (int) Lib25D.heightAdj(
//                    Constants.EYE_TO_FRAME_Y,
//                    this.height,
//                    Game.GAME_FRAME_HEIGHT - y
//            );
//        }
        this.resize(this.getWidth(), newHeight);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void moveStarted(Pointer pointer) {
        this.initialX = this.x;
        this.initialY = this.y;
    }

    public void dragPressed(Pointer pointer) {
        this.dragging = true;
        this.moveStarted(pointer);
    }

    public void dragMoved(Pointer pointer) {
    }

    public void dragReleased(Pointer pointer) {
        this.dragging = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}
