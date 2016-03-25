package tw.edu.ntut.csie.game.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.extend.Animation;
import tw.edu.ntut.csie.game.util.MovableGameObject;

import tw.edu.ntut.csie.game.physics.Lib25D;

public class Sheep extends MovableGameObject {

    private Animation body_rest, body_walk, head_rest,head_walk, tail, eye_happy;
    private boolean isWalk, isRest, isDrag, isFall, isLand;
    private List<Animation> animations;
    private List<Animation> animations_body;
    private List<Animation> animations_head;
    private List<Animation> animations_eye;
    private Random rand;
    //direction = true = Left
    private boolean direction;
    private int HEAD_SHIFT_POS_X = 30;
    private int HEAD_SHIFT_POS_Y = -2;
    private int EYE_SHIFT_POS_X = 17;
    private int EYE_SHIFT_POS_Y = -2;
    private int x, y, count;

    public Sheep(int x, int y) {
        this.width = 100;
        this.height = 200;
        this.direction = true;
        this.x = x;
        this.y = y;

        isWalk = false;
        isDrag = false;
        isWalk = false;
        isFall = false;
        isLand = false;

        animations = new ArrayList<>();
        animations_body = new ArrayList<>();
        animations_head = new ArrayList<>();
        animations_eye = new ArrayList<>();

        body_rest = new Animation();
        animations.add(body_rest);
        animations_body.add(body_rest);
        body_rest.addFrame(R.drawable.sheep_default_0);
        body_rest.addFrame(R.drawable.sheep_default_1);
        body_rest.addFrame(R.drawable.sheep_default_2);
        body_rest.addFrame(R.drawable.sheep_default_1);

        body_walk = new Animation();
        animations.add(body_walk);
        animations_body.add(body_walk);
        body_walk.addFrame(R.drawable.sheep_walk_0);
        body_walk.addFrame(R.drawable.sheep_walk_1);
        body_walk.addFrame(R.drawable.sheep_walk_2);
        body_walk.addFrame(R.drawable.sheep_walk_3);

        head_rest = new Animation();
        animations.add(head_rest);
        animations_head.add(head_rest);
        head_rest.addFrame(R.drawable.face_default);

        head_walk = new Animation();
        animations.add(head_walk);
        animations_head.add(head_walk);
        head_walk.addFrame(R.drawable.face_walk_0);
        head_walk.addFrame(R.drawable.face_walk_1);
        head_walk.addFrame(R.drawable.face_walk_2);
        head_walk.addFrame(R.drawable.face_walk_1);

        eye_happy = new Animation();
        animations.add(eye_happy);
        animations_eye.add(eye_happy);
        eye_happy.addFrame(R.drawable.eye_default);

        tail = new Animation();

        this.setLocation(x,y);
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        for (Animation item : animations_body) {
            item.setLocation(x, y);
        }
        for (Animation item : animations_head) {
            if(direction) item.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y);
            else item.setLocation(x + HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y);
        }
        for (Animation item : animations_eye) {
            if(direction) item.setLocation(x - HEAD_SHIFT_POS_X + EYE_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + EYE_SHIFT_POS_Y);
            else item.setLocation(x + HEAD_SHIFT_POS_X - EYE_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + EYE_SHIFT_POS_Y);
        }
    }

    private void setAnimation() {
        if(isRest){
            for (Animation item : animations) item.setVisible(false);
            body_rest.setVisible(true);
            head_rest.setVisible(true);
            eye_happy.setVisible(true);
        }
        else if(isWalk){
            for (Animation item : animations) item.setVisible(false);
            body_walk.setVisible(true);
            head_walk.setVisible(true);
            eye_happy.setVisible(true);
        }
        else {
            for (Animation item : animations) item.setVisible(false);
            body_rest.setVisible(true);
            head_rest.setVisible(true);
        }

    }

    public void rest() {
        isRest = true;
        isWalk = false;
        setAnimation();
    }
    public void walk() {
        isWalk = true;
        isRest = false;
        setAnimation();
        for (count = 0 ; count < 200 ; count++) {

        }
        if (count == 200) {
            this.setLocation(--x, y);
            count = 0;
        }
    }
    public void drag() {
        isDrag = true;
    }

    private void aiMove() {
        if (this.dragging) isDrag = true;
        else {
            isDrag = false;
            this.walk();

        }
    }

    @Override
    public void move() {
        aiMove();
    }

    @Override
    public void show() {
        for (Animation item : animations) {
            item.move();
            item.show();
        }
    }

    @Override
    public void release() {
        for (Animation item : animations) {
            item.release();
            item = null;
        }
    }
}
