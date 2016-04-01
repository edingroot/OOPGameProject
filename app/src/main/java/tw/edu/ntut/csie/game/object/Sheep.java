package tw.edu.ntut.csie.game.object;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.extend.Animation;
import tw.edu.ntut.csie.game.util.MovableGameObject;


public class Sheep extends MovableGameObject {

    private Animation body_rest, body_walk, head_rest, head_walk, head_drag, body_drag, tail, eye_happy, body_fall, body_land;
    private Animation head_fall, head_land;
    private boolean isWalk, isRest, isDrag, isFall, isLand;
    private List<Animation> animations;
    private List<Animation> animations_body;
    private List<Animation> animations_head;
    private List<Animation> animations_eye;
    private Random random;
    //direction = true = Left
    private boolean direction;
    private int HEAD_SHIFT_POS_X = 30;
    private int HEAD_SHIFT_POS_Y = -2;
    private int EYE_SHIFT_POS_X = 17;
    private int EYE_SHIFT_POS_Y = -2;
    private int TIME_DELAY = 100;
    private int x, y, count, aiCount=0, instr=0;
    private int initImageX, initImageY;
    private MovingBitmap image;
    private boolean dragRealese;
    private int realeseY;

    public Sheep(int x, int y) {
        this.width = 100;
        this.height = 85;
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

        body_walk = new Animation(5);
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

        body_drag = new Animation(2);
        animations.add(body_drag);
        animations_body.add(body_drag);
        body_drag.addFrame(R.drawable.sheep_drag_0);
        body_drag.addFrame(R.drawable.sheep_drag_1);

        head_drag = new Animation();
        animations.add(head_drag);
        animations_head.add(head_drag);
        head_drag.addFrame(R.drawable.face_drag);
        this.setLocation(x, y);

        body_fall = new Animation();
        animations.add(body_fall);
        animations_body.add(body_fall);
        body_fall.addFrame(R.drawable.sheep_fall_0);
        body_fall.addFrame(R.drawable.sheep_fall_1);
        //body_fall.setRepeating(false);

        body_land = new Animation(3);
        animations.add(body_land);
        animations_body.add(body_land);
        body_land.addFrame(R.drawable.sheep_landing_0);
        body_land.addFrame(R.drawable.sheep_landing_1);
        body_land.addFrame(R.drawable.sheep_landing_2);
        body_land.addFrame(R.drawable.sheep_landing_2);
        //body_land.setRepeating(false);

        head_fall = new Animation();
        animations.add(head_fall);
        animations_head.add(head_fall);
        head_fall.addFrame(R.drawable.face_fall_0);
        head_fall.addFrame(R.drawable.face_fall_1);
        head_fall.addFrame(R.drawable.face_fall_2);
        //head_fall.setRepeating(false);

        head_land = new Animation(3);
        animations.add(head_land);
        animations_head.add(head_land);
        head_land.addFrame(R.drawable.face_landing_0);
        head_land.addFrame(R.drawable.face_landing_1);
        head_land.addFrame(R.drawable.face_landing_2);
        head_land.addFrame(R.drawable.face_landing_2);
        //head_land.setRepeating(false);
    }

    public void clearActions() {
        isRest = false;
        isWalk = false;
        isFall = false;
        isLand = false;
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
        if(direction) head_drag.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y+20);
        else head_drag.setLocation(x + HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y+20);
    }

    private void setAnimation() {
        if (isRest) {
            for (Animation item : animations) item.setVisible(false);
            body_rest.setVisible(true);
            head_rest.setVisible(true);
            eye_happy.setVisible(true);
        }
        else if (isWalk) {
            for (Animation item : animations) item.setVisible(false);
            body_walk.setVisible(true);
            head_walk.setVisible(true);
            eye_happy.setVisible(true);
        }
        else if (isDrag) {
            for (Animation item : animations) item.setVisible(false);
            body_drag.setVisible(true);
            head_drag.setVisible(true);
        }
        else if (isLand) {
            for (Animation item : animations) item.setVisible(false);
            body_land.setVisible(true);
            head_land.setVisible(true);
        }
        else if (isFall) {
            for (Animation item : animations) item.setVisible(false);
            body_fall.setVisible(true);
            head_fall.setVisible(true);
        }
        else {
            for (Animation item : animations) item.setVisible(false);
            body_rest.setVisible(true);
            head_rest.setVisible(true);
        }

    }

    public void rest() {
        body_land.reset();
        head_land.reset();
        clearActions();
        isRest = true;
        setAnimation();
    }
    public void walk() {
        clearActions();
        isWalk = true;
        setAnimation();
        for (count = 0 ; count < 200 ; count++) {}
        if (count == 200) {
            if (direction) this.setLocation(--x, y);
            else this.setLocation(++x, y);
            count = 0;
        }
    }
    public void drag() {
        clearActions();
        isDrag = true;
        setAnimation();
    }
    public void fall() {
        clearActions();
        isFall = true;
//        head_fall.setDelay(30);
//        body_fall.setDelay();
        setAnimation();

        if (y < 260) this.setLocation(x, y+=20);
        else {
            dragRealese = false;
            isFall = false;
            isLand = true;
        }
    }
    public void land() {
        y = 250;
        setLocation(x, y);
        isLand = true;
        setAnimation();
        if (body_land.isLastFrame()) this.rest();
    }


    private void aiMove() {
        if (this.dragging) {
            instr = 0;
            this.drag();
            dragRealese = true;
        }
        else {
            isDrag = false;
            if (dragRealese) {
                realeseY = y;
                this.fall();
            }
            else if (isLand) {
                this.land();
            }
            else {
                if (--aiCount <= 0) {
                    aiCount = TIME_DELAY;
                    instr = (int)(Math.random()*100);
                }
                if (instr > 80) this.walk();
                else this.rest();
            }
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
            item = null;
            item.release();
        }
    }

    @Override
    public void dragPressed(Pointer pointer){
        super.dragPressed(pointer);
        Pointer singlePointer = pointer;
        initImageX = this.getX();
        initImageY = this.getY();
        initialX = singlePointer.getX();
        initialY = singlePointer.getY();
    }

    @Override
    public void dragMoved(Pointer pointer){
        Pointer singlePointer = pointer;
        int deltaX = singlePointer.getX() - initialX;
        int deltaY = singlePointer.getY() - initialY;
        x = initImageX + deltaX;
        y = initImageY + deltaY;
        this.setLocation(x, y);
    }
}
