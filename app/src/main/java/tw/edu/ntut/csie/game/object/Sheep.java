package tw.edu.ntut.csie.game.object;

import java.util.ArrayList;
import java.util.List;

import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.SheepPos;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.extend.Animation;
import tw.edu.ntut.csie.game.state.StateRun;
import tw.edu.ntut.csie.game.util.MovableGameObject;
import tw.edu.ntut.csie.game.util.SheepState;

public class Sheep extends MovableGameObject {

    //region imagePositionConst

    private static final int HEAD_SHIFT_POS_X = 30;
    private static final int HEAD_SHIFT_POS_Y = 8;
    private static final int HEAD_DRAG_SHIFT_POS_Y = 40;
    private static final int R_HEAD_SHIFT_POS_X = 54;
    private static final int R_HEAD_SAD_SHIFT_POS_X = 51;
    private static final int EYE_SHIFT_POS_X = 17;
    private static final int EYE_SHIFT_POS_Y = -2;
    private static final int R_EYE_SHIFT_POS_X = 17;
    private static final int HEAD_FALL_SHIFT_POS_Y = 15;
    private static final int TAIL_SHIFT_X = 82;
    private static final int R_TAIL_SHIFT_X = 15;
    private static final int TAIL_SHIFT_Y = 30;
    private static final int TAIL_SHAKE_Y = 1;
    private static final int TAIL_DRAG_POS_X = 5;
    private static final int TAIL_DRAG_POS_Y = 40;
    private static final int TAIL_LAND_POS_Y0 = 5, TAIL_LAND_POS_Y2 = -5;

    private static final int eyeW = 30, eyeH = 26;
    //endregion

    private List<Grass> grasses;
    private Grass grassTest;

    private SheepPos p_body;
    private SheepPos p_head;
    private SheepPos p_head_sad;
    private SheepPos p_r_head_sad;
    private SheepPos p_head_fall;
    private SheepPos p_body_drag;
    private SheepPos p_head_drag;
    private SheepPos p_tail_drag;
    private SheepPos p_eye;
    private SheepPos p_tail;
    private SheepPos p_shadow;
    private SheepPos p_state;
    private SheepPos p_head_eat;
    private SheepPos p_eat;
    private SheepPos p_r_eat;

    private static final int oriWidth = 100, oriHeight = 100;

    private static final int TIME_DELAY = 100;
    private static final int WALK_DELAY = 50;
    private static final int BLINK_DELAY = 150, BLINK_TIME = 10;
    private static final int CHEW_DELAY = 60, EAT_DELAY = 30, RESIZE_DELAY = 5;
    private static final int GRASS_SHORTEST_DISTANCE = 100;
    private SheepState state;

    //region animationDeclaration
    private Animation body_rest, body_walk, head_rest, head_walk, head_drag, body_drag, tail, eye_happy, body_fall, body_land;
    private Animation head_fall, head_land;
    private Animation head_sad_rest, head_sad_walk, eye_sad;
    private Animation head_eat, head_chew;
    private Animation r_body_rest, r_body_walk, r_head_rest, r_head_walk, r_head_drag, r_body_drag, r_tail, r_eye_happy, r_body_fall, r_body_land;
    private Animation r_head_fall, r_head_land;
    private Animation r_head_sad_rest, r_head_sad_walk, r_eye_sad;
    private Animation r_head_eat, r_head_chew;
    private Animation shadow, r_shadow;
    private Animation sign_hungry, r_sign_hungry;

    private List<Animation> animations;
    private List<Animation> animations_body;
    private List<Animation> animations_head;
    private List<Animation> animations_eye;
    private List<Animation> animations_tail;

    //endregion

    private double ratio;
    private StateRun stateRun;
    private int id;
    private boolean isWalk, isRest, isDrag, isFall, isLand, isEat, isChew;
    private boolean direction; // true: Left
    private double walkDir;
    private double rx, ry;
    private int chewCount, chewTimer, eatCount=0;
    private boolean arriveGrass;
    private int walkCount=0, aiCount=0, blinkCount=0, blinkTimeCount=0, instr=0;
    private int initImageX, initImageY, releaseY, initialPointerX, initialPointerY;
    private boolean dragRelease;
    private boolean isBlink;
    private int tmpY = 0;
    private double lx, ly;
    private double angle;

