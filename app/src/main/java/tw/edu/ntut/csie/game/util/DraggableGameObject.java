package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.Pointer;

public abstract class DraggableGameObject implements GameObject {
    protected int x = 0, y = 0;
    protected int initialX, initialY;
    protected int width, height;
    private boolean dragging = false;

    public abstract void dragMoved(Pointer pointer);
    public abstract void dragReleased(Pointer pointer);

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

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
