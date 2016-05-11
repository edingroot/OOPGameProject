package tw.edu.ntut.csie.game.util;

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

    public String getState() {

        if (sad) return "sad";
        if (hungry) return "hungry";
        else if (thirsty) return "thirsty";
        else return "happy";
    }

    public void work() {

        if (hungryValue > 30) {
            clearState();
            hungry = true;
        }
        else if (thirstyValue > 40) {
            clearState();
            thirsty = true;
        }else if (happiness < 80) {
            clearState();
            sad = true;
        }
        else {
            happinessDecline();
            satietyDecline();
            moistureDecline();

            happy = true;
        }
    }

    public void satisfy(String event) {
        if (event.equals("eat")) {
            hungryValue -= 30;
            thirstyValue -= 10;
            happiness = 100;
        }
        else if (event == "drink") {
            thirstyValue -= 40;
            hungryValue -= 10;
            happiness = 100;
        }
        else if (event == "drag") {
            happiness = 100;
        }
    }
}
