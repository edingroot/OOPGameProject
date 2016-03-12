package tw.edu.ntut.csie.game.physics;

public class Lib25D {

    /**
     * Calculate horizontal displacement adjustment of 2.5D
     * @param E distance of eye to the nearest object(dObject = 0)
     * @param D distance of the object
     * @param M horizontal movement in 3D
     * @return the new deltaX
     */
    public static double horizontalMoveAdj(double E, double D, double M) {

        double result = 1;

        result *= M * D;
        result /= E + D;
        result = M - result;

        return result;
    }

//    public static double[] displayAdjToDepth(double height, ) {
//        double sizeAdj = 1;
//
//    }

}
