package tw.edu.ntut.csie.game.object;

import java.util.ArrayList;
import java.util.List;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class ScoreBoard implements GameObject {
    private static final int SCORE_Y = 2;
    private MovingBitmap image;
    private int height;
    public int score;
    private int int_numbers[] = {
            R.drawable._0, R.drawable._1, R.drawable._2, R.drawable._3, R.drawable._4,
            R.drawable._5, R.drawable._6, R.drawable._7, R.drawable._8, R.drawable._9
    };
    private List<MovingBitmap> digits, digits2;
    public List<MovingBitmap> scoreIcon;

    public ScoreBoard() {

        score = 0;

        image = new MovingBitmap(R.drawable.score_board);
        image.resize((int) (image.getWidth() * 0.6), (int) (image.getHeight() * 0.6));
        this.height = image.getHeight();
        scoreIcon = new ArrayList<>();
        scoreIcon.add(new MovingBitmap(R.drawable._0));
        scoreIcon.add(new MovingBitmap(R.drawable._0));
        scoreIcon.add(new MovingBitmap(R.drawable._0));
        digits = new ArrayList<>();
        digits2 = new ArrayList<>();
        for (int i = 0 ; i < 10 ; i++) {
            digits.add(new MovingBitmap(int_numbers[i]));
            digits.get(i).resize((int) (digits.get(i).getWidth() * 0.6), (int) (digits.get(i).getHeight() * 0.6));
            digits2.add(new MovingBitmap(int_numbers[i]));
            digits2.get(i).resize((int) (digits2.get(i).getWidth() * 0.6), (int) (digits2.get(i).getHeight() * 0.6));
        }

        this.setLocation(Game.GAME_FRAME_WIDTH - image.getWidth(), 0);
    }

    public void addScore(int score){
        this.score += score;
    }

    private int pow(int x, int i){
        return x * i==0? 1 : pow(x, --i);
    }

    public void calcScore(){
        if (score < 10) {
            scoreIcon.set(0, digits.get(score));
            int posX = Game.GAME_FRAME_WIDTH - 5 - scoreIcon.get(0).getWidth();
            scoreIcon.get(0).setLocation(posX, SCORE_Y);
            scoreIcon.get(0).show();
        }
        else if (score < 100) {
            scoreIcon.set(0, digits.get(score % 10));
            scoreIcon.set(1, digits2.get(score / 10));
            int posX = Game.GAME_FRAME_WIDTH - 5 - scoreIcon.get(0).getWidth();
            scoreIcon.get(0).setLocation(posX, SCORE_Y);
            posX -= scoreIcon.get(1).getWidth();
            scoreIcon.get(1).setLocation(posX, SCORE_Y);
            scoreIcon.get(0).show();
            scoreIcon.get(1).show();
        }
        else {
            scoreIcon.set(0, digits.get(0));
            scoreIcon.set(1, digits2.get(0));
            scoreIcon.set(2, digits.get(1));
            int posX = Game.GAME_FRAME_WIDTH - 5 - scoreIcon.get(0).getWidth();
            scoreIcon.get(0).setLocation(posX, SCORE_Y);
            posX -= scoreIcon.get(1).getWidth();
            scoreIcon.get(1).setLocation(posX, SCORE_Y);
            posX -= scoreIcon.get(2).getWidth();
            scoreIcon.get(2).setLocation(posX, SCORE_Y);
            scoreIcon.get(0).show();
            scoreIcon.get(1).show();
            scoreIcon.get(2).show();
        }

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
        calcScore();
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void release() {
        image.release();
        image = null;
    }
}
