package com.kchardy.game;

import com.kchardy.game.tile.Coin;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.entity.mob.Goblin;
import com.kchardy.game.entity.mob.Player;
import com.kchardy.game.entity.mob.TowerBoss;
import com.kchardy.game.tile.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Handler {

    public LinkedList <Entity> entity = new LinkedList<Entity>();
    public LinkedList <Tile> tile = new LinkedList<Tile>();

    public Handler()
    {

    }

    public void render(Graphics g)
    {
        for(Entity en: entity)
        {
            if(Game.getVisiableArea() != null && en.getBounds().intersects(Game.getVisiableArea()))
                en.render(g);
        }
        for(Tile ti: tile)
        {
            if(Game.getVisiableArea() != null && ti.getBounds().intersects(Game.getVisiableArea()))

                ti.render(g);
        }

    }

    public void tick()
    {

        for(Entity en: entity)
        {
           // if(Game.getVisiableArea() != null && en.getBounds().intersects(Game.getVisiableArea()))
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
               // if(red==0 && green==0 && blue==255)
                //    addEntity(new Player(x*64, y*64, 64, 64,false, Id.player, this));
                if(red==255 && green==0 && blue==0)
                    addTile(new Chest(x*64, y*64, 64, 64, true,Id.chest, this, Game.lifePotion, 1));
                   // addEntity(new Potion(x*64, y*64, 64, 64, Id.growPotion, this));
                if(red==0 && green==255 && blue==0)
                    addEntity(new Goblin(x*64, y*64, 64, 64, Id.goblin, this));
                if(red==0 && (green>123 && green < 129 ) && blue==0)
                    addTile(new Ladder(x*64, y*64, 64, 64*15, true, Id.ladder, this, 128-green));
                if(red==0 && green==0 && blue==255)
                    addEntity(new Player(x*64, y*64, 48,48, Id.player, this));
                if(red==255 && green==252 && blue==9)
                    addTile(new Coin(x*64, y*64, 64, 64, true, Id.coin, this));
                if(red==255 && green==0 && blue == 255)
                    addEntity(new TowerBoss(x*64, y*64, 64, 64, Id.towerBoss, this, 3));


            }
        }
//        for(int i = 0; i < Game.WIDTH*Game.SCALE/64+1; i++)
//        {
//            addTile(new Wall(i*64, Game.HEIGHT*Game.SCALE - 6, 64, 64, true, Id.wall, this)); // -64 zamiast 6
//            if(i!=0 && i!=1 && i!=7 && i!=8 && i!=9)
//                addTile(new Wall(i*64, 300, 64, 32, true, Id.wall, this)); // -64 zamiast 6
//
//        }
    }
    public void clearLeve()
    {
        entity.clear();
        tile.clear();
    }
}
