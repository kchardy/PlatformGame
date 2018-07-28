package com.kchardy.game.com.kchardy.game.input;

import com.kchardy.game.Game;
import com.kchardy.game.com.kchardy.game.graphics.gui.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseListener, MouseMotionListener {

    int x, y;
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (int i = 0; i< Game.launcher.buttons.length; i++)
        {
            Button button = Game.launcher.buttons[i];

            if(x>=button.getX() && y>= button.getY() &&
                    x<=button.getX() + button.getWidth() && y<=button.getY()+button.getHeight())
                button.triggerEvent(); //it's kind of like creating a ractangle or where
            // mouse has to be in order for this code to get cold

        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
