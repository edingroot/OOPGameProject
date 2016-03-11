package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class Tree extends MovableGameObject {
    private MovingBitmap image;

    public Tree(int x, int y) {
        image = new MovingBitmap(R.drawable.tree1);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.setLocation(x, y);
    }

    /**
     * Set object location with BOTTOM LEFT reference point
     * @param x
     * @param y
     */
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        image.setLocation(x, y - image.getHeight());
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
