package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class RightNav extends MovableGameObject {
    private MovingBitmap image;
    private boolean expanded = false;

    public RightNav(int y) {
        this.draggable = false;
        this.y = y;
        updateImageFromState();
    }

    @Override
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
    public void clicked(Pointer pointer) {
        expanded = !expanded;
        updateImageFromState();
    }

    private void updateImageFromState() {
        MovingBitmap image2 = new MovingBitmap(expanded ? R.drawable.nav_right : R.drawable.nav_right_pull);
        image2.resize((int)(image2.getWidth() * 0.5), (int)(image2.getHeight() * 0.5));

        if (image != null)
            image.release();
        image = image2;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.setLocation(Game.GAME_FRAME_WIDTH - image.getWidth(), y);
        image.show();
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void release() {
        image.release();
        image = null;
    }
}
