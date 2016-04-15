package tw.edu.ntut.csie.game.object;

import java.util.ArrayList;
import java.util.List;

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
    private static final int R_HEAD_SHIFT_POS_X = 60;
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
    //endregion

    private static final int TIME_DELAY = 100;
    private static final int WALK_DELAY = 50;
    private static final int BLINK_DELAY = 150, BLINK_TIME = 10;
    private SheepState state;

    //region animationDeclaration
    private Animation body_rest, body_walk, head_rest, head_walk, head_drag, body_drag, tail, eye_happy, body_fall, body_land;
    private Animation head_fall, head_land;
    private Animation head_sad_rest, head_sad_walk, eye_sad;
    private Animation r_body_rest, r_body_walk, r_head_rest, r_head_walk, r_head_drag, r_body_drag, r_tail, r_eye_happy, r_body_fall, r_body_land;
    private Animation r_head_fall, r_head_land;
    private Animation r_head_sad_rest, r_head_sad_walk, r_eye_sad;

    private List<Animation> animations;
    private List<Animation> animations_body;
    private List<Animation> animations_head;
    private List<Animation> animations_eye;
    private List<Animation> animations_tail;
    //endregion

    private StateRun stateRun;
    private int id;
    private boolean isWalk, isRest, isDrag, isFall, isLand;
    private boolean direction; // true: Left

    private int walkCount=0, aiCount=0, blinkCount=0, blinkTimeCount=0, instr=0;
    private int initImageX, initImageY, releaseY, initialPointerX, initialPointerY;
    private boolean dragRelease;
    private boolean isBlink;

    public Sheep(StateRun stateRun, int x, int y, int id) {
        this(stateRun, x, y);
        this.id = id;
    }

    public Sheep(StateRun stateRun, int x, int y) {
        this.width = 100;
        this.height = 100;
        this.stateRun = stateRun;
        this.direction = true;
        this.x = x;
        this.y = y;

        isWalk = false;
        isDrag = false;
        isWalk = false;
        isFall = false;
        isLand = false;

        state = new SheepState();

        animations = new ArrayList<>();
        animations_body = new ArrayList<>();
        animations_head = new ArrayList<>();
        animations_eye = new ArrayList<>();
        animations_tail = new ArrayList<>();


        //region AnimationLeft

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
        animations_head.add(head_drag);
        head_drag.addFrame(R.drawable.face_drag);

        body_fall = new Animation();
        animations.add(body_fall);
        animations_body.add(body_fall);
        body_fall.addFrame(R.drawable.sheep_fall_0);
        body_fall.addFrame(R.drawable.sheep_fall_1);

        body_land = new Animation(2);
        animations.add(body_land);
//        animations_body.add(body_land);
        body_land.addFrame(R.drawable.sheep_landing_0);
        body_land.addFrame(R.drawable.sheep_landing_0);
        body_land.addFrame(R.drawable.sheep_landing_1);
        body_land.addFrame(R.drawable.sheep_landing_1);
        body_land.addFrame(R.drawable.sheep_landing_2);
        body_land.addFrame(R.drawable.sheep_landing_2);

        head_fall = new Animation();
        animations.add(head_fall);
        animations_head.add(head_fall);
        head_fall.addFrame(R.drawable.face_fall_0);
        head_fall.addFrame(R.drawable.face_fall_1);
        head_fall.addFrame(R.drawable.face_fall_2);

        head_land = new Animation(2);
        animations.add(head_land);
//        animations_head.add(head_land);
        head_land.addFrame(R.drawable.face_landing_0);
        head_land.addFrame(R.drawable.face_landing_0);
        head_land.addFrame(R.drawable.face_landing_1);
        head_land.addFrame(R.drawable.face_landing_1);
        head_land.addFrame(R.drawable.face_landing_2);
        head_land.addFrame(R.drawable.face_landing_2);

        //endregion

        //region AnimationRight

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
//        animations_head.add(r_head_sad_rest);
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
//        animations_head.add(r_head_sad_walk);
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
        animations_head.add(r_head_drag);
        r_head_drag.addFrame(R.drawable.r_face_drag);
//        this.setLocation(x, y);

        r_body_fall = new Animation();
        animations.add(r_body_fall);
        animations_body.add(r_body_fall);
        r_body_fall.addFrame(R.drawable.r_sheep_fall_0);
        r_body_fall.addFrame(R.drawable.r_sheep_fall_1);

        r_body_land = new Animation(2);
        animations.add(r_body_land);
//        animations_body.add(r_body_land);
        r_body_land.addFrame(R.drawable.r_sheep_landing_0);
        r_body_land.addFrame(R.drawable.r_sheep_landing_0);
        r_body_land.addFrame(R.drawable.r_sheep_landing_1);
        r_body_land.addFrame(R.drawable.r_sheep_landing_1);
        r_body_land.addFrame(R.drawable.r_sheep_landing_2);
        r_body_land.addFrame(R.drawable.r_sheep_landing_2);

        r_head_fall = new Animation();
        animations.add(r_head_fall);
        animations_head.add(r_head_fall);
        r_head_fall.addFrame(R.drawable.r_face_fall_0);
        r_head_fall.addFrame(R.drawable.r_face_fall_1);
        r_head_fall.addFrame(R.drawable.r_face_fall_2);

        r_head_land = new Animation(2);
        animations.add(r_head_land);
//        animations_head.add(r_head_land);
        r_head_land.addFrame(R.drawable.r_face_landing_0);
        r_head_land.addFrame(R.drawable.r_face_landing_0);
        r_head_land.addFrame(R.drawable.r_face_landing_1);
        r_head_land.addFrame(R.drawable.r_face_landing_1);
        r_head_land.addFrame(R.drawable.r_face_landing_2);
        r_head_land.addFrame(R.drawable.r_face_landing_2);

        //endregion
    }

    public void clearActions() {
        isRest = false;
        isWalk = false;
        isFall = false;
        isLand = false;
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        stateRun.updateForeObjectLocation(this, x, y);

        for (Animation item : animations_body) {
            item.setLocation(x, y);
        }
        for (Animation item : animations_head) {
            if(direction) item.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y);
            else item.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y);
        }
        for (Animation item : animations_eye) {
            if(direction) item.setLocation(x - HEAD_SHIFT_POS_X + EYE_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + EYE_SHIFT_POS_Y);
            else item.setLocation(x + R_HEAD_SHIFT_POS_X + R_EYE_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + EYE_SHIFT_POS_Y);
        }
        if (direction) head_drag.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_DRAG_SHIFT_POS_Y);
        else r_head_drag.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_DRAG_SHIFT_POS_Y);

        if (direction) head_fall.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y - HEAD_FALL_SHIFT_POS_Y);
        else r_head_fall.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y - HEAD_FALL_SHIFT_POS_Y);

        //region tailSetLocation
        if (direction) {
            if (isRest) tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y);
            else if (isWalk) {
                switch (body_walk.getCurrentFrameIndex()){
                    case 0:
                        tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y - TAIL_SHAKE_Y);
                        break;
                    case 1:
                        tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_SHAKE_Y);
                        break;
                    case 2:
                        tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y - TAIL_SHAKE_Y);
                        break;
                    case 3:
                        tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_SHAKE_Y);
                        break;
                    default:
                        break;
                }
            }
            else if (isDrag) tail.setLocation(x + TAIL_SHIFT_X - TAIL_DRAG_POS_X, y + TAIL_SHIFT_Y + TAIL_DRAG_POS_Y);
            else if (isFall) tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y);
            else if (isLand) {
                switch (body_land.getCurrentFrameIndex()){
                    case 0:
                        tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_LAND_POS_Y0);
                        break;
                    case 1:
                        tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y);
                        break;
                    case 2:
                        tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_LAND_POS_Y2);
                        break;
                    case 3:
                        tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_LAND_POS_Y2);
                        break;
                    default:
                        break;
                }
            }
            else tail.setLocation(x + TAIL_SHIFT_X, y + TAIL_SHIFT_Y);
        }
        else {
            if (isRest) r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y);
            else if (isWalk) {
                switch (r_body_walk.getCurrentFrameIndex()) {
                    case 0:
                        r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y - TAIL_SHAKE_Y);
                        break;
                    case 1:
                        r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_SHAKE_Y);
                        break;
                    case 2:
                        r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y - TAIL_SHAKE_Y);
                        break;
                    case 3:
                        r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_SHAKE_Y);
                        break;
                    default:
                        break;
                }
            }
            else if (isDrag) r_tail.setLocation(x - R_TAIL_SHIFT_X + TAIL_DRAG_POS_X, y + TAIL_SHIFT_Y + TAIL_DRAG_POS_Y);
            else if (isFall) r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y);
            else if (isLand) {
                switch (r_body_land.getCurrentFrameIndex()) {
                    case 0:
                        r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_LAND_POS_Y0);
                        break;
                    case 1:
                        r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y);
                        break;
                    case 2:
                        r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_LAND_POS_Y2);
                        break;
                    case 3:
                        r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y + TAIL_LAND_POS_Y2);
                        break;
                    default:
                        break;
                }
            }
            else r_tail.setLocation(x - R_TAIL_SHIFT_X, y + TAIL_SHIFT_Y);
        }
        //endregion

        //region particularLocation
        r_head_sad_walk.setLocation(x + R_HEAD_SAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y);
        r_head_sad_rest.setLocation(x + R_HEAD_SAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y);

        int HEAD_SHIFT0 = 25, HEAD_SHIFT1 = 50, HEAD_SHIFT2 = 20, HEAD_SHIFT3 = -10, HEAD_SHIFT4 = -20, HEAD_SHIFT5 = -30;
        int BODY_SHIFT0 = 15, BODY_SHIFT1 = 20, BODY_SHIFT2 = 10, BODY_SHIFT3 = -10, BODY_SHIFT4 = -20, BODY_SHIFT5 = -30;
        if (direction) {
            switch (body_land.getCurrentFrameIndex()) {
                case 0:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT0);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT0);
                    body_land.setLocation(x, y + BODY_SHIFT0);
                    r_body_land.setLocation(x, y + BODY_SHIFT0);
                    break;
                case 1:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT1);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT1);
                    body_land.setLocation(x, y + BODY_SHIFT1);
                    r_body_land.setLocation(x, y + BODY_SHIFT1);
                    break;
                case 2:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT2);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT2);
                    body_land.setLocation(x, y + BODY_SHIFT2);
                    r_body_land.setLocation(x, y + BODY_SHIFT2);
                    break;
                case 3:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT3);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT3);
                    body_land.setLocation(x, y + BODY_SHIFT3);
                    r_body_land.setLocation(x, y + BODY_SHIFT3);
                    break;
                case 4:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT4);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT4);
                    body_land.setLocation(x, y + BODY_SHIFT4);
                    r_body_land.setLocation(x, y + BODY_SHIFT4);
                    break;
                case 5:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT5);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT5);
                    body_land.setLocation(x, y + BODY_SHIFT5);
                    r_body_land.setLocation(x, y + BODY_SHIFT5);
                    break;
                default:
                    break;
            }
        }
        else {
            switch (r_body_land.getCurrentFrameIndex()) {
                case 0:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT0);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT0);
                    body_land.setLocation(x, y + BODY_SHIFT0);
                    r_body_land.setLocation(x, y + BODY_SHIFT0);
                    break;
                case 1:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT1);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT1);
                    body_land.setLocation(x, y + BODY_SHIFT1);
                    r_body_land.setLocation(x, y + BODY_SHIFT1);
                    break;
                case 2:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT2);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT2);
                    body_land.setLocation(x, y + BODY_SHIFT2);
                    r_body_land.setLocation(x, y + BODY_SHIFT2);
                    break;
                case 3:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT3);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT3);
                    body_land.setLocation(x, y + BODY_SHIFT3);
                    r_body_land.setLocation(x, y + BODY_SHIFT3);
                    break;
                case 4:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT4);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT4);
                    body_land.setLocation(x, y + BODY_SHIFT4);
                    r_body_land.setLocation(x, y + BODY_SHIFT4);
                    break;
                case 5:
                    head_land.setLocation(x - HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT5);
                    r_head_land.setLocation(x + R_HEAD_SHIFT_POS_X, y + HEAD_SHIFT_POS_Y + HEAD_SHIFT5);
                    body_land.setLocation(x, y + BODY_SHIFT5);
                    r_body_land.setLocation(x, y + BODY_SHIFT5);
                    break;
                default:
                    break;
            }
        }
        //endregion
    }

    private void setAnimation() {
        if (isRest) {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {
                body_rest.setVisible(true);
                tail.setVisible(true);
                if (state.getState() == "happy") {
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
                if (state.getState() == "happy") {
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

            if (direction) {
                body_walk.setVisible(true);
                tail.setVisible(true);
                if (state.getState() == "happy") {
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
                if (state.isGoodMood()) {
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
            }else {
                r_body_drag.setVisible(true);
                r_head_drag.setVisible(true);
                r_tail.setVisible(true);
            }
        }
        else if (isLand) {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {
                body_land.setVisible(true);
                head_land.setVisible(true);
                tail.setVisible(true);
            }else {
                r_body_land.setVisible(true);
                r_head_land.setVisible(true);
                r_tail.setVisible(true);
            }
        }
        else if (isFall) {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {
                body_fall.setVisible(true);
                head_fall.setVisible(true);
                tail.setVisible(true);
            }else {
                r_body_fall.setVisible(true);
                r_head_fall.setVisible(true);
                r_tail.setVisible(true);
            }
        }
        else {
            for (Animation item : animations) item.setVisible(false);

            if (direction) {
                body_rest.setVisible(true);
                head_rest.setVisible(true);
                tail.setVisible(true);
            }else {
                r_body_rest.setVisible(true);
                r_head_rest.setVisible(true);
                r_tail.setVisible(true);
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
            if (Math.random()*10 > 5) direction = true;
            else direction = false;
            walkCount = WALK_DELAY;
        }
        if(!stateRun.isGrabbingMap) {
            if (direction) this.setLocation(--x, y);
            else this.setLocation(++x, y);
        }
        else {
            if (direction) --moveX;
            else ++moveX;
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

        if (y < 260) {
            if (!stateRun.isGrabbingMap) this.setLocation(x, y+=20);
            else y += 20;
        }
        else {
            dragRelease = false;
            isFall = false;
            isLand = true;
        }
    }
    public void land() {
        y = 250;
        setLocation(x, y);
        isLand = true;
        setAnimation();
        if (body_land.isLastFrame() || r_body_land.isLastFrame()) this.rest();

    }


    private void aiMove() {
        if (this.dragging) {
            instr = 0;
            this.drag();
            dragRelease = true;
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
                this.blink();
                if (--aiCount <= 0) {
                    aiCount = TIME_DELAY;
                    instr = (int)(Math.random()*100);
                }
                if (instr > 33) this.walk();
                else this.rest();
            }
        }
    }

    @Override
    public void move() {
        state.healthDecline();
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
        state.setHealth(100);
        initImageX = this.getX();
        initImageY = this.getY();
        initialPointerX = pointer.getX();
        initialPointerY = pointer.getY();
    }

    @Override
    public void dragMoved(Pointer pointer){
        int deltaX = pointer.getX() - initialPointerX;
        int deltaY = pointer.getY() - initialPointerY;
        x = initImageX + deltaX;
        y = initImageY + deltaY;
        this.setLocation(x, y);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
