package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.Pointer;

public class Common {

    public static boolean isInObjectScope(Pointer pointer, MovableGameObject object) {
        return (pointer.getX() > object.getX() && pointer.getX() < object.getX() + object.getWidth() &&
                pointer.getY() > object.getY() && pointer.getY() < object.getY() + object.getHeight());
    }

    public static boolean isOutOfBouds(MovableGameObject object, int wrapWidth, int wrapHeight) {
        return (object.getX() < 0 || object.getX() + object.getWidth() > wrapWidth &&
                object.getY() < 0 || object.getY() + object.getHeight() > wrapHeight);
    }
}
