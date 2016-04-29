package tw.edu.ntut.csie.game.object;

import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.state.StateRun;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class Cloud extends MovableGameObject {
    public static final int SHAKE_THRESHOLD = 45;
    public static final int MOVE_DETECT_DELAY = 5;
    public static final int CMP_MAX_XY = 1000;
    
    public static final int TYPE_WHITE = 1; // 白雲
    public static final int TYPE_GRAY = 2; // 烏雲
    public static final int TYPE_BLACK = 3; // 雨雲
    public static final int LEVEL_BIG = 1;
    public static final int LEVEL_MEDIUM = 2;
    public static final int LEVEL_SMALL = 3;
    private static final int CLOUD_IMAGE[][] = {
            {R.drawable.cloud_white1_1, R.drawable.cloud_white1_2, R.drawable.cloud_white1_3},
            {R.drawable.cloud_gray1_1, R.drawable.cloud_gray1_2, R.drawable.cloud_gray1_3},
            {R.drawable.cloud_black1_1, R.drawable.cloud_black1_2, R.drawable.cloud_black1_3}
    };

    private StateRun appStateRun;
    private int type;
    private int level; // (1~3): how big it is
    private int speed = 1; // (<0): moving left
    private MovingBitmap image;
    private int moveEventCounter = 0;
    private int moveMaxX = 0, moveMinX = CMP_MAX_XY, moveMaxY = 0, moveMinY = CMP_MAX_XY;

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
        // appStateRun.updateForeObjectLocation(this, x, y);
    }

    public void resize(double ratio) {
        super.resize(ratio);
        image.resize((int)(width*ratio), (int)(height*ratio));
    }

    @Override
    public void dragMoved(Pointer pointer) {
        moveMaxX = pointer.getX() > moveMaxX ? pointer.getX() : moveMaxX;
        moveMinX = pointer.getX() < moveMinX ? pointer.getX() : moveMinX;
        moveMaxY = pointer.getY() > moveMaxY ? pointer.getY() : moveMaxY;
        moveMinY = pointer.getY() < moveMinY ? pointer.getY() : moveMinY;

        int newX = initialX + pointer.getX() - initialPointerX;
        int newY = initialY + pointer.getY() - initialPointerY;
        this.setLocation(newX, newY);
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

    @Override
    public void move() {
        moveEventCounter = (moveEventCounter + 1) % MOVE_DETECT_DELAY;
        if (moveEventCounter == 0) {
            if (moveMaxX - moveMinX > SHAKE_THRESHOLD || moveMaxY - moveMinY > SHAKE_THRESHOLD) {
                System.out.println("Cloud shake exceed threshold!");
                System.out.printf("moveX=%d\n", moveMaxX - moveMinX);
            }
            moveMaxX = moveMaxY = 0;
            moveMinX = moveMinY = CMP_MAX_XY;
        }

//        if (!appStateRun.isGrabbingMap && !dragging) {
//            this.setLocation(x + speed, y);
//        }
    }

    @Override
    public void show() {
        image.show();
    }

    @Override
    public void release() {
        image.release();
        image = null;
    }

    private void updateImageFromState() {
        if (image != null)
            image.release();

        // TODO: Bumping animation
        image = new MovingBitmap(CLOUD_IMAGE[type - 1][level - 1], x, y);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
}
