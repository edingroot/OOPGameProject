package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.Pointer;

public abstract class DraggableGameObject implements GameObject {
    protected int x, y;
    protected int initialX, initialY;
    protected int width, height;
    private boolean dragging = false;

    public abstract void initialize();
    public abstract void setLocation(int x, int y);
    public abstract void dragMoved(Pointer pointer);
    public abstract void dragReleased(Pointer pointer);

    public void dragPressed(Pointer pointer) {
        this.initialX = this.x;
        this.initialY = this.y;
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
