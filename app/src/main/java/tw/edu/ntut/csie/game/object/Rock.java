package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class Rock extends MovableGameObject {
    private MovingBitmap image;

    public Rock(int x, int y, int imgResource1, int imgResource2) {
        this.draggable = false;
        if (Math.random() * 100 < 50)
            image = new MovingBitmap(imgResource1);
        else
            image = new MovingBitmap(imgResource2);
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
        image.resize((int) (width * ratio), (int) (height * ratio));
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
