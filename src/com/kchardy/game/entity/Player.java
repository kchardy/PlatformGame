package com.kchardy.game.entity;

import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.tile.Tile;

import java.awt.*;

public class Player extends Entity{

    public Player(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        if(x <= 0)
            x = 0;
        if(y <= 0)
            y = 0;
        if(x+width >= 810)//(width/14*10*3)) //WIDTH/14*10*SCALE        // prze SCALE = 4 ->1080
            x = 810 - width;
        if(y+height >= 578)//(width/14*10*3)) //WIDTH/14*10*SCALE        // prze SCALE = 4 ->771
            y = 578 - height;

        for(Tile ti : handler.tile)
        {
            if(!ti.solid) break;
            if(ti.getId()== Id.wall)
            {
                if(getBoundsTop().intersects(ti.getBounds()))
                {
                    setVelY(0);
                    if(jumping)
                    {
                        jumping = false;
                        gravity = 0.0;
                        falling = true;
                    }
//                    y = ti.getY() + ti.height;
                }
                if(getBoundsBottom().intersects(ti.getBounds()))
                {
                    setVelY(0);
//                    y = ti.getY() - ti.height;
                    if(falling) falling = false;
                }
                else
                {
                    if(!falling && !jumping)
                    {
                        gravity = 0.0;
                        falling = true;
                    }
                }
                if(getBoundsLeft().intersects(ti.getBounds()))
                {
                    setVelX(0);
                    x = ti.getX() + ti.width;
                }
                if(getBoundsRight().intersects(ti.getBounds()))
                {
                    setVelX(0);
                    x = ti.getX() - ti.width;
                }
            }
        }
        if(jumping)
        {
            gravity -= 0.1;
            setVelY((int) -gravity);
            if(gravity <= 0.0)
            {
                jumping = false;
                falling = true;
            }
        }
        if(falling)
        {
            gravity += 0.1;
            setVelY((int) gravity);
        }
    }
}
