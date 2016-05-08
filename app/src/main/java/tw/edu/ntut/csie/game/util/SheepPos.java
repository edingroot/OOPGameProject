package tw.edu.ntut.csie.game.util;

/**
 * Created by voyag on 2016/5/7.
 */
public class SheepPos {

    public int cx, cy; //center point
    public int px, py; //set position point
    public int picW, picH;
    public int lenX, lenY;
    public double angle, length;

    public SheepPos(double angle, double length, int picW, int picH) {
        this.picW = picW;
        this.picH = picH;
        this.angle = angle;
        this.length = length;
    }

    public void set(int x, int y, boolean direction, double ratio){
        lenX = (int)(length*Math.cos(Math.toRadians(angle))*ratio);
        lenY = (int)(length*Math.sin(Math.toRadians(angle))*ratio);

        cx = (direction) ? x + lenX : x - lenX;
        cy = y - lenY;
        //cy = y;

        px = cx - (int)(picW*0.8*ratio / 2);
        py = cy - (int)(picH*0.8*ratio / 2);

        //px=cx;
        //py=cy;
    }

}