    private int resizeTimer;

    public Sheep(StateRun stateRun, int x, int y, int id) {
        this(stateRun, x, y);
        this.id = id;
    }

    public Sheep(StateRun stateRun, int x, int y) {
        width = oriWidth;
        height = oriHeight;
        this.stateRun = stateRun;
        this.direction = true;
        this.x = x;
        this.y = y;

        isWalk = false;
        isDrag = false;
        isWalk = false;
        isFall = false;
        isLand = false;
        isEat = false;
        isChew = false;

        chewCount = 0;
        chewTimer = CHEW_DELAY;
        resizeTimer = 0;

        state = new SheepState();

        grasses = new ArrayList<>();

        animations = new ArrayList<>();
        animations_body = new ArrayList<>();
        animations_head = new ArrayList<>();
        animations_eye = new ArrayList<>();
        animations_tail = new ArrayList<>();

        sign_hungry = new Animation();
        animations.add(sign_hungry);
        sign_hungry.addFrame(R.drawable.state_hungry);


        //region AnimationLeft

        shadow = new Animation();
        animations.add(shadow);
        shadow.addFrame(R.drawable.shadow_sheep);

        tail = new Animation();
        animations.add(tail);
        animations_tail.add(tail);
        tail.addFrame(R.drawable.tail);

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

        head_sad_rest = new Animation();
        animations.add(head_sad_rest);
        animations_head.add(head_sad_rest);
        animations_head.add(head_sad_rest);
        head_sad_rest.addFrame(R.drawable.face_sad_default);

        head_walk = new Animation();
        animations.add(head_walk);
        animations_head.add(head_walk);
        head_walk.addFrame(R.drawable.face_walk_0);
        head_walk.addFrame(R.drawable.face_walk_1);
        head_walk.addFrame(R.drawable.face_walk_2);
        head_walk.addFrame(R.drawable.face_walk_1);

        head_sad_walk = new Animation();
        animations.add(head_sad_walk);
        animations_head.add(head_sad_walk);
        head_sad_walk.addFrame(R.drawable.face_sad_walk_0);
        head_sad_walk.addFrame(R.drawable.face_sad_walk_1);
        head_sad_walk.addFrame(R.drawable.face_sad_walk_2);
        head_sad_walk.addFrame(R.drawable.face_sad_walk_1);

        eye_happy = new Animation(3);
        animations.add(eye_happy);
        animations_eye.add(eye_happy);
        eye_happy.addFrame(R.drawable.eye_happy);
        eye_happy.addFrame(R.drawable.eye_happy);
        eye_happy.addFrame(R.drawable.eye_happy_1);
        eye_happy.addFrame(R.drawable.eye_happy_2);

        eye_sad = new Animation();
        animations.add(eye_sad);
        animations_eye.add(eye_sad);
        eye_sad.addFrame(R.drawable.eye_sad_0);
        eye_sad.addFrame(R.drawable.eye_sad_0);
        eye_sad.addFrame(R.drawable.eye_sad_1);
        eye_sad.addFrame(R.drawable.eye_sad_2);
        eye_sad.addFrame(R.drawable.eye_sad_1);

        body_drag = new Animation(2);
        animations.add(body_drag);
        animations_body.add(body_drag);
        body_drag.addFrame(R.drawable.sheep_drag_0);
        body_drag.addFrame(R.drawable.sheep_drag_1);

        head_drag = new Animation();
        animations.add(head_drag);
//        animations_head.add(head_drag);
        head_drag.addFrame(R.drawable.face_drag);

        body_fall = new Animation();
        animations.add(body_fall);
        animations_body.add(body_fall);
        body_fall.addFrame(R.drawable.sheep_fall_0);
        body_fall.addFrame(R.drawable.sheep_fall_1);

        body_land = new Animation(2);
        animations.add(body_land);
        animations_body.add(body_land);
        body_land.addFrame(R.drawable.sheep_landing_0);
        body_land.addFrame(R.drawable.sheep_landing_0);
        body_land.addFrame(R.drawable.sheep_landing_1);
        body_land.addFrame(R.drawable.sheep_landing_1);
        body_land.addFrame(R.drawable.sheep_landing_2);
        body_land.addFrame(R.drawable.sheep_landing_2);

        head_fall = new Animation();
        animations.add(head_fall);
//        animations_head.add(head_fall);
        head_fall.addFrame(R.drawable.face_fall_0);
        head_fall.addFrame(R.drawable.face_fall_1);
        head_fall.addFrame(R.drawable.face_fall_2);

        head_land = new Animation(2);
        animations.add(head_land);
        animations_head.add(head_land);
        head_land.addFrame(R.drawable.face_landing_0);
        head_land.addFrame(R.drawable.face_landing_0);
        head_land.addFrame(R.drawable.face_landing_1);
        head_land.addFrame(R.drawable.face_landing_1);
        head_land.addFrame(R.drawable.face_landing_2);
        head_land.addFrame(R.drawable.face_landing_2);

        head_eat = new Animation(7);
        animations.add(head_eat);
        animations_head.add(head_eat);
        head_eat.addFrame(R.drawable.face_eat_0);
        head_eat.addFrame(R.drawable.face_eat_1);
        head_eat.addFrame(R.drawable.face_eat_1);

        head_chew = new Animation(5);
        animations.add(head_chew);
        animations_head.add(head_chew);
        head_chew.addFrame(R.drawable.face_chew_0);
        head_chew.addFrame(R.drawable.face_chew_1);
        head_chew.addFrame(R.drawable.face_chew_2);
        //endregion

        //region AnimationRight

        r_shadow = new Animation();
        animations.add(r_shadow);
        r_shadow.addFrame(R.drawable.r_shadow_sheep);

        r_tail = new Animation();
        animations.add(r_tail);
        animations_tail.add(r_tail);
        r_tail.addFrame(R.drawable.r_tail);

        r_body_rest = new Animation();
        animations.add(r_body_rest);
        animations_body.add(r_body_rest);
        r_body_rest.addFrame(R.drawable.r_sheep_default_0);
        r_body_rest.addFrame(R.drawable.r_sheep_default_1);
        r_body_rest.addFrame(R.drawable.r_sheep_default_2);
        r_body_rest.addFrame(R.drawable.r_sheep_default_1);

        r_body_walk = new Animation(5);
        animations.add(r_body_walk);
        animations_body.add(r_body_walk);
        r_body_walk.addFrame(R.drawable.r_sheep_walk_0);
        r_body_walk.addFrame(R.drawable.r_sheep_walk_1);
        r_body_walk.addFrame(R.drawable.r_sheep_walk_2);
        r_body_walk.addFrame(R.drawable.r_sheep_walk_3);

        r_head_rest = new Animation();
        animations.add(r_head_rest);
        animations_head.add(r_head_rest);
        r_head_rest.addFrame(R.drawable.r_face_default);

        r_head_sad_rest = new Animation();
        animations.add(r_head_sad_rest);
        animations_head.add(r_head_sad_rest);
        animations_head.add(r_head_sad_rest);
        r_head_sad_rest.addFrame(R.drawable.r_face_sad_default);

        r_head_walk = new Animation();
        animations.add(r_head_walk);
        animations_head.add(r_head_walk);
        r_head_walk.addFrame(R.drawable.r_face_walk_0);
        r_head_walk.addFrame(R.drawable.r_face_walk_1);
        r_head_walk.addFrame(R.drawable.r_face_walk_2);
        r_head_walk.addFrame(R.drawable.r_face_walk_1);

        r_head_sad_walk = new Animation();
        animations.add(r_head_sad_walk);
        animations_head.add(r_head_sad_walk);
        r_head_sad_walk.addFrame(R.drawable.r_face_sad_walk_0);
        r_head_sad_walk.addFrame(R.drawable.r_face_sad_walk_1);
        r_head_sad_walk.addFrame(R.drawable.r_face_sad_walk_2);
        r_head_sad_walk.addFrame(R.drawable.r_face_sad_walk_1);

        r_eye_happy = new Animation(3);
        animations.add(r_eye_happy);
        animations_eye.add(r_eye_happy);
        r_eye_happy.addFrame(R.drawable.r_eye_happy);
        r_eye_happy.addFrame(R.drawable.r_eye_happy);
        r_eye_happy.addFrame(R.drawable.r_eye_happy_1);
        r_eye_happy.addFrame(R.drawable.r_eye_happy_2);

        r_eye_sad = new Animation(3);
        animations.add(r_eye_sad);
        animations_eye.add(r_eye_sad);
        r_eye_sad.addFrame(R.drawable.r_eye_sad_0);
        r_eye_sad.addFrame(R.drawable.r_eye_sad_0);
        r_eye_sad.addFrame(R.drawable.r_eye_sad_1);
        r_eye_sad.addFrame(R.drawable.r_eye_sad_2);
        r_eye_sad.addFrame(R.drawable.r_eye_sad_1);

        r_body_drag = new Animation(2);
        animations.add(r_body_drag);
        animations_body.add(r_body_drag);
        r_body_drag.addFrame(R.drawable.r_sheep_drag_0);
        r_body_drag.addFrame(R.drawable.r_sheep_drag_1);

        r_head_drag = new Animation();
        animations.add(r_head_drag);
//        animations_head.add(r_head_drag);
        r_head_drag.addFrame(R.drawable.r_face_drag);

        r_body_fall = new Animation();
        animations.add(r_body_fall);
        animations_body.add(r_body_fall);
        r_body_fall.addFrame(R.drawable.r_sheep_fall_0);
        r_body_fall.addFrame(R.drawable.r_sheep_fall_1);

        r_body_land = new Animation(2);
        animations.add(r_body_land);
        animations_body.add(r_body_land);
        r_body_land.addFrame(R.drawable.r_sheep_landing_0);
        r_body_land.addFrame(R.drawable.r_sheep_landing_0);
        r_body_land.addFrame(R.drawable.r_sheep_landing_1);
        r_body_land.addFrame(R.drawable.r_sheep_landing_1);
        r_body_land.addFrame(R.drawable.r_sheep_landing_2);
        r_body_land.addFrame(R.drawable.r_sheep_landing_2);

        r_head_fall = new Animation();
        animations.add(r_head_fall);
//        animations_head.add(r_head_fall);
        r_head_fall.addFrame(R.drawable.r_face_fall_0);
        r_head_fall.addFrame(R.drawable.r_face_fall_1);
        r_head_fall.addFrame(R.drawable.r_face_fall_2);

        r_head_land = new Animation(2);
        animations.add(r_head_land);
        animations_head.add(r_head_land);
        r_head_land.addFrame(R.drawable.r_face_landing_0);
        r_head_land.addFrame(R.drawable.r_face_landing_0);
        r_head_land.addFrame(R.drawable.r_face_landing_1);
        r_head_land.addFrame(R.drawable.r_face_landing_1);
        r_head_land.addFrame(R.drawable.r_face_landing_2);
        r_head_land.addFrame(R.drawable.r_face_landing_2);

        r_head_eat = new Animation(7);
        animations.add(r_head_eat);
        animations_head.add(r_head_eat);
        r_head_eat.addFrame(R.drawable.r_face_eat_0);
        r_head_eat.addFrame(R.drawable.r_face_eat_1);
        r_head_eat.addFrame(R.drawable.r_face_eat_1);

        r_head_chew = new Animation(5);
        animations.add(r_head_chew);
        animations_head.add(r_head_chew);
        r_head_chew.addFrame(R.drawable.r_face_chew_0);
        r_head_chew.addFrame(R.drawable.r_face_chew_1);
        r_head_chew.addFrame(R.drawable.r_face_chew_2);
        //endregion

        p_body = new SheepPos(0, 0, 100, 94);
        p_head = new SheepPos(165, 40, 64, 57);
        p_head_sad = new SheepPos(165, 40, 75, 58);
        p_r_head_sad = new SheepPos(165, 38, 75, 58);
        p_head_fall = new SheepPos(165, 40, 58, 95);
        p_body_drag = new SheepPos(90, 30, 92, 119);
        p_head_drag = new SheepPos(170, 35, 65, 71);
        p_tail_drag = new SheepPos(15, 40, 29, 27);
        p_eye = new SheepPos(150, 44, 31, 26);
        p_tail = new SheepPos(0, 40, 29, 27);
        p_shadow = new SheepPos(250, 30, 130, 32);
        p_state = new SheepPos(125, 70, 34, 35);
        p_head_eat = new SheepPos (165, 49, 64, 57);
        p_r_eat = new SheepPos(200, 43, 78, 90);
        p_eat = new SheepPos(195, 60, 78, 90);
    }

