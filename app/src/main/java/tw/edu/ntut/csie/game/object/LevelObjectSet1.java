package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.state.StateRun;
import tw.edu.ntut.csie.game.util.LevelObjectSet;

public class LevelObjectSet1 extends LevelObjectSet {

    public LevelObjectSet1(StateRun stateRun, int mapLeftMargin, int mapRightMargin) {
        super(stateRun, mapLeftMargin, mapRightMargin);
    }

    @Override
    public void addObjects() {
        MovingBitmap imgFloor = backgroundSet.imgGround;
        int imgRes1, imgRes2;

        imgRes1 = R.drawable.border1;
        imgRes2 = R.drawable.border2;
        objects.add(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 45, 240, imgRes1, imgRes2));
        objects.add(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 30, 280, imgRes1, imgRes2));
        objects.add(new Stone(imgFloor.getX() + MAP_LEFT_MARGIN + 15, 320, imgRes1, imgRes2));

        objects.add(new Stone(imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN - 95, 240, imgRes1, imgRes2));
        objects.add(new Stone(imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN - 80, 280, imgRes1, imgRes2));
        objects.add(new Stone(imgFloor.getX() + imgFloor.getWidth() - MAP_RIGHT_MARGIN - 65, 320, imgRes1, imgRes2));

        objects.add(new Rock(imgFloor.getX() + MAP_LEFT_MARGIN + 200, 170, R.drawable.rock1, R.drawable.rock2));
        objects.add(new Bush(imgFloor.getX() + MAP_LEFT_MARGIN + 240, 250, R.drawable.bush1, R.drawable.bush2));
        objects.add(new Tree(imgFloor.getX() + MAP_LEFT_MARGIN + 600, 190, R.drawable.tree1, R.drawable.tree2));

        addToAppObjectTable();
    }

}
