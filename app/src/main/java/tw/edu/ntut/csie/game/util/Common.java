package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.Pointer;

public class Common {

    public static boolean isInObjectScope(Pointer pointer, MovableGameObject object) {
        return (pointer.getX() > object.getX() && pointer.getX() < object.getX() + object.getWidth() &&
                pointer.getY() > object.getY() && pointer.getY() < object.getY() + object.getHeight());
    }

    public static boolean isInObjectScope(Pointer pointer, int x, int y, int width, int height) {
        return (pointer.getX() > x && pointer.getX() < x + width &&
                pointer.getY() > y && pointer.getY() < y + height);
    }

    public static boolean isOutOfBounds(MovableGameObject object, int floorX, int wrapWidth, int wrapHeight) {
        int globalX = object.getX() - floorX;
        int globalY = object.getY();
        return (globalX < 0 || globalX + object.getWidth() > wrapWidth ||
                globalY + object.getHeight() < 0 || globalY >= wrapHeight);
    }
}
