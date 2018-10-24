package com.kchardy.game.entity.mob;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.states.LizardState;
import com.kchardy.game.tile.Tile;

import java.awt.*;
import java.util.Random;

public class Lizard extends Entity {

    private Random random = new Random();

    private int rollCounter;

    private int frame = 0;
    private int frameDelay = 0;

    public Lizard(int x, int y, int width, int height, Id id, Handler handler) {
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

        lizardState = LizardState.WALKING;
    }

    @Override
    public void render(Graphics g) {
        if(facing == 0 && lizardState == LizardState.WALKING || lizardState == LizardState.SPINNING)
        {
            g.drawImage(Game.lizard[frame].getBufferedImage(),x, y, width, height, null);//+5
        }
        else if(facing == 1 && lizardState == LizardState.WALKING || lizardState == LizardState.SPINNING)
        {
            g.drawImage(Game.lizard[frame+3].getBufferedImage(),x, y, width, height, null);
        }
        else if(facing == 0 && lizardState == LizardState.ROLLED)
        {
            g.drawImage(Game.lizardRolling[frame].getBufferedImage(),x, y, width, height, null);//+5
        }
        else if(facing == 1 && lizardState == LizardState.ROLLED)
        {
            g.drawImage(Game.lizardRolling[frame+3].getBufferedImage(),x, y, width, height, null);
        }


    }

    @Override
    public void tick() {
        x+=velX;
        y+=velY;

        if(lizardState == LizardState.ROLLED){
            setVelX(0);
            rollCounter++;
            if(rollCounter>=300)
            {
                rollCounter = 0;
                lizardState = LizardState.WALKING;
            }

            if(lizardState == LizardState.WALKING || lizardState == LizardState.SPINNING)
            {
                rollCounter = 0;

                if(velX == 0){
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
            }
        }

        for(Tile ti : handler.tile) {
            if (ti.isSolid()) {
                if (getBoundsBottom().intersects(ti.getBounds())) {
                    setVelY(0);
                    if (falling) falling = false;
                } else {
                    if (!falling) {
                        gravity = 0.8;
                        falling = true;
                    }
                }
                if (getBoundsLeft().intersects(ti.getBounds())) {
                    if(lizardState == LizardState.SPINNING)
                        setVelX(5);
                    else
                        setVelX(2);
                }
                if (getBoundsRight().intersects(ti.getBounds())) {
                    if(lizardState == LizardState.SPINNING)
                        setVelX(-5);
                    else
                        setVelX(-2);
                }
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
                if(frame >= 4)
                    frame = 0;//0
                frameDelay = 0;
            }
        }

    }
}
