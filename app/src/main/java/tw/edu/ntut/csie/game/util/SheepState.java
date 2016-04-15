package tw.edu.ntut.csie.game.util;

/**
 * Created by Lin on 2016/4/3.
 */
public class SheepState {

    private static final int HEALTH_DELAY = 10;
    private boolean hungry, thirsty, sad;
    private int hungryValue, thirstyValue;
    private int health;
    private int healthCount;


    public SheepState() {
        hungry = false;
        thirsty = false;
        hungryValue = 0;
        thirstyValue = 0;
        health = 100;
    }

    public void healthDecline() {
        if(--healthCount <= 0){
            healthCount = HEALTH_DELAY;
            health--;
        }
    }


    public String getState() {

        if (!isGoodMood()) return "sad";
//        if (hungry) return "hungry";
//        else if (thirsty) return "thirsty";
        else return "happy";
    }

    public boolean isGoodMood() {

        if (health > 70) return true;
        else return false;
    }

    public void setHealth(int num){
        health = num;
    }
}
