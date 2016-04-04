package tw.edu.ntut.csie.game.util;

/**
 * Created by Lin on 2016/4/3.
 */
public class SheepState {

    private boolean isHungry, isThirsty;
    private int hungry, thirsty;
    private int health;


    public SheepState() {
        isHungry = false;
        isThirsty = false;
        hungry = 0;
        thirsty = 0;
        health = 100;
    }

    public String getState() {

        if (isHungry) return "hungry";
        else if (isThirsty) return "thirsty";
        return "happy";
    }

    public boolean isGoodMood() {
        if (health > 70) return true;
        else return false;
    }
}
