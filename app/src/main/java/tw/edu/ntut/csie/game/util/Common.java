package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.Pointer;

public class Common {

    public static boolean isInObjectScope(Pointer pointer, MovableGameObject object) {
        return (pointer.getX() > object.getX() && pointer.getX() < object.getX() + object.getWidth() &&
                pointer.getY() > object.getY() && pointer.getY() < object.getY() + object.getHeight());
    }

    public static boolean isOutOfBounds(MovableGameObject object, int floorX, int wrapWidth, int wrapHeight) {
        int globalX = object.getX() - floorX;
        int globalY = object.getY();
        System.out.println(globalX);
        return (globalX < 0 || globalX > wrapWidth ||
                globalY < 0 || globalY >= wrapHeight);
    }
}
