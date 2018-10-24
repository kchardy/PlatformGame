package com.kchardy.game.entity.powerup;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.tile.Tile;

import java.awt.*;
import java.util.Random;

public class Bean extends Entity {

    private Random random;

    public Bean(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);
        random = new Random();

        int direction = random.nextInt(2);

        switch (direction)
        {
            case 0:
                setVelX(-1);
                break;
            case 1:
                setVelX(1);
                break;
        }

        falling = true;
        gravity = 0.17;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Game.magicBean.getBufferedImage(),getX(), getY(), width, height, null);
    }

    @Override
    public void tick() {

        x+=velX;
        y+=velY;

        for(int i = 0; i<handler.tile.size();i++)
        {
            Tile t = handler.tile.get(i);

            if(t.isSolid())
            {
                if(getBoundsBottom().intersects(t.getBounds()))
                {
                    jumping = false;
                    gravity = 8.0;
                }
                if(getBoundsLeft().intersects(t.getBounds()))
                {
                    setVelX(1);
                }
                if(getBoundsRight().intersects(t.getBounds()))
                    setVelX(-1);
            }
        }
        if(jumping)
        {
            gravity -= 0.17;
            setVelY((int) -gravity);
            if(gravity <= 0.0)
            {
                jumping = false;
                falling = true;
            }
        }
        if(falling)
        {
            gravity += 0.17;
            setVelY((int) gravity);
        }
    }
}
