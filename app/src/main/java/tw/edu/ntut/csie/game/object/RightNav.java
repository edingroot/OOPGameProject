package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;

public class RightNav implements GameObject {
    private MovingBitmap image;

    public RightNav(int x, int y) {
        image = new MovingBitmap(R.drawable.score_board);
        this.setLocation(x, y);
    }

    public void setLocation(int x, int y) {
        image.setLocation(x, y);
    }

    @Override
    public void move() {
    }

    @Override
    public void show() {
        image.show();
    }

    @Override
    public void release() {
        image.release();
        image = null;
    }
}
