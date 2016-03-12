package tw.edu.ntut.csie.game.physics;

public class Lib25D {

    /**
     * Calculate horizontal displacement adjustment of 2.5D
     * @param E distance of eye to the nearest object(dObject = 0)
     * @param d distance of the object
     * @param M horizontal movement in 3D
     * @return the new deltaX
     */
    public static double horizontalMoveAdj(double E, double d, double M) {
        double ratio = 0.5;
        double result = 1;

        double Epd = E + d;
        double sEm = Math.sqrt(Epd * Epd + M * M);
        double mDe = (M * d) / (Epd);
        result *= mDe * sEm;
        result /= sEm ;

        return result * ratio;
    }

//    public static double[] displayAdjToDepth(double height, ) {
//        double sizeAdj = 1;
//
//    }

}
