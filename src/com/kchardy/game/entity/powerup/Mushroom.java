package com.kchardy.game.entity.powerup;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.tile.Tile;

import java.awt.*;
import java.util.Random;

public class Mushroom extends Entity {

    private Random random = new Random();
    public Mushroom(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);

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
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Game.mushroom.getBufferedImage(), x, y, width, height, null);
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
                }
                if(getBoundsRight().intersects(ti.getBounds()))
                {
                    setVelX(-1);
                }
            }
            if(falling)
            {
              gravity += 0.1;
              setVelY((int) gravity);
             }
        }

    }

