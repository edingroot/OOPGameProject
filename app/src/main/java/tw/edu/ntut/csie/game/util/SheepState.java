package tw.edu.ntut.csie.game.util;

import android.provider.Settings;

/**
 * Created by Lin on 2016/4/3.
 */
public class SheepState {

    private static final int HAPPINESS_DELAY = 10, HUNGRY_DELAY = 10, THIRSTY_DELAY = 10;
    private boolean happy, sad, hungry, thirsty;
    private int hungryCount, thirstyCount;
    private int hungryValue, thirstyValue;
    private int happiness;
    private int happinessCount;


    public SheepState() {
        hungry = false;
        thirsty = false;
        hungryCount = 10;
        thirstyCount = 10;
        hungryValue = 0;
        thirstyValue = 0;
        happiness = 100;
    }

    private void clearState() {
        happy = false;
        sad = false;
        hungry = false;
        thirsty = false;
    }

    public void happinessDecline() {
        if(--happinessCount <= 0){
            happinessCount = HAPPINESS_DELAY;
            happiness--;
        }
    }

    public void satietyDecline() {
        if(--hungryCount <= 0){
            hungryCount = HUNGRY_DELAY;
            hungryValue ++;
        }
    }

    public void moistureDecline() {
        if (--thirstyCount <= 0) {
            thirstyCount = THIRSTY_DELAY;
            thirstyValue++;
        }
    }

    public int getState() {

        if (sad) return 3;
        if (hungry) return 1;
        else if (thirsty) return 2;
        else return 0;
    }

    public void work() {
        //System.out.println(hungryValue);
        if (hungryValue > 40) {
            clearState();
            hungry = true;
        }
        else if (thirstyValue > 100) {
            clearState();
            thirsty = true;
        }else if (happiness < 80) {
            clearState();
            satietyDecline();
            sad = true;
        }
        else {
            happy = true;
            happinessDecline();
            satietyDecline();
            moistureDecline();
        }
    }

    public void satisfy(int event) {
        if (event == 1) {
            hungryValue = 0;
            thirstyValue = 0;
            happiness = 100;
            clearState();
            happy = true;
        }
        else if (event == 2) {
            thirstyValue -= 40;
            hungryValue -= 10;
            happiness = 100;
        }
        else if (event == 3) {
            thirstyValue = 0;
            happiness = 100;
        }
    }
}
