package com.kchardy.game.com.kchardy.game.input;

import com.kchardy.game.Game;
import com.kchardy.game.entity.Entity;

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
            switch (key)
            {
                case KeyEvent.VK_W:
                   // en.velY += -1;  przyspiesza jak dluzej przytrzymasz
                   // en.setVelY(-1);
                    if(!en.jumping)
                    {
                        en.jumping = true;
                        en.gravity = 9.0;
                    }


                    break;
//                  case KeyEvent.VK_S:
//                    //en.velY += 1;
//                    en.setVelY(1);
//                    break;
                case KeyEvent.VK_A:
                   // en.velX += -1;
                    en.setVelX(-1);
                    break;
                case KeyEvent.VK_D:
                    //en.velX += 1;
                    en.setVelX(1);
                    break;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        for(Entity en: Game.handler.entity)
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
