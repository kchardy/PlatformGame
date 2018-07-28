package com.kchardy.game.com.kchardy.game.input;

import com.kchardy.game.Game;
import com.kchardy.game.Id;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.tile.Tile;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        for(Entity en: Game.handler.entity)
        {
            if(en.getId() == Id.player)
            {
                if(en.goingDown)
                    return;
                switch (key)
                {
                    case KeyEvent.VK_W:
                        for(int k = 0; k<Game.handler.tile.size();k++)
                        {
                            Tile t = Game.handler.tile.get(k);
                            if(t.getId() == Id.ladder)
                            {
                                if(en.getBoundsTop().intersects(t.getBounds()))
                                {
                                    if(!en.goingDown)
                                    {
                                        en.goingDown = true;
                                    }
                                }
                            }
                        }
                        // en.velY += -1;  przyspiesza jak dluzej przytrzymasz
                        // en.setVelY(-1);
                        if(!en.jumping)
                        {
                            en.jumping = true;
                            en.gravity = 9.0;
                        }
                        break;
                  case KeyEvent.VK_S:
                    for(int j = 0; j<Game.handler.tile.size();j++)
                    {
                        Tile t = Game.handler.tile.get(j);
                        if(t.getId() == Id.ladder)
                        {
                            if(en.getBoundsBottom().intersects(t.getBounds()))
                            {
                                if(!en.goingDown)
                                {
                                    en.goingDown = true;
                                }
                            }
                        }
                    }
                    break;
                    case KeyEvent.VK_A:
                        // en.velX += -1;
                        en.setVelX(-1);
                        en.facing = 1;
                        break;
                    case KeyEvent.VK_D:
                        //en.velX += 1;
                        en.setVelX(1);
                        en.facing = 0;
                        break;
                    case KeyEvent.VK_Q:
                        en.die();
                        break;

                }
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        for(Entity en: Game.handler.entity)
        {
            if(en.getId() == Id.player)
            {
                switch (key)
                {
                    case KeyEvent.VK_W:
                        en.setVelY(0);
                        break;
                    case KeyEvent.VK_S:
                        en.setVelY(0);
                        break;
                    case KeyEvent.VK_A:
                        en.setVelX(0);
                        break;
                    case KeyEvent.VK_D:
                        en.setVelX(0);
                        break;
                }
            }

        }
    }
}
