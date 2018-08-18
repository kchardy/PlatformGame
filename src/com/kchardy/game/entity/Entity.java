package com.kchardy.game.entity;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.states.BossStade;
import com.kchardy.game.states.LizardState;
import com.kchardy.game.states.PlayerState;

import java.awt.*;

import static com.kchardy.game.Game.deathScreen;
import static com.kchardy.game.Game.gameOver;
import static com.kchardy.game.Game.lives;

public abstract class Entity {

    public int x, y;
    public int velX, velY;
    public int width, height;
    public int facing = 0;  //0 - left, 1 - right
    public int hp;
    public int phaseTime;
    public int type;

    public static PlayerState state;


    public boolean jumping = false;
    public boolean falling = true;
    public boolean goingDown = false;
    public boolean attackable = false;


    public Id id;
    public BossStade bossStade;
    public LizardState lizardState;

    public double gravity = 0.0;

    public Handler handler;

    public Entity(int x, int y, int width, int height, Id id, Handler handler)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;
        this.handler = handler;
    }

    public abstract void render(Graphics g);

    public abstract void tick();

    public void die()
    {
        handler.removeEntity(this);

        if(getId()==Id.player)
        {
            lives --;
            deathScreen = true;

            if(lives <= 0)
            gameOver = true;
//            Game.losealife.play();
        }
//        handler.removeEntity(this);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Id getId()
    {
        return id;
    }

    public int getType() { return type; }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public void setType(int type) {
        this.type = type;
    }


    public Rectangle getBounds()
    {
        return new Rectangle(getX(), getY(), width, height);
    }

    public Rectangle getBoundsTop()
    {
        return new Rectangle(getX() + 10, getY(), width - 20, 5);
    }

    public Rectangle getBoundsBottom()
    {
        return new Rectangle(getX() + 10, getY() + height - 5, width - 20, 5);
    }

    public Rectangle getBoundsLeft()
    {
        return new Rectangle(getX(), getY() + 10, 5, height - 20);
    }
    public Rectangle getBoundsRight()
    {
        return new Rectangle(getX() + width -5, getY() + 10, 5, height - 20);
    }
}
