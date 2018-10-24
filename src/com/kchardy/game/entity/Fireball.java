package com.kchardy.game.entity;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.mob.Goblin;
import com.kchardy.game.states.GoblinState;
import com.kchardy.game.tile.Tile;

import java.awt.*;


public class Fireball extends Entity {

    private int frame = 0;
    private int frameDelay = 0;

    public int fireTime = 0;
    public static boolean fire;



    public Fireball(int x, int y, int width, int height, Id id, Handler handler, int facing) {
        super(x, y, width, height, id, handler);

        switch (facing)
        {
            case  0:
                setVelX(5);
                break;
            case 1:
                setVelX(-5);
                break;
        }
    }

    @Override
    public void render(Graphics g) {
        if(facing == 1)
        {
            g.drawImage(Game.fireball2.getBufferedImage(),x, y, width, height, null);
        }
        else if(facing == 0)
        {
            g.drawImage(Game.fireball1.getBufferedImage(),x, y, width, height, null);
        }
    }

    @Override
    public void tick() {

        x+=velX;

        if(fire)
        {
            fireTime++;
            if(fireTime>=360)
            {
                fire = false;
                fireTime = 0;
            }
            else
                fire = true;
        }

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
                    if (e.getId() == Id.goblin)
                    {
                        Goblin.stade = GoblinState.DEATH;
                        Game.goblins ++;
                        handler.goblinCreated= false;
                    }
                }
            }
        }

        if (jumping)
        {
            gravity -= 0.15;
            setVelY((int) - gravity);
            if (gravity <= 0.5)
            {
                jumping = false;
                falling = true;
            }

        }
        if (falling) {
            gravity += 0.2;
            setVelY((int) gravity);
        }

        if(velX!=0)
        {
            frameDelay++;
            if(frameDelay >= 5)
            {
                frame++;
                if(frame >= 4)
                    frame = 0;
                frameDelay = 0;
            }
        }
    }
}
