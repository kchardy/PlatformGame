package com.kchardy.game.entity.mob;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.states.BossStade;
import com.kchardy.game.states.LizardState;
import com.kchardy.game.states.PlayerState;
import com.kchardy.game.tile.Tile;

import java.awt.*;
import java.util.Random;

public class Player extends Entity {

    private PlayerState state;

    private int frame = 0;
    private int frameDelay = 0;
    private int pixelsTravelled = 0;
    private Random random;

    public Player(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);

        state = PlayerState.SMALL;
        random = new Random();
    }

    @Override
    public void render(Graphics g) {
        if(facing == 0)
        {
            g.drawImage(Game.player[frame+5].getBufferedImage(),x, y, width, height, null);
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

//        if(x <= 0)
//            x = 0;
//        if(y <= 0)
//            y = 0;
//        if(x+width >= 810)//(width/14*10*3)) //WIDTH/14*10*SCALE        // prze SCALE = 4 ->1080
//            x = 810 - width;
//        if(y+height >= 578)//(width/14*10*3)) //WIDTH/14*10*SCALE        // prze SCALE = 4 ->771
//            y = 578 - height;

        //----------------
//        if(velX!=0)
//            animate = true;
//        else animate = false;

//        if(goingDown)
//        {
//            pixelsTravelled += velY;
//        }

       // for(Tile ti : handler.tile)
        for(int i = 0; i<handler.tile.size();i++) {
            Tile ti = handler.tile.get(i);
            if (ti.isSolid() && !goingDown)
            { ///dodane
              //  if (ti.getId() == Id.wall) //nie bylo
               // {
                    if (getBoundsTop().intersects(ti.getBounds()) && ti.getId() != Id.coin )
                    {
                        setVelY(0);
                        if (jumping && !goingDown) {
                            jumping = false;
                            gravity = 0.8;
                            falling = true;
                        }
                        if (ti.getId() == Id.chest)
                        {
                            if (getBounds().intersects(ti.getBounds()))
                                ti.activated = true;
                        }
                    }
                    if (getBoundsBottom().intersects(ti.getBounds()) && ti.getId() != Id.coin)
                    {
                        setVelY(0);
                        if (falling) falling = false;
                    }
                    else if (!falling && !jumping)
                    {
                        falling = true;
                        gravity = 0.8; // -0,8
                    }
                    if (getBoundsLeft().intersects(ti.getBounds()) && ti.getId() != Id.coin)
                    {
                        setVelX(0);
                        x = ti.getX() + ti.width;
                    }
                    if (getBoundsRight().intersects(ti.getBounds())&& ti.getId() != Id.coin)
                    {
                        setVelX(0);
                        x = ti.getX() - ti.width;
                    }
                    //dodane
                    if(getBounds().intersects(ti.getBounds()) && ti.getId() == Id.coin)
                    {
                        Game.coins++;
                        ti.die();
                    }
            }
        }

        for(int i = 0; i < handler.entity.size(); i++)
        {
            Entity e = handler.entity.get(i);
            if(e.getId() == Id.potion)
            {
                switch (e.getType())
                {
                    case 0:
                        if(getBounds().intersects(e.getBounds()))
                        {
                            int tpX = getX();
                            int tpY = getY();
                            width+=(width/3);
                            height+=(height/3);
                            setX(tpX - width);
                            setY(tpY - height);
                            if(state == PlayerState.SMALL)
                                state = PlayerState.BIG;
                            e.die();
                        }
                        break;
                    case 1:
                        if(getBounds().intersects(e.getBounds()))
                        {
                            Game.lives++;
                            e.die();
                        }
                        break;

                }

            }
            else if(e.getId() == Id.goblin || e.getId() == Id.towerBoss)
            {
                if(getBoundsBottom().intersects(e.getBoundsTop()))
                {
                    if(e.getId() != Id.towerBoss)
                            e.die();
                    else if(e.attackable)
                    {
                        e.hp--;
                        e.falling = true;
                        e.gravity = 3.0;
                        e.bossStade = BossStade.RECOVERING;
                        e.attackable = false;
                        e.phaseTime = 0;

                        jumping = true;
                        falling = false;
                        gravity = 3.5;
                    }
                }
                else if(getBounds().intersects(e.getBoundsRight()) || getBounds().intersects(e.getBoundsLeft()) ||
                        getBounds().intersects(e.getBoundsBottom()))
                {
                    if(state == PlayerState.BIG)
                    {
                        state = PlayerState.SMALL;
                        width/=3;
                        height/=3;
                        x+=width;
                        y+=height;
                    }
                    else if(state == PlayerState.SMALL)
                    {
                        die();
                    }
                }
                else if(e.getId() == Id.lizard)
                {
                    if(e.lizardState == LizardState.WALKING)
                    {
                        if(getBoundsBottom().intersects(e.getBoundsTop()))
                        {
                            e.lizardState = LizardState.ROLLED;

                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }
                        else if(getBounds().intersects(e.getBounds()))
                        {
                            die();
                        }
                    }
                    else if(e.lizardState == LizardState.ROLLED)
                    {
                        if(getBoundsBottom().intersects(e.getBoundsTop()))
                        {
                            e.lizardState = LizardState.SPINNING;


                            int direction = random.nextInt(2);

                            switch (direction)
                            {
                                case 0:
                                    e.setVelX(-5);
                                    //facing = 0;
                                    break;
                                case 1:
                                    e.setVelX(5);
                                    //facing = 1;
                                    break;
                            }

                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }

                        if(getBoundsLeft().intersects(e.getBoundsRight()))
                        {
                            e.setVelX(-5);
                            e.lizardState = LizardState.SPINNING;
                        }
                        if(getBoundsRight().intersects(e.getBoundsLeft()))
                        {
                            e.setVelX(5);
                            e.lizardState = LizardState.SPINNING;
                        }

                    }
                    else if(e.lizardState == LizardState.SPINNING)
                    {
                        if(getBoundsBottom().intersects(e.getBoundsTop()))
                        {
                            e.lizardState = LizardState.ROLLED;

                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }
                        else if(getBounds().intersects(e.getBounds()))
                        {
                            die();
                        }
                    }
                }

            }
        }
        if(jumping && !goingDown)
        {
            gravity -= 0.1;//0,15
            setVelY((int) -gravity);
            if(gravity <= 0.0)//0,6
            {
                jumping = false;
                falling = true;
            }

        }
        if(falling && !goingDown)
        {
            gravity += 0.1;//0,15
            setVelY((int) gravity);
        }
        if(velX!=0)
        {
            frameDelay++;
            if(frameDelay >= 20)//3 // 10
            {
                frame++;
                if(frame >= 6)//5 dlugosc obrazkow w jedna strone
                    frame = 0;
                frameDelay = 0;
            }
        }
        if(goingDown)
        {
            for(int i = 0; i<Game.handler.tile.size();i++)
            {
                Tile t = Game.handler.tile.get(i);
                if(t.getId()==Id.ladder)
                {
                    if(getBounds().intersects(t.getBounds()))
                    {
                        switch (facing)
                        {
                            case 0:
                                setVelY(-5); // lub -1
                                setVelY(0);
                                pixelsTravelled+=-velY;
                                break;
                            case 2:
                                setVelY(5);
                                setVelX(0);
                                pixelsTravelled+=velY;
                                break;
                        }
                        if(pixelsTravelled > t.height * 2 + height)//dodane * 2 + height
                        {
                             goingDown = false;
                             pixelsTravelled = 0;
                        }
                    }
                }
            }
        }
    }
}
