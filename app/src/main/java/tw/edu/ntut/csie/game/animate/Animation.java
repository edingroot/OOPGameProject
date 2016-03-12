package tw.edu.ntut.csie.game.animate;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.Constants;
/**
 * Created by Lin on 2016/3/12.
 */
public class Animation implements GameObject{

    private ArrayList<MovingBitmap> frames;
    private int num = 0;
    private int count = 0;
    private boolean isRepeation = true;

    private Timer timer;
    private TimerTask timerTask;

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {

            }
        };
    }

    private void StartTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, Constants.TIME_DELAY, Constants.TIME_INTERVAL);
    }

    private void StopTimer() { timer.cancel(); }

    public void play() { StartTimer(); }

    public void stop() { StopTimer(); }

    public void isRepeat(boolean flag) { isRepeation = flag; }

    public void addFrame(String fileName) { frames.add(new MovingBitmap(fileName)); }

    @Override
    public void move() {

    }

    @Override
    public void show() {

    }

    @Override
    public void release() {

    }
}
