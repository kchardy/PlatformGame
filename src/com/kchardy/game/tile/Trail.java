package com.kchardy.game.tile;

import com.kchardy.game.Handler;
import com.kchardy.game.Id;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Trail extends Tile {

    private float alpha = 1.0f;

    private BufferedImage image;

    public Trail(int x, int y, int width, int height, boolean solid, Id id, Handler handler, BufferedImage image) {
        super(x, y, width, height, solid, id, handler);
        this.image = image;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.drawImage(image, getX(), getY(), width, height, null);

    }

    @Override
    public void tick() {
        alpha-=0.05;

        if(alpha<0.05)
            die();
    }
}
