package com.kchardy.game.tile;

import com.kchardy.game.Game;
import com.kchardy.game.Handler;
import com.kchardy.game.Id;
import com.kchardy.game.com.kchardy.game.graphics.Sprite;
import com.kchardy.game.entity.powerup.Potion;
import com.kchardy.game.entity.powerup.Staff;

import java.awt.*;

import static com.kchardy.game.Game.chest;

public class Chest extends Tile {

    public static boolean isVisible = false;

    private Sprite powerUp;
    private boolean poppedUp = false;
    private int spriteY = getY();
    private int type;

    public Chest(int x, int y, int width, int height, boolean solid, Id id, Handler handler, Sprite powerUp, int type) {
        super(x, y, width, height, solid, id, handler);
        this.powerUp = powerUp;
        this.type = type;
    }

    @Override
    public void render(Graphics g) {
        if(!poppedUp && isVisible)
            g.drawImage(chest.getBufferedImage(), x, spriteY, width, height, null);
        if(!activated && isVisible)
            g.drawImage(chest.getBufferedImage(), x, y, width, height, null);
        else if(activated && isVisible)
            g.drawImage(Game.openedChest.getBufferedImage(), x, y, width, height, null);
    }

    @Override
    public void tick() {

        if(activated && !poppedUp){
            spriteY --;
            if(spriteY <=y - height)
            {
                if(powerUp == Game.lifePotion || powerUp == Game.growPotion)
                     handler.addEntity(new Potion(x, spriteY, width, height, Id.potion, handler, type));
                else if(powerUp == Game.staff)
                    handler.addEntity(new Staff(x, spriteY, width, height, Id.staff, handler));

                poppedUp = true;

            }
        }
    }
}
