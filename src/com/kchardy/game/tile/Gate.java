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
        g.drawImage(Game.gate[0].getBufferedImage(), getX(), getY()+height-64, width, 64, null);
        g.drawImage(Game.gate[1].getBufferedImage(), getX()+64, getY()+64, width, 64, null);
        g.drawImage(Game.gate[2].getBufferedImage(), getX(), getY(), width, 64, null);
        g.drawImage(Game.gate[3].getBufferedImage(), getX()+64, getY()+ 192, width, 64, null);
    }

    @Override
    public void tick() {

    }
}
