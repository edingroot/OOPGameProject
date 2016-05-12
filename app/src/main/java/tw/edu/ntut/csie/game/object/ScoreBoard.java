package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class ScoreBoard implements GameObject {
    private MovingBitmap image;

    public ScoreBoard() {
        image = new MovingBitmap(R.drawable.score_board);
        image.resize((int)(image.getWidth() * 0.6), (int)(image.getHeight() * 0.6));

        this.setLocation(Game.GAME_FRAME_WIDTH - image.getWidth(), 0);
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
