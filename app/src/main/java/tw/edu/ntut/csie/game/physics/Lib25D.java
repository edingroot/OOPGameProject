package tw.edu.ntut.csie.game.physics;

public class Lib25D {

    /**
     * Calculate horizontal displacement adjustment of 2.5D
     *
     * @param E distance of eye to the nearest object(dObject = 0)
     * @param D distance of the object
     * @param M horizontal movement in 3D
     * @return the new deltaX
     */
    public static double horizontalMoveAdj(double E, double D, double M) {
        double result;

        result = M - (M * D) / (E+D);

        return result;
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
     * @param E distance of eye to the nearest object(dObject = 0)
     * @param H height of the object
     * @param F vertical movement in 3D
     * @return
     */
    public static double heightAdj(double E, double H, double F) {
        double result;

        result = E * H / (E + F);

        return result;
    }

}
