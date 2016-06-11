package tw.edu.ntut.csie.game.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tw.edu.ntut.csie.game.state.StateRun;

public abstract class LevelObjectSet {
    private StateRun appStateRun;
    protected int MAP_LEFT_MARGIN;
    protected int MAP_RIGHT_MARGIN;
    protected BackgroundSet backgroundSet;
    protected List<MovableGameObject> objects;

    public LevelObjectSet(StateRun stateRun, int mapLeftMargin, int mapRightMargin) {
        this.appStateRun = stateRun;
        this.MAP_LEFT_MARGIN = mapLeftMargin;
        this.MAP_RIGHT_MARGIN = mapRightMargin;
        this.backgroundSet = appStateRun.backgroundSet;
        objects = new ArrayList<>();
    }

    public abstract void addObjects();

    protected void addToAppObjectTable() {
        for (MovableGameObject object : objects) {
            appStateRun.addToForeObjectTable(object);
        }
    }

    public void release() {
        Iterator<MovableGameObject> it = objects.iterator();
        while (it.hasNext()) {
            MovableGameObject obj = it.next();
            appStateRun.removeFromForeObjectTable(obj);
            obj.release();
            it.remove();
        }
        objects = null;
    }

}
