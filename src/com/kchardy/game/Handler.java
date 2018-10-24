package com.kchardy.game;

import com.kchardy.game.entity.mob.Lizard;
import com.kchardy.game.entity.powerup.Bean;
import com.kchardy.game.states.GoblinState;
import com.kchardy.game.tile.Coin;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.entity.mob.Goblin;
import com.kchardy.game.entity.mob.Player;
import com.kchardy.game.entity.mob.TowerBoss;
import com.kchardy.game.tile.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

import static com.kchardy.game.Game.*;

public class Handler {

    public LinkedList <Entity> entity = new LinkedList<Entity>();
    public LinkedList <Tile> tile = new LinkedList<Tile>();
    public boolean created = false;
    public boolean goblinCreated = false;

    public Random random;

    public Handler()
    {

    }

    public void render(Graphics g)
    {

        for(Entity e: entity)
        {
            if(e.getId()!=Id.particle &&Game.getVisiableArea() != null && e.getBounds().intersects(Game.getVisiableArea()))
                e.render(g);
        }
        for(Tile t: tile)
        {
            if(Game.getVisiableArea() != null && t.getBounds().intersects(Game.getVisiableArea()))

                t.render(g);
        }
        for(Entity e: entity)
        {
            if(e.getId()==Id.particle &&Game.getVisiableArea() != null && e.getBounds().intersects(Game.getVisiableArea()))
                e.render(g);
        }

        g.drawImage(coin.getBufferedImage(),Game.getVisiableArea().x +20,Game.getVisiableArea().y+20, 75, 75, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier", Font.BOLD, 20));
        g.drawString("x" + coins, Game.getVisiableArea().x+100, Game.getVisiableArea().y+95);

        g.drawImage(goblin[1].getBufferedImage(),Game.getVisiableArea().x +20,Game.getVisiableArea().y+90, 75, 75, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier", Font.BOLD, 20));
        g.drawString("x" + (goblinsLimit-goblins), Game.getVisiableArea().x+100, Game.getVisiableArea().y+150);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier", Font.BOLD, 30));
        g.drawString(""+timer, Game.getVisiableArea().x+850, Game.getVisiableArea().y+30);

        if(gateOpened)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 50));
            g.drawString("The gates are open!", Game.getVisiableArea().x+200, Game.getVisiableArea().y+250);
        }
        if(findChest)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 50));
            g.drawString("Find a chest!", Game.getVisiableArea().x+250, Game.getVisiableArea().y+250);
        }
    }

    public void tick()
    {

        for(Entity en: entity)
        {
                en.tick();
        }
        for(Tile ti: tile)
        {
            if(Game.getVisiableArea() != null && ti.getBounds().intersects(Game.getVisiableArea()))
                ti.tick();
        }
    }

    public void addEntity(Entity en)
    {
        entity.add(en);
    }

    public void removeEntity(Entity en)
    {
        entity.remove(en);
    }

    public void addTile(Tile ti)
    {
        tile.add(ti);
    }

    public void removeTile(Tile ti)
    {
        tile.remove(ti);
    }

    public void createLevel(BufferedImage level)
    {
        int width = level.getWidth();
        int height = level.getHeight();

        for(int y = 0;y < height;y++)
        {
            for (int x = 0;x < width;x++)
            {
                int pixel = level.getRGB(x, y);

                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if(red==0 && green==0 && blue==0)
                    addTile(new Wall(x*64, y*64, 64, 64,true, Id.wall, this));//64

                if(red==255 && green==0 && blue==0)
                    addTile(new Chest(x*64, y*64, 64, 64, true,Id.chest, this, Game.growPotion, 0));

                if(red==255 && green==10 && blue==0)
                    addTile(new Chest(x*64, y*64, 64, 64, true,Id.chest, this, Game.lifePotion, 1));

                if(!goblinCreated)
                if(red==0 && green==255 && blue==0)
                {
                    addEntity(new Goblin(x*64, y*64, 64, 64, Id.goblin, this));
                    goblinCreated = true;
                }

                if(red==0 && (green>123 && green < 129 ) && blue==0)
                    addTile(new Ladder(x*64, y*64, 64, 64*15, true, Id.ladder, this, 128-green, true));

                if(red==255 && green==252 && blue==9)
                    addTile(new Coin(x*64, y*64, 64, 64, true, Id.coin, this));

                if(red==255 && green==0 && blue == 255)
                    addEntity(new TowerBoss(x*64, y*64, 64, 64, Id.towerBoss, this, 3));

                if(red==255 && green==100 && blue == 255)
                    addEntity(new Lizard(x*64, y*64, 64, 64, Id.lizard, this));

                if(red==75 && green==0 && blue==0)
                    addTile(new Gate(x*64, y*64, 64, 64*5, true, Id.gate, this));

                if(red==111 && green==111 && blue == 111)
                    addEntity(new Bean(x*64, y*64, 64, 64, Id.bean, this));

                if(!created)
                    if(red==0 && green==0 && blue==255)
                    {

                        addEntity(new Player(x*64, y*64, 48,48, Id.player, this));
                        created = true;
                    }

            }
        }
        Game.deathY = Game.getDeathY();
    }
    public void clearLevel()
    {
        goblins = 0;
        entity.clear();
        tile.clear();
    }

    public void createGoblin() {
        if(goblins==1)
            addEntity(new Goblin(1300, 1500, 64, 64, Id.goblin, this));
        else if(goblins==2)
            addEntity(new Goblin(600, 900, 64, 64, Id.goblin, this));
        else if(goblins==3)
            addEntity(new Goblin(2500, 1500, 64, 64, Id.goblin, this));
        goblinCreated = true;
        Goblin.stade = GoblinState.ALIVE;
    }
}
