package com.kchardy.game.entity.mob;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.tile.Tile;

import java.awt.*;
import java.util.Random;

public class Goblin extends Entity {

    private Random random = new Random();


    private int frame = 0;
    private int frameDelay = 0;

    public Goblin(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);

        int direction = random.nextInt(2);

        switch (direction)
        {
            case 0:
                setVelX(-1);
                facing = 0;
                break;
            case 1:
                setVelX(1);
                facing = 1;
                break;
        }
    }


    @Override
    public void render(Graphics g) {
        if(facing == 0)
        {
            g.drawImage(Game.goblin[frame+5].getBufferedImage(),x, y, width, height, null);//+5
        }
        else if(facing == 1)
        {
            g.drawImage(Game.goblin[frame].getBufferedImage(),x, y, width, height, null);
        }
    }

    @Override
    public void tick() {
        x+=velX;
        y+=velY;

        for(Tile ti : handler.tile)
        {
            if(!ti.solid) break;
            if(getBoundsBottom().intersects(ti.getBounds()))
            {
                setVelY(0);
                if(falling) falling = false;
            }
            else
            {
                if(!falling)
                {
                    gravity = -0.8;
                    falling = true;
                }
            }
            if(getBoundsLeft().intersects(ti.getBounds()))
            {
                setVelX(1);
                facing = 1;
            }
            if(getBoundsRight().intersects(ti.getBounds()))
            {
                setVelX(-1);
                facing = 0;
            }
        }
        if(falling)
        {
            gravity += 0.1;
            setVelY((int) gravity);
        }
        if(velX!=0)
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

