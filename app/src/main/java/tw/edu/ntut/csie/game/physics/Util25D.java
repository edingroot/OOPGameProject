package tw.edu.ntut.csie.game.physics;

import tw.edu.ntut.csie.game.util.Constants;

public class Util25D {

    public static int calOnForegroundX(int screenWidth, int screenHeight, int x, int y) {
        double E = Constants.EYE_TO_FRAME_Y;
        double D = screenHeight - y;
        int screenCenter = screenWidth / 2;
        double M = screenCenter - x;
        int deltaX = (int) Lib25D.horizontalMoveAdj(E, D, M);
        return screenCenter + deltaX;
    }

    public static int calOnForegroundHeight(int screenHeight, int height, int y) {
        double E = Constants.EYE_TO_FRAME_Y;
        double F = Math.abs(screenHeight - y);
        return (int) Lib25D.heightAdj(E, height, F);
    }

}
