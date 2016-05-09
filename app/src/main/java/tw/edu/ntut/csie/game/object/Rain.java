package tw.edu.ntut.csie.game.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.util.MovableGameObject;

public class Rain extends MovableGameObject {
    private static final int DROP_WIDTH = 17;
    private static final int DROP_HEIGHT = 28;
    private static final int FALLING_SPEED = 8;
    private final List<MovingBitmap> drops;

    public Rain(int x, int y, int width, int height) {
        this.draggable = false;
        drops = new ArrayList<>();

        this.width = width;
        this.height = height;
        this.setLocation(x, y);
        generateDrops();
    }

    public void setLocation(int x, int y) {
        int deltaX = x - this.x;
        int deltaY = y - this.y;
        for (MovingBitmap drop : drops) {
            drop.setLocation(drop.getX() + deltaX, drop.getY() + deltaY);
        }
        super.setLocation(x, y);
    }

    @Override
    public void move() {
        Iterator<MovingBitmap> it = drops.iterator();
        while (it.hasNext()) {
            MovingBitmap drop = it.next();
            int fallY = FALLING_SPEED + (int) (Math.random() * 5);
            if (drop.getY() + DROP_HEIGHT + fallY <= this.y + this.height) {
                drop.setLocation(drop.getX(), drop.getY() + fallY);
            } else {
                it.remove();
                drop.release();
                drop = null;
            }
        }
        generateDrops();
    }

    @Override
    public void show() {
        for (MovingBitmap drop : drops) {
            drop.show();
        }
    }

    @Override
    public void release() {
        for (MovingBitmap drop : drops) {
            drop.release();
            drop = null;
        }
    }

    private void generateDrops() {
        for (int i = this.x; i + DROP_WIDTH <= this.x + this.width; i += DROP_WIDTH) {
            if (Math.floor(Math.random() * 3) == 0) {
                drops.add(new MovingBitmap(R.drawable.raindrop, i, this.y));
            }
        }
    }
}
