package com.kchardy.game.tile;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;

import java.awt.*;

public class Gate extends Tile {
    public Gate(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Game.gate.getBufferedImage(), getX(), getY(), 128, 128, null);
    }

    @Override
    public void tick() {

    }
}
