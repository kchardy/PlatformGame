package com.kchardy.game.entity.powerup;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;

import java.awt.*;

public class Staff extends Entity {
    public Staff(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Game.staff.getBufferedImage(), getX(), getY(), width, height, null);
    }

    @Override
    public void tick() {

    }
}
