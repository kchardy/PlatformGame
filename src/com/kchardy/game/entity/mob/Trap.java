package com.kchardy.game.entity.mob;

import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;

import java.awt.*;

public class Trap extends Entity {

    private int wait;
    private int pixelsTravelled = 0;

    private boolean moving;
    private boolean hide;

    public Trap(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);

        moving = false;
        hide = true;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(getX(),getY(), width, height);
    }

    @Override
    public void tick() {
        y+=velY;

        if(!moving)
            wait++;

        if(wait>=180)
        {
            if(hide)
                hide = false;
            else
                hide = true;

            moving = true;
            wait = 0;
        }

        if(moving)
        {
            if(hide)
                setVelY(-1);//-3
            else
                setVelY(1);//3;

            pixelsTravelled +=velY;

            if(pixelsTravelled>=height || pixelsTravelled<=-height)
            {
                pixelsTravelled = 0;
                moving = false;

                setVelY(0);
            }
        }
    }
}
