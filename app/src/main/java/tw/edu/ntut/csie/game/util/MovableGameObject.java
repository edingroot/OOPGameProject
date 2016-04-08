package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.Pointer;

public abstract class MovableGameObject implements GameObject {
    protected int x = 0, y = 0, moveX = 0, moveY = 0;
    protected int initialX, initialY;
    protected int width, height;
    protected boolean dragging = false;

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void moveStarted(Pointer pointer) {
        this.initialX = this.x;
        this.initialY = this.y;
        this.moveX = this.x;
        this.moveY = this.y;
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

    public int getDeltaX() { return moveX - initialX; }

    public int getDeltaY() { return moveY - initialY; }

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
