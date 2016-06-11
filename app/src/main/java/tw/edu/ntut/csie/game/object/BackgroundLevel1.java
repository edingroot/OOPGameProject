package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.BackgroundSet;

public class BackgroundLevel1 extends BackgroundSet {

    public BackgroundLevel1() {
        int initialX = -(WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center of screen
        staticBackground = new MovingBitmap(R.drawable.background_static);
        staticBackground.setLocation(initialX, 0);

        imgGround = new MovingBitmap(R.drawable.ground);
        imgGround.setLocation(
                -(imgGround.getWidth() - Game.GAME_FRAME_WIDTH) / 2,
                BackgroundLevel1.WRAP_HEIGHT - BackgroundLevel1.OVERLAP_FOREGROUND
        );

        backImages[0] = new MovingBitmap(R.drawable.background0);
        backImages[1] = new MovingBitmap(R.drawable.background1);
        backImages[2] = new MovingBitmap(R.drawable.background2);

        int px = -(WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center screen
        backImages[0].setLocation(px, 120);
        backImages[1].setLocation(px, 170);
        backImages[2].setLocation(px, 180);
    }
}
