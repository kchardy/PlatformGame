package com.kchardy.game.entity.mob;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.tile.Tile;

import java.awt.*;

public class Player extends Entity {

    private int frame = 0;
    private int frameDelay = 0;

    private boolean animate = false;

    public Player(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);
    }

    @Override
    public void render(Graphics g) {
        if(facing == 0)
        {
            g.drawImage(Game.player[frame+5].getBufferedImage(),x, y, width, height, null);//+5
        }
        else if(facing == 1)
        {
            g.drawImage(Game.player[frame].getBufferedImage(),x, y, width, height, null);
        }
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        if(x <= 0)
            x = 0;
//        if(y <= 0)
//            y = 0;
//        if(x+width >= 810)//(width/14*10*3)) //WIDTH/14*10*SCALE        // prze SCALE = 4 ->1080
//            x = 810 - width;
//        if(y+height >= 578)//(width/14*10*3)) //WIDTH/14*10*SCALE        // prze SCALE = 4 ->771
//            y = 578 - height;
        if(velX!=0)
            animate = true;
        else animate = false;

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
                        gravity = -0.8;
                        falling = true;
                    }
                }
                if(getBoundsBottom().intersects(ti.getBounds()))
                {
                    setVelY(0);
                    if(falling) falling = false;
                }
                else
                {
                    if(!falling && !jumping)
                    {
                        gravity = -0.8;
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
            if(ti.getId() == Id.chest)
            {
                if(getBoundsTop().intersects(ti.getBounds()))
                    ti.activated = true;
            }
        }


        for(int i = 0; i < handler.entity.size(); i++)
        {
            Entity e = handler.entity.get(i);   //mushroom episode

            if(e.getId() == Id.potion)
            {
                if(getBounds().intersects(e.getBounds())) // collading with the mushroom
                {
                    int tpX = getX();
                    int tpY = getY();
                    width *= 1.4;//2
                    height *= 1.4;//2
                    e.setVelX(tpX - width);// setVelX(tpX - width);// - width);
                    e.setVelY(tpY - height);// - height);
                    e.die();
                }
            }
            else if(e.getId() == Id.goblin)
            {
                if(getBoundsBottom().intersects(e.getBoundsTop()))
                {
                    e.die();
                }
                else if(getBounds().intersects(e.getBounds()))
                {
                    die();
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
        if(animate)
        {
            frameDelay++;
            if(frameDelay >= 3)
            {
                frame++;
                if(frame >= 6)//5
                    frame = 0;//0
                frameDelay = 0;
            }
        }
    }
}
