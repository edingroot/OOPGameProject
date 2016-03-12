package tw.edu.ntut.csie.game.animate;

import java.util.Timer;
import java.util.TimerTask;

import tw.edu.ntut.csie.game.core.MovingBitmap;

/**
 * Created by Lin on 2016/3/12.
 */
public class Animation {

    private MovingBitmap images[];
    private int num = 0;
    private int count = 0;

    private Timer timer;
    private TimerTask timerTask;

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                images[count].show();

                if (count - 1 < num) {
                    images[num].release();
                }
                else
                    images[count-1].release();

                if (count < num)
                    count++;
                else
                    count = 0;
            }
        };
    }

    private void StartTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask,0,250);
    }

    private void StopTimer() {
        timer.cancel();
    }

    public void play() {
        StartTimer();
    }

    public void stop() {
        StopTimer();
    }

    public void imageInsert(MovingBitmap image) {
        images[num] = new MovingBitmap();
        images[num] = image;
        num++;
    }
}
