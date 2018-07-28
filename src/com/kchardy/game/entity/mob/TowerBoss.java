package com.kchardy.game.entity.mob;

import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.states.BossStade;
import com.kchardy.game.tile.Tile;

import java.awt.*;
import java.util.Random;

public class TowerBoss extends Entity {

    public int jumpTime = 0;
    public boolean addJumpTime = false;

    private Random random;

    public TowerBoss(int x, int y, int width, int height, Id id, Handler handler, int hp) {
        super(x, y, width, height, id, handler);
        this.hp = hp;

        bossStade = BossStade.IDLE;

        random = new Random();
    }

    @Override
    public void render(Graphics g) {
        if(bossStade == BossStade.IDLE || bossStade == BossStade.SPINNING)
            g.setColor(Color.MAGENTA);
        else if(bossStade == BossStade.RECOVERING)
            g.setColor(Color.RED);
        else if(bossStade == BossStade.RUNNING || bossStade == BossStade.JUMPING)
            g.setColor(Color.YELLOW);

        g.fillRect(x, y, width, height);
    }

    @Override
    public void tick() {
        x+=velX;
        y+=velY;

        if(hp <= 0)
        {
            die();
        }

        phaseTime++;

        if((phaseTime>=180 && bossStade == BossStade.IDLE) || phaseTime>=600 && bossStade != BossStade.SPINNING)
            chooseState();

        if(bossStade == BossStade.RECOVERING && phaseTime >= 180)
        {
            bossStade = BossStade.SPINNING;
            phaseTime = 0;
        }

        if(phaseTime>=360 && bossStade == BossStade.SPINNING)
        {
            phaseTime = 0;
            bossStade = BossStade.IDLE;
        }
        if(bossStade == BossStade.IDLE || bossStade == BossStade.RECOVERING)
        {
            setVelX(0);
            setVelY(0);
        }
        if(bossStade == BossStade.RUNNING || bossStade == BossStade.JUMPING)
            attackable = true;
        else
            attackable = false;

        if(bossStade != BossStade.JUMPING)
        {
            addJumpTime = false;
            jumpTime = 0;
        }

        if(addJumpTime)
        {
            jumpTime++;
            if(jumpTime >= 30)
            {
                addJumpTime = false;
                jumpTime = 0;
            }
            if(!jumping && !falling)
            {
                jumping = true;
                gravity = 8.0;
            }
        }

        for(int i = 0; i<handler.tile.size();i++) {
            Tile t = handler.tile.get(i);
            if (t.isSolid())
                if (getBoundsTop().intersects(t.getBounds()))
                {
                    setVelY(0);
                    if (jumping) {
                        jumping = false;
                        gravity = 0.8; //-0,8
                        falling = true;
                    }
                }
                if (getBoundsBottom().intersects(t.getBounds()))
                {
                    setVelY(0);
                    if (falling)
                    {
                        falling = false;
                        addJumpTime = true;
                    }
                }
                if (getBoundsLeft().intersects(t.getBounds()))
                {
                    setVelX(0);
                    if(bossStade == BossStade.RUNNING)
                    {
                        setVelX(4);
                    }
                    x = t.getX() + t.width;
                }
                if (getBoundsRight().intersects(t.getBounds()))
                {
                    setVelX(0);
                    if(bossStade == BossStade.RUNNING)
                    {
                        setVelX(-4);
                    }
                    x = t.getX() - t.width;
                }
            }

            for (int i = 0;i<handler.entity.size();i++)
            {
                Entity e = handler.entity.get(i);
                if(e.getId() == Id.player)
                {
                    if(bossStade == BossStade.JUMPING)
                    {
                        if(jumping || falling)
                        {
                            if(getX()>=e.getX()-4 && getX()<=e.getX()+4)
                                setVelX(0);
                            else if(e.getX() < getX())
                                setVelX(-3);
                            else if (e.getX() > getX())
                                setVelX(3);
                        }
                        else
                        {
                            setVelX(0);
                        }
                    }
                    else if(bossStade == BossStade.SPINNING)
                    {
                        if(e.getX() < getX())
                             setVelX(-3);
                         else if (e.getX() > getX())
                             setVelX(3);
                    }

                }
            }
        if(jumping)
        {
            gravity -= 0.1;//0,15
            setVelY((int) -gravity);
            if(gravity <= 0.0)//0,6
            {
                jumping = false;
                falling = true;
            }

        }
        if(falling)
        {
            gravity += 0.1;//0,15
            setVelY((int) gravity);
        }

    }
    public void chooseState()
    {
        int nextPhase = random.nextInt(2);
        if(nextPhase == 0)
        {
            bossStade = BossStade.RUNNING;
            int direction = random.nextInt(2);
            if(direction == 0)
                setVelX(-4);
            else
                setVelX(4);
        }
        else if(nextPhase == 1)
        {
            bossStade = BossStade.JUMPING;
            jumping = true;
            gravity = 8.0;
        }

        phaseTime = 0;
    }

}