    public void clearActions() {
        isRest = false;
        isWalk = false;
        isFall = false;
        isLand = false;
        isEat = false;
        isChew = false;
    }

       @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        stateRun.updateForeObjectLocation(this);

        p_body.set(x, y, direction, ratio);
        p_head.set(x, y, direction, ratio);
        p_head_sad.set(x,y,direction,ratio);
        p_r_head_sad.set(x,y,direction,ratio);
        p_head_drag.set(x,y,direction,ratio);
        p_head_fall.set(x,y,direction,ratio);
        p_eye.set(x,y,direction,ratio);
        p_tail.set(x,y,direction,ratio);
        p_shadow.set(x,y,direction,ratio);
        p_state.set(x,y,direction,ratio);
        p_body_drag.set(x,y,direction,ratio);
        p_tail_drag.set(x,y,direction,ratio);
        p_head_eat.set(x,y,direction,ratio);
        p_eat.set(x,y,direction,ratio);
        p_r_eat.set(x,y,direction,ratio);
        //System.out.println(p_shadow.cy);

        for (Animation item : animations_body) item.setLocation(p_body.px, p_body.py);
        for (Animation item : animations_head) item.setLocation(p_head.px, p_head.py);
        for (Animation item : animations_eye) item.setLocation(p_eye.px, p_eye.py);
        tail.setLocation(p_tail.px, p_tail.py);
        r_tail.setLocation(p_tail.px, p_tail.py);
        r_head_sad_rest.setLocation(p_r_head_sad.px, p_r_head_sad.py);
        r_head_sad_walk.setLocation(p_r_head_sad.px, p_r_head_sad.py);
        head_drag.setLocation(p_head_drag.px, p_head_drag.py);
        head_fall.setLocation(p_head_fall.px, p_head_fall.py);
        r_head_drag.setLocation(p_head_drag.px, p_head_drag.py);
        r_head_fall.setLocation(p_head_fall.px, p_head_fall.py);
        sign_hungry.setLocation(p_state.px, p_state.py);
        body_drag.setLocation(p_body_drag.px, p_body_drag.py);
        r_body_drag.setLocation(p_body_drag.px, p_body_drag.py);
        head_eat.setLocation(p_head_eat.px, p_head_eat.py);
        //head_chew.setLocation(p_head_eat.px, p_head_eat.py);
        if (isDrag) {
            tail.setLocation(p_tail_drag.px, p_tail_drag.py);
            r_tail.setLocation(p_tail_drag.px, p_tail_drag.py);
        }
        if(y>210) {
            shadow.setLocation(p_shadow.px, p_shadow.py);
            r_shadow.setLocation(p_shadow.px, p_shadow.py);
        }else {
            shadow.setLocation(p_shadow.px, 220);
            r_shadow.setLocation(p_shadow.px, 220);
        }
    }

    @Override
    public int getY25D() {
        p_body.set(x,y,direction,ratio);
        return p_body.py;
    }

    private void setAnimation() {
        if (isRest) {
            for (Animation item : animations) item.setVisible(false);
            showState();
            if (direction) {
                body_rest.setVisible(true);
                tail.setVisible(true);
                shadow.setVisible(true);
                if (state.getState()==0) {
                    eye_happy.setVisible(true);
                    head_rest.setVisible(true);
                }
                else {
                    eye_sad.setVisible(true);
                    head_sad_rest.setVisible(true);
                }
            }else {
                r_body_rest.setVisible(true);
                r_tail.setVisible(true);
                r_shadow.setVisible(true);
                if (state.getState() == 0) {
                    r_eye_happy.setVisible(true);
                    r_head_rest.setVisible(true);
                }
                else {
                    r_eye_sad.setVisible(true);
                    r_head_sad_rest.setVisible(true);
                }
            }
        }
        else if (isWalk) {
            for (Animation item : animations) item.setVisible(false);
            showState();
            if (direction) {
                body_walk.setVisible(true);
                tail.setVisible(true);
                shadow.setVisible(true);
                if (state.getState()==0) {
                    eye_happy.setVisible(true);
                    head_walk.setVisible(true);
                }
                else {
                    eye_sad.setVisible(true);
                    head_sad_walk.setVisible(true);
                }
            }else {
                r_body_walk.setVisible(true);

                r_tail.setVisible(true);
                r_shadow.setVisible(true);
                if (state.getState()==0) {
                    r_eye_happy.setVisible(true);
                    r_head_walk.setVisible(true);
                }
                else {
                    r_eye_sad.setVisible(true);
                    r_head_sad_walk.setVisible(true);
                }
            }

        }
        else if (isDrag) {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {
                body_drag.setVisible(true);
                head_drag.setVisible(true);
                tail.setVisible(true);
                shadow.setVisible(true);
            }else {
                r_body_drag.setVisible(true);
                r_head_drag.setVisible(true);
                r_tail.setVisible(true);
                r_shadow.setVisible(true);
            }
        }
        else if (isLand) {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {
                body_land.setVisible(true);
                head_land.setVisible(true);
                tail.setVisible(true);
                shadow.setVisible(true);
            }else {
                r_body_land.setVisible(true);
                r_head_land.setVisible(true);
                r_tail.setVisible(true);
                r_shadow.setVisible(true);
            }
        }
        else if (isFall) {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {
                body_fall.setVisible(true);
                head_fall.setVisible(true);
                tail.setVisible(true);
                shadow.setVisible(true);
            }else {
                r_body_fall.setVisible(true);
                r_head_fall.setVisible(true);
                r_tail.setVisible(true);
                r_shadow.setVisible(true);
            }
        }
        else if (isEat) {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {
                body_rest.setVisible(true);
                head_eat.setVisible(true);
                tail.setVisible(true);
                shadow.setVisible(true);
            }else {
                r_body_rest.setVisible(true);
                r_head_eat.setVisible(true);
                r_tail.setVisible(true);
                r_shadow.setVisible(true);
            }
        }
        else if (isChew) {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {

                body_rest.setVisible(true);
                head_chew.setVisible(true);
                tail.setVisible(true);
                shadow.setVisible(true);
            }else {
                r_body_rest.setVisible(true);
                r_head_chew.setVisible(true);
                r_tail.setVisible(true);
                r_shadow.setVisible(true);
            }
        }
        else {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {
                body_rest.setVisible(true);
                head_rest.setVisible(true);
                tail.setVisible(true);
                shadow.setVisible(true);
            }else {
                r_body_rest.setVisible(true);
                r_head_rest.setVisible(true);
                r_tail.setVisible(true);
                r_shadow.setVisible(true);
            }
        }

    }

    private void blink() {

        if (isBlink){
            if (--blinkTimeCount <= 0) {
                blinkTimeCount = BLINK_TIME;
                isBlink = false;
            }
        }
        else {
            eye_happy.reset();
            r_eye_happy.reset();
            eye_sad.reset();
            r_eye_sad.reset();
            if (--blinkCount <= 0) {
                blinkCount = BLINK_DELAY;
                isBlink = true;
            }
        }
    }
    public void rest() {
        chewCount = 0;
        body_land.reset();
        head_land.reset();
        r_body_land.reset();
        r_head_land.reset();
        clearActions();
        isRest = true;
        setAnimation();
    }
    public void walk() {
        clearActions();
        isWalk = true;
        setAnimation();

        if (--walkCount <= 0) {
            walkDir = Math.random()*100;
            if (walkDir < 6.25) {rx = 0.924; ry = -0.383;}
            else if (walkDir < 12.5) {rx = 0.707; ry = -0.707;}
            else if (walkDir < 18.75) {rx = 0.383; ry = -0.924;}
            else if (walkDir < 25) {rx = 0; ry = -1;}
            else if (walkDir < 61.25) {rx = -0.383; ry = -0.924;}
            else if (walkDir < 37.5) {rx = ry = -0.707;}
            else if (walkDir < 43.75) {rx = -0.924; ry = -0.383;}
            else if (walkDir < 50) {rx = -1; ry = 0;}
            else if (walkDir < 56.25) {rx = -0.924; ry = 0.383;}
            else if (walkDir < 62.5) {rx = -0.707; ry = 0.707;}
            else if (walkDir < 68.75) {rx = -0.383; ry = 0.924;}
            else if (walkDir < 75) {rx = 0; ry = 1;}
            else if (walkDir < 81.25) {rx = 0.383; ry = 0.924;}
            else if (walkDir < 87.5) {rx = ry = 0.707;}
            else if (walkDir < 93.75) {rx = 0.924; ry = 0.383;}
            else {rx = 1; ry = 0;}
            direction = rx < 0;
            walkCount = WALK_DELAY;
        }
        if(!stateRun.isGrabbingMap) {

            if (y>210 && y<300) {
                x = (int)(x + rx);
                y = (int)(y + ry);
                this.setLocation(x, y);
            }
            else this.rest();
        }
        else {
            int tmpX, tmpY;
            tmpX = (int)(moveX + rx);
            tmpY = (int)(moveY + ry);
            if (y>210 && y<300) {
                moveX = tmpX;
                moveY = tmpY;
            }
            else this.rest();
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
        setAnimation();

        if (y < 210){
            if (!stateRun.isGrabbingMap) this.setLocation(x, y+=20);
            else y += 20;
        }
        else if (y < tmpY+10) {
            if (!stateRun.isGrabbingMap) this.setLocation(x, y+=5);
            else y += 5;
        }
        else {
            dragRelease = false;
            isFall = false;
            isLand = true;
        }
    }
    public void land() {
        if (tmpY +10 > 210) y = tmpY + 5;
        else y = 210;
        setLocation(x, y);
        isLand = true;
        setAnimation();
        if (body_land.isLastFrame() || r_body_land.isLastFrame()) this.rest();

    }
    public void eat() {
        if(--eatCount <= 0){
            eatCount = EAT_DELAY;
            chewCount++;
        }
        if (chewCount >= 2){
            chewCount = 0;
            arriveGrass = false;
            isChew = false;
            state.satisfy(1);
            this.rest();
        }
        //clearActions();
        setLocation(x, y);
        isEat = true;
        setAnimation();
        if (head_eat.isLastFrame() || r_head_eat.isLastFrame()) {
            isEat = false;
            isChew = true;
            this.chew();
        }
    }
    public void chew() {
        head_eat.reset();
        r_head_eat.reset();

        if (chewCount < 2) {
            setLocation(x, y);
            isChew = true;
            setAnimation();
            //System.out.println("timer" + chewTimer);
            if (--chewTimer<=0) {
                chewTimer = CHEW_DELAY;
                this.eat();
            }

        }
        else {
            chewCount = 0;
            arriveGrass = false;
            isChew = false;
            state.satisfy(1);
            this.rest();
        }
    }
    public void showState() {
        if (state.getState()==1) {
            sign_hungry.setVisible(true);
        }
        else sign_hungry.setVisible(false);
    }

    private void aiMove() {
        this.showState();
        if (this.dragging) {
            instr = 0;
            this.drag();
            dragRelease = true;
            tmpY = y;
        }
        else {
            isDrag = false;
            if (dragRelease) {
                releaseY = y;
                this.fall();

            }
            else if (isLand) {
                this.land();
            }
            else {

                if (calcGrassDistance(stateRun.grass) >= GRASS_SHORTEST_DISTANCE || state.getState()==0){
                    this.blink();
                    if (--aiCount <= 0) {
                        aiCount = TIME_DELAY;
                        instr = (int) (Math.random() * 100);
                    }
                    if (instr > 33) this.walk();
                    else this.rest();
                }
                else if (state.getState()==1) {

                    this.blink();
                    if (isEat) this.eat();
                    else if (isChew) this.chew();
                    else this.walkToGrass();
                }
            }
        }
    }

    @Override
    public void move() {
        state.work();
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

    @Override
    public void dragPressed(Pointer pointer){
        super.dragPressed(pointer);
        state.satisfy(3);
        initImageX = this.getX();
        initImageY = this.getY();
        initialPointerX = pointer.getX();
        initialPointerY = pointer.getY();
    }

    @Override
    public void dragMoved(Pointer pointer) {
        super.dragMoved(pointer);
        int deltaX = pointer.getX() - initialPointerX;
        int deltaY = pointer.getY() - initialPointerY;
        x = initImageX + deltaX;
        y = initImageY + deltaY;
        this.setLocation(x, y);
    }

    @Override
    public void resize(double ratio){
        if (--resizeTimer <= 0) {
            resizeTimer = RESIZE_DELAY;

            System.out.println(ratio);
            if (y > 210 && !isFall && !isLand) {
                this.ratio = ratio;
                width = (int) (oriWidth * ratio);
                height = (int) (oriHeight * ratio);
                for (Animation item : animations) {
                    item.resize(ratio * 0.8);
                }
            }
            else if (y < 210) {
                this.ratio = 0.7352;
                width = (int) (oriWidth * this.ratio);
                height = (int) (oriHeight * this.ratio);
                for (Animation item : animations) {
                    item.resize(this.ratio * 0.8);
                }
            }
        }
    }

    public double calcGrassDistance(Grass grass){
        double disHor, disVer;

        if (direction) {
            disHor = (double) grass.getX() - ((double) p_eat.cx);
            disVer = (double) grass.getY() - ((double) p_eat.cy);
        }
        else {
            disHor = (double) grass.getX() - ((double) p_r_eat.cx);
            disVer = (double) grass.getY() - ((double) p_r_eat.cy);
        }

        return (Math.sqrt(disHor * disHor + disVer * disVer));
    }

    public void walkToGrass(){
        if (calcGrassDistance(stateRun.grass) < GRASS_SHORTEST_DISTANCE) {

            clearActions();
            isWalk = true;
            setAnimation();

            if (calcGrassDistance(stateRun.grass) >= 1)
                setLocation(x,y);
            else {
                clearActions();
                isEat = true;
                this.eat();
            }
            if ((direction && ((stateRun.grass.getY() - p_eat.cy)!=0 || (stateRun.grass.getX() - p_eat.cx)!=0))
                    || (!direction && ((stateRun.grass.getY() - p_r_eat.cy)!=0 || (stateRun.grass.getX() - p_r_eat.cx)!=0))) {

                if (direction) {
                    angle = Math.atan(((double) (stateRun.grass.getY() - p_eat.cy) / (double) (stateRun.grass.getX() - p_eat.cx)));
                    if ((stateRun.grass.getY() - p_eat.cy) >= 0 && (stateRun.grass.getX() - p_eat.cx) < 0)
                        angle = -Math.toDegrees(angle) + 180;
                    else if ((stateRun.grass.getY() - p_eat.cy) >= 0 && (stateRun.grass.getX() - p_eat.cx) >= 0)
                        angle = -Math.toDegrees(angle) + 360;
                    else if ((stateRun.grass.getY() - p_eat.cy) < 0 && (stateRun.grass.getX() - p_eat.cx) >= 0)
                        angle = -Math.toDegrees(angle);
                    else
                        angle = -Math.toDegrees(angle) + 180;
                }
                else {
                    angle = Math.atan(((double) (stateRun.grass.getY() - p_r_eat.cy) / (double) (stateRun.grass.getX() - p_r_eat.cx)));
                    if ((stateRun.grass.getY() - p_r_eat.cy) >= 0 && (stateRun.grass.getX() - p_r_eat.cx) < 0)
                        angle = -Math.toDegrees(angle) + 180;
                    else if ((stateRun.grass.getY() - p_r_eat.cy) >= 0 && (stateRun.grass.getX() - p_r_eat.cx) >= 0)
                        angle = -Math.toDegrees(angle) + 360;
                    else if ((stateRun.grass.getY() - p_r_eat.cy) < 0 && (stateRun.grass.getX() - p_r_eat.cx) >= 0)
                        angle = -Math.toDegrees(angle);
                    else
                        angle = -Math.toDegrees(angle) + 180;
                }
                if (angle == 270){
                    lx = 0;
                    ly = -1;
                }
                else if (angle == 90){
                    lx = 0;
                    ly = -1;
                }
                else {
                    angle = Math.toRadians(angle);
                    lx = Math.cos(angle);
                    ly = Math.sin(angle);
                }
                System.out.println(calcGrassDistance(stateRun.grass) + " " + Math.toDegrees(angle) + " " + lx + " " + ly);

                direction = (lx < 0);

                x += lx;
                y -= ly;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
