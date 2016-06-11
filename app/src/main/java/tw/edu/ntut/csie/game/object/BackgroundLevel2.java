package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.BackgroundSet;

public class BackgroundLevel2 extends BackgroundSet {

    public BackgroundLevel2() {
        int initialX = -(WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center of screen
        staticBackground = new MovingBitmap(R.drawable.background);
        staticBackground.setLocation(initialX, 0);

        imgFloor = new MovingBitmap(R.drawable.floor);
        imgFloor.setLocation(
                -(imgFloor.getWidth() - Game.GAME_FRAME_WIDTH) / 2,
                BackgroundLevel1.WRAP_HEIGHT - BackgroundLevel1.OVERLAP_FOREGROUND
        );

        backImages[0] = new MovingBitmap(R.drawable.dessert_bg0);
        backImages[1] = new MovingBitmap(R.drawable.dessert_bg1);
        backImages[2] = new MovingBitmap(R.drawable.dessert_bg2);

        int px = -(WRAP_WIDTH - Game.GAME_FRAME_WIDTH) / 2; // center screen
        backImages[0].setLocation(px, 100);
        backImages[1].setLocation(px, 150);
        backImages[2].setLocation(px, 180);
    }
}
