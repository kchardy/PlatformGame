package com.kchardy.game.entity.mob;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.entity.Particle;
import com.kchardy.game.states.BossStade;
import com.kchardy.game.states.LizardState;
import com.kchardy.game.states.PlayerState;
import com.kchardy.game.tile.Tile;
import com.kchardy.game.tile.Trail;

import java.awt.*;
import java.util.Random;

public class Player extends Entity {


    private int frame = 0;
    private int frameDelay = 0;
    private int pixelsTravelled = 0;
    private int invincibilityTime = 0;
    private int particleDelay = 0;

    private Random random;
    private boolean invincible = false;

    public Player(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);

        state = PlayerState.SMALL;
        random = new Random();
    }

    @Override
    public void render(Graphics g) {
        if(state == PlayerState.FIRE)
        {
            if (facing == 0) {
                g.drawImage(Game.playerStaff[frame + 5].getBufferedImage(), x, y, width, height, null);
            } else if (facing == 1) {
                g.drawImage(Game.playerStaff[frame].getBufferedImage(), x, y, width, height, null);
            }
        }
        else
        {
            if (facing == 0) {
                g.drawImage(Game.player[frame + 5].getBufferedImage(), x, y, width, height, null);
            } else if (facing == 1) {
                g.drawImage(Game.player[frame].getBufferedImage(), x, y, width, height, null);
            }
        }
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        if(invincible)
        {
            if(facing == 0)
                handler.addTile(new Trail(getX(), getY(), width, height, false, Id.trail, handler, Game.player[4+frame].getBufferedImage() ));
            else if(facing ==1)
                handler.addTile(new Trail(getX(), getY(), width, height, false, Id.trail, handler, Game.player[frame].getBufferedImage() ));

            particleDelay++;
            if(particleDelay>=3)
            {
                handler.addEntity(new Particle(getX() + random.nextInt(width), getY() + random.nextInt(height), 10, 10, Id.particle, handler));
                particleDelay = 0;
            }

            invincibilityTime++;
            if(invincibilityTime>=600)//~10 sec
            {
                invincible=false;
                invincibilityTime=0;
            }

            //speed incresing
            if(velX == 1) //1 normal walking for a player
                setVelX(5);
            else if(velX == -1)
                setVelX(-5);
        }
        else
        {
            if(velX == 5) //1 normal walking for a player
                setVelX(1);
            else if(velX == -5)
                setVelX(-1);
        }
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
        for (int i = 0; i < handler.tile.size(); i++) {
            Tile ti = handler.tile.get(i);
            if (ti.isSolid() && !goingDown) { ///dodane
                //  if (ti.getId() == Id.wall) //nie bylo
                // {
                if (getBoundsTop().intersects(ti.getBounds()) && ti.getId() != Id.coin) {
                    setVelY(0);
                    if (jumping && !goingDown) {
                        jumping = false;
                        gravity = 0.8;
                        falling = true;
                    }
                    if (ti.getId() == Id.chest) {
                        if (getBounds().intersects(ti.getBounds()))
                            ti.activated = true;
                    }
                }
                if (getBounds().intersects(ti.getBounds())) {
                    if (ti.getId() == Id.gate) {
                        Game.switchLevel();
                    }
                }
                if (getBoundsBottom().intersects(ti.getBounds()) && ti.getId() != Id.coin) {
                    setVelY(0);
                    if (falling) falling = false;
                } else if (!falling && !jumping) {
                    falling = true;
                    gravity = 0.8; // -0,8
                }
                if (getBoundsLeft().intersects(ti.getBounds()) && ti.getId() != Id.coin) {
                    setVelX(0);
                    x = ti.getX() + ti.width;
                }
                if (getBoundsRight().intersects(ti.getBounds()) && ti.getId() != Id.coin) {
                    setVelX(0);
                    x = ti.getX() - ti.width;
                }
                //dodane
                if (getBounds().intersects(ti.getBounds()) && ti.getId() == Id.coin) {
                    Game.coins++;
                    ti.die();
                }
            }
        }

        for (int i = 0; i < handler.entity.size(); i++) {
            Entity e = handler.entity.get(i);
            if (e.getId() == Id.potion) {
                switch (e.getType()) {
                    case 0:
                        if (getBounds().intersects(e.getBounds())) {
                            int tpX = getX();
                            int tpY = getY();
                            width += (width / 3);
                            height += (height / 3);
                            setX(tpX - width);
                            setY(tpY - height);
                            if (state == PlayerState.SMALL)
                                state = PlayerState.BIG;
                            e.die();
                        }
                        break;
                    case 1:
                        if (getBounds().intersects(e.getBounds())) {
                            Game.lives++;
                            e.die();
                        }
                        break;

                }

            } else if (e.getId() == Id.goblin || e.getId() == Id.towerBoss || e.getId() == Id.trap) {
                if (invincible) {
                    e.die();
                } else {
                    if (getBoundsBottom().intersects(e.getBoundsTop())) {
                        if (e.getId() != Id.towerBoss)
                        {
                            e.die();
//                            Game.goombastomp.play();
                        }
                        else if (e.attackable) {
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
                    } else if (getBounds().intersects(e.getBounds())) {
                        if (state == PlayerState.BIG) {
                            state = PlayerState.SMALL;
                            width /= 3;
                            height /= 3;
                            x += width;
                            y += height;
                        } else if (state == PlayerState.SMALL) {
                            die();
                        }
                    }

                    else if (e.getId() == Id.lizard) {
                        if(invincible) e.die();
                        else
                        {
                            if (e.lizardState == LizardState.WALKING) {
                                if (getBoundsBottom().intersects(e.getBoundsTop())) {
                                    e.lizardState = LizardState.ROLLED;

                                    jumping = true;
                                    falling = false;
                                    gravity = 3.5;
                                } else if (getBounds().intersects(e.getBounds())) {
                                    die();
                                }
                            } else if (e.lizardState == LizardState.ROLLED) {
                                if (getBoundsBottom().intersects(e.getBoundsTop())) {
                                    e.lizardState = LizardState.SPINNING;

                                    int direction = random.nextInt(2);

                                    switch (direction) {
                                        case 0:
                                            e.setVelX(-5);
                                            break;
                                        case 1:
                                            e.setVelX(5);
                                            break;
                                    }

                                    jumping = true;
                                    falling = false;
                                    gravity = 3.5;
                                }

                                if (getBoundsLeft().intersects(e.getBoundsRight())) {
                                    e.setVelX(-5);
                                    e.lizardState = LizardState.SPINNING;
                                }
                                if (getBoundsRight().intersects(e.getBoundsLeft())) {
                                    e.setVelX(5);
                                    e.lizardState = LizardState.SPINNING;
                                }

                            } else if (e.lizardState == LizardState.SPINNING) {
                                if (getBoundsBottom().intersects(e.getBoundsTop())) {
                                    e.lizardState = LizardState.ROLLED;

                                    jumping = true;
                                    falling = false;
                                    gravity = 3.5;
                                } else if (getBounds().intersects(e.getBounds())) {
                                    die();
                                }
                            }
                        }

                    } else if (e.getId() == Id.bean) {
                        if (getBounds().intersects(e.getBounds())) {
                            invincible = true;
                            e.die();
                        }
                    }
                    else if (e.getId() == Id.staff)
                    {
                        if(getBounds().intersects(e.getBounds()))
                        {
                            if(state == PlayerState.SMALL)
                            {
                                int tpX = getX();
                                int tpY = getY();
                                width+=(width/3);
                                height+= (height/3);
                                setX(tpX - width);
                                setY(tpY - height);
                            }
                            state = PlayerState.FIRE;
                            e.die();
                        }
                    }

                }
            }
        }
            if (jumping && !goingDown) {
                gravity -= 0.1;//0,15
                setVelY((int) -gravity);
                if (gravity <= 0.0)//0,6
                {
                    jumping = false;
                    falling = true;
                }

            }
            if (falling && !goingDown) {
                gravity += 0.1;//0,15
                setVelY((int) gravity);
            }
            if (velX != 0) {
                frameDelay++;
                if (frameDelay >= 20)//3 // 10
                {
                    frame++;
                    if (frame >= 6)//5 dlugosc obrazkow w jedna strone
                        frame = 0;
                    frameDelay = 0;
                }
            }
            if (goingDown) {
                for (int i = 0; i < Game.handler.tile.size(); i++) {
                    Tile t = Game.handler.tile.get(i);
                    if (t.getId() == Id.ladder) {
                        if (getBounds().intersects(t.getBounds())) {
                            switch (facing) {
                                case 0:
                                    setVelY(-5); // lub -1
                                    setVelY(0);
                                    pixelsTravelled += -velY;
                                    break;
                                case 2:
                                    setVelY(5);
                                    setVelX(0);
                                    pixelsTravelled += velY;
                                    break;
                            }
                            if (pixelsTravelled > t.height * 2 + height)//dodane * 2 + height
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


