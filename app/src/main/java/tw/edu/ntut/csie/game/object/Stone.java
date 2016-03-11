package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.DraggableGameObject;

public class Stone extends DraggableGameObject {
    private MovingBitmap image;

    public Stone(int x, int y) {
        image = new MovingBitmap(R.drawable.digit_0); // TODO: replace with correct drawable
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.setLocation(x, y);
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
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
    public void dragMoved(Pointer pointer) {

    }

    @Override
    public void dragReleased(Pointer pointer) {

    }

    @Override
    public void release() {
        image.release();
        image = null;
    }
}