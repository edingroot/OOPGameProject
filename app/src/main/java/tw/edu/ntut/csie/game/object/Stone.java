package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class Stone extends MovableGameObject {
    private MovingBitmap image;
    private double random = Math.random()*100;

    public Stone(int x, int y) {
        this.draggable = false;
        if (random < 50)image = new MovingBitmap(R.drawable.border1);
        else image = new MovingBitmap(R.drawable.border2);
        image.resize((int)(image.getWidth()*0.8), (int)(image.getHeight()*0.8));
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.setLocation(x, y);
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        image.setLocation(x, y);
    }

    public void resize(double ratio) {
        super.resize(ratio);
        image.resize((int)(width*ratio), (int)(height*ratio));
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
