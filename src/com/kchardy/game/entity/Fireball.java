package com.kchardy.game.entity;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.tile.Tile;

import java.awt.*;

public class Fireball extends Entity {
    public Fireball(int x, int y, int width, int height, Id id, Handler handler, int facing) {
        super(x, y, width, height, id, handler);

        switch (facing)
        {
            case  0:
                setVelX(-5);
                break;
            case 1:
                setVelX(5);
                break;
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Game.fireball.getBufferedImage(), getX(), getY(), width, height, null);
    }

    @Override
    public void tick() {

        x+=velX;
        y+=velY;

        for(int i =0;i<handler.tile.size();i++)
        {
            Tile t = handler.tile.get(i);
            if(t.isSolid())
            {
                if(getBoundsLeft().intersects(t.getBounds()) || getBoundsRight().intersects(t.getBounds()))
                    die();

            }
            if(getBoundsBottom().intersects(t.getBounds()))
            {
                jumping = true;
                falling = false;
                gravity = 4.0;
            }
            else if(!jumping && !falling)
            {
                falling = true;
                gravity = 1.0;
            }
        }

        for(int i =0;i<handler.entity.size();i++)
        {
            Entity e = handler.entity.get(i);

            if(e.getId() == Id.goblin ||e.getId() == Id.lizard)
            {
                if(getBounds().intersects(e.getBounds()))
                {
                    e.die();
                    die();
                }
            }
        }

        if (jumping)
        {
            gravity -= 0.15;//0,17
            setVelY((int) -gravity);
            if (gravity <= 0.5)//0,6
            {
                jumping = false;
                falling = true;
            }

        }
        if (falling) {
            gravity += 0.15;//0,17//0,4 faster
            setVelY((int) gravity);
        }
    }
}
