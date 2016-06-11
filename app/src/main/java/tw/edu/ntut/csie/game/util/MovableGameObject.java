package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.Pointer;

public abstract class MovableGameObject implements GameObject {
    protected int x = 0, y = 0, moveX = 0, moveY = 0;
    protected int initialX, initialY, initialPointerX, initialPointerY;
    protected int width, height;
    protected boolean draggable = true;
    protected boolean dragging = false;
    private long dragPressedMillis = 0;
    private int dragMovedTimes = 0;

    public boolean isDraggable() {
        return draggable;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void resize(double ratio) {
        this.width = (int) (this.width * ratio);
        this.height = (int) (this.height * ratio);
    }

    public void moveStarted(Pointer pointer) {
        this.initialX = this.x;
        this.initialY = this.y;
        this.initialPointerX = pointer.getX();
        this.initialPointerY = pointer.getY();
        this.moveX = this.x;
        this.moveY = this.y;
    }

    public void dragPressed(Pointer pointer) {
        this.dragging = true;
        this.dragPressedMillis = System.currentTimeMillis();
        this.dragMovedTimes = 0;
        this.moveStarted(pointer);
    }

    public void dragMoved(Pointer pointer) {
        this.dragMovedTimes++;
    }

    public void dragReleased(Pointer pointer) {
        this.dragging = false;
        long millis = System.currentTimeMillis();
        if (this.dragMovedTimes < 10 && Math.abs(millis - dragPressedMillis) < 300)
            clicked(pointer);
    }

    public void clicked(Pointer pointer) {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * return y for determining how far the object is
     *
     * @return int y25d
     */
    public int getY25D() {
        return y;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public int getInitialPointerX() {
        return initialPointerX;
    }

    public int getInitialPointerY() {
        return initialPointerY;
    }

    public int getDeltaX() {
        return moveX - initialX;
    }

    public int getDeltaY() {
        return moveY - initialY;
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
