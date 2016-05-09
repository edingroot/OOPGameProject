package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.state.StateRun;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class Cloud extends MovableGameObject {
    public static final int SHAKE_THRESHOLD = 100;
    public static final int SHAKE_COUNT_THRESHOLD = 8;
    public static final int SHAKE_CHECK_DELAY = 1;
    public static final int CMP_MAX_XY = 1000;
    
    public static final int TYPE_WHITE = 1; // 白雲
    public static final int TYPE_GRAY = 2; // 烏雲
    public static final int TYPE_BLACK = 3; // 雨雲
    public static final int LEVEL_BIG = 3;
    public static final int LEVEL_MEDIUM = 2;
    public static final int LEVEL_SMALL = 1;
    private static final int CLOUD_IMAGE[][] = {
            {R.drawable.cloud_white1_3, R.drawable.cloud_white1_2, R.drawable.cloud_white1_1},
            {R.drawable.cloud_gray1_3, R.drawable.cloud_gray1_2, R.drawable.cloud_gray1_1},
            {R.drawable.cloud_black1_3, R.drawable.cloud_black1_2, R.drawable.cloud_black1_1}
    };

    private StateRun appStateRun;
    private int type;
    private int level; // (1~3): how big it is
    private int speed = 1; // (<0): moving left
    private boolean raining = false;
    private MovingBitmap image;
    private Rain rain;
    private int checkShakeCounter = 0, directionChangedCounter = 0;
    private int lastDragDirection = 1; // right: 1; left: -1
    private int lastCheckX = CMP_MAX_XY;

    public Cloud(StateRun appStateRun, int x, int y, int type, int level) {
        this.appStateRun = appStateRun;
        this.x = x;
        this.y = y;
        this.type = type;
        this.level = level;
        updateImageFromState();
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        image.setLocation(x, y);
        appStateRun.updateForeObjectLocation(this, x, y);
        if (raining) {
            rain.setLocation(x, y + this.height);
        }
    }

    public void resize(double ratio) {
//        super.resize(ratio);
//        image.resize((int)(width*ratio), (int)(height*ratio));
    }

    @Override
    public void dragMoved(Pointer pointer) {
        int newX = initialX + pointer.getX() - initialPointerX;
        int newY = initialY + pointer.getY() - initialPointerY;
        this.setLocation(newX, newY);
        if (raining) {
            rain.setLocation(newX, newY + this.height);
        }
    }

    @Override
    public void dragReleased(Pointer pointer) {
        super.dragReleased(pointer);

        // determine moving direction after dragging
        if (speed * lastDragDirection < 0)
            speed = -speed;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean rainning) {
        if (rainning) {
            rain = new Rain(this.x, this.y + this.height, this.width, 300);
        } else {
            rain.release();
            rain = null;
        }
        this.raining = rainning;
    }

    @Override
    public void move() {
        detectShaking();

        if (!appStateRun.isGrabbingMap && !dragging) {
            int newX = x + speed;
            this.setLocation(newX, y);
            if (raining) {
                 rain.setLocation(newX, y);
            }
        }
        if (raining) {
            rain.move();
        }
    }

    @Override
    public void show() {
        image.show();
        if (raining) {
            rain.show();
        }
    }

    @Override
    public void release() {
        image.release();
        image = null;
        if (rain != null) {
            rain.release();
            rain = null;
        }
    }

    private void updateImageFromState() {
        if (image != null)
            image.release();

        // TODO: Bumping animation
        image = new MovingBitmap(CLOUD_IMAGE[type - 1][level - 1], x, y);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    private void detectShaking() {
        checkShakeCounter = (checkShakeCounter + 1) % SHAKE_CHECK_DELAY;
        if (!dragging || checkShakeCounter != 0)
            return;

        if (lastCheckX == CMP_MAX_XY) {
            lastCheckX = this.x;
            return;
        }

        int delta = this.x - lastCheckX;
        if (Math.abs(delta) < SHAKE_THRESHOLD) {
            if (lastDragDirection * delta < 0) { // different direction
                lastDragDirection = -lastDragDirection;
                directionChangedCounter = (directionChangedCounter + 1) % SHAKE_COUNT_THRESHOLD;
                if (directionChangedCounter == 0) {
                    System.out.println("Cloud shake exceed threshold!");
                    spreadToClouds();
                }
            }
        } else {
            checkShakeCounter = 0;
        }
        lastCheckX = this.x;
    }

    private void spreadToClouds() {
        if (level == LEVEL_SMALL)
            return;

        int newType = type == TYPE_WHITE ? TYPE_WHITE : type - 1;
        int x1 = this.x, x2 = this.x + this.width / 3;
        appStateRun.addToForeObjectTable(new Cloud(appStateRun, x1, y, newType, level - 1));
        appStateRun.addToForeObjectTable(new Cloud(appStateRun, x2, y, newType, level - 1));
    }
}
