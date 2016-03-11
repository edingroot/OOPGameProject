package tw.edu.ntut.csie.game.util;

import tw.edu.ntut.csie.game.Pointer;

public class Common {

    public static boolean isInImageScope(Pointer pointer, MovableGameObject image) {
        return (pointer.getX() > image.getX() && pointer.getX() < image.getX() + image.getWidth() &&
                pointer.getY() > image.getY() && pointer.getY() < image.getY() + image.getHeight());
    }
}
