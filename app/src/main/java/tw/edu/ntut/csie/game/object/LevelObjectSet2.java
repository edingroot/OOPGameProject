package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.state.StateRun;
import tw.edu.ntut.csie.game.util.LevelObjectSet;

public class LevelObjectSet2 extends LevelObjectSet {

    public LevelObjectSet2(StateRun stateRun, int mapLeftMargin, int mapRightMargin) {
        super(stateRun, mapLeftMargin, mapRightMargin);
    }

    @Override
    public void addObjects() {
        MovingBitmap imgFloor = backgroundSet.imgGround;

        int imgRes1 = R.drawable.dessert_border1;
        int imgRes2 = R.drawable.dessert_border2;
        objects.add(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 45, 240, imgRes1, imgRes2));
        objects.add(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 30, 280, imgRes1, imgRes2));
        objects.add(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 15, 320, imgRes1, imgRes2));

        objects.add(new Stone(imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN - 95, 240, imgRes1, imgRes2));
        objects.add(new Stone(imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN - 80, 280, imgRes1, imgRes2));
        objects.add(new Stone(imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN - 65, 320, imgRes1, imgRes2));

        objects.add(new Rock(imgFloor.getX() + MAP_LEFT_MARGIN + 200, 170, R.drawable.dessert_rock1, R.drawable.dessert_rock2));
        objects.add(new Bush(imgFloor.getX() + MAP_LEFT_MARGIN + 240, 250, R.drawable.dessert_bush1, R.drawable.dessert_bush2));
        objects.add(new Tree(imgFloor.getX() + MAP_LEFT_MARGIN + 600, 190, R.drawable.dessert_tree1, R.drawable.dessert_tree1));

        addToAppObjectTable();
    }

}
