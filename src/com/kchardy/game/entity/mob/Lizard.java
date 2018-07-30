package com.kchardy.game.entity.mob;

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
        g.setColor(Color.RED);
        g.fillRect(getX(), getY(), width, height);
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
                          //  facing = 0;
                            break;
                        case 1:
                            setVelX(1);
                         //   facing = 1;
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
                   // facing = 1;
                }
                if (getBoundsRight().intersects(ti.getBounds())) {
                    if(lizardState == LizardState.SPINNING)
                        setVelX(-5);
                    else
                        setVelX(-2);

                    // facing = 0;
                }
            }
        }
        if(falling)
        {
            gravity += 0.1;
            setVelY((int) gravity);
        }

        //animacja
//        if(velX!=0)
//        {
//            frameDelay++;
//            if(frameDelay >= 3)
//            {
//                frame++;
//                if(frame >= 6)//5
//                    frame = 0;//0
//                frameDelay = 0;
//            }
//        }

    }
}
