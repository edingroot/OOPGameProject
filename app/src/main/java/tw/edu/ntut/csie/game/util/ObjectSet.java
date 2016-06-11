package tw.edu.ntut.csie.game.util;

public abstract class ObjectSet {
    protected int FLOOR_X;
    protected int MAP_LEFT_MARGIN;
    protected int MAP_RIGHT_MARGIN;

    public ObjectSet(int floorX, int mapLeftMargin, int mapRightMargin) {
        this.FLOOR_X = floorX;
        this.MAP_LEFT_MARGIN = mapLeftMargin;
        this.MAP_RIGHT_MARGIN = mapRightMargin;
    }

    public abstract void addObjects();

}
