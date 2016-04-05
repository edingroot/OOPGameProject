package tw.edu.ntut.csie.game.physics;

import tw.edu.ntut.csie.game.util.Constants;

public class Lib25D {

    /**
     * Calculate horizontal displacement adjustment of 2.5D
     *
     * @param D distance of the object
     * @param M horizontal movement in 3D
     * @return the new deltaX
     */
    public static double horizontalMoveAdj(double D, double M) {
        return M - (M * D) / (Constants.EYE_TO_FRAME_Y + D);
    }

    /**
     * Calculate vertical displacement adjustment of 2.5D
     *
     * @param E distance of eye to the nearest object(dObject = 0)
     * @param Eh distance of eye to ground
     * @param F vertical movement in 3D
     * @return
     */
    public static double verticalMoveAdj(double E, double Eh, double F) {
        double result;

        double distance = E + F;
        double horizonOfB = Math.sqrt(distance * distance + Eh * Eh);
        double ratio = F / (E + F);
        double horizonOfFToB = horizonOfB * ratio;

        result = Math.sqrt(horizonOfFToB * horizonOfFToB - F * F);

        return result;
    }

    /**
     * Calculate height adjustment of 2.5D
     *
     * @param H height of the object
     * @param Y vertical movement in 2D
     * @return new height of the object
     */
    public static double heightAdj(double H, double Y) {
        double F = Y * (Constants.EYE_TO_FLOOR_H) / (Constants.EYE_TO_FLOOR_H - Y);

        return Constants.EYE_TO_FRAME_Y * H / (Constants.EYE_TO_FRAME_Y + F);
    }

}
