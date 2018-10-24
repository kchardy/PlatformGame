package com.kchardy.game.tile;

import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.mob.Trap;

import java.awt.*;

public class Ladder extends Tile {

    public Ladder(int x, int y, int width, int height, boolean solid, Id id, Handler handler, int facing, boolean trap) {
        super(x, y, width, height, solid, id, handler);
        this.facing = facing;

        if(trap)
        {
            handler.addEntity(new Trap(getX(), getY()+64, width, 64, Id.trap, handler));
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(128,128,128));
        g.fillRect(x, y, width, height);
    }

    @Override
    public void tick() {

    }
}
