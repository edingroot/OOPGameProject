package tw.edu.ntut.csie.game.util;

import android.provider.Settings;

/**
 * Created by Lin on 2016/4/3.
 */
public class SheepState {

    private static final int HAPPINESS_DELAY = 10, HUNGRY_DELAY = 10, THIRSTY_DELAY = 10, HEALTH_DELAY = 10;
    private boolean happy, sad, hungry, thirsty, dead;
    private int hungryCount, thirstyCount, healthCount;
    private int hungryValue, thirstyValue, health;
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
        health = 100;
    }

    private void clearState() {
        happy = false;
        sad = false;
        hungry = false;
        thirsty = false;
    }

    public void healthDecline() {
        if (--healthCount <= 0) {
            healthCount = HEALTH_DELAY;
            health--;
        }
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
        if (dead) return 4;
        else if (hungry) return 1;
        else if (thirsty) return 2;
        else return 0;
    }

    public void work() {
        if (health <= 0) {
            clearState();
            dead = true;
        }
        else if (hungryValue > 100) {
            clearState();
            hungry = true;
            healthDecline();
        }
        else if (thirstyValue > 1000) {
            clearState();
            thirsty = true;
            healthDecline();
        }
        else if (happiness < 40) {
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
