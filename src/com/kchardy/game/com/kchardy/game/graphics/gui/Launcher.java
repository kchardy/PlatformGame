package com.kchardy.game.com.kchardy.game.graphics.gui;

import com.kchardy.game.Game;

import java.awt.*;

public class Launcher {

    public Button[] buttons;

    public Launcher()
    {
        buttons = new Button[2];

        buttons[0] = new Button(100, Game.getFrameHeight()/2-100, Game.getFrameWidth()-200, 100, "Start Game");
        buttons[1] = new Button(100, Game.getFrameHeight()/2+100, Game.getFrameWidth()-200, 100, "Exit");
    }
    public void render(Graphics g)
    {
        g.setColor(new Color(2,2,100));
        g.fillRect(0, 0, Game.getFrameWidth(), Game.getFrameHeight());

        for(int i = 0; i<buttons.length;i++)
        {
            buttons[i].render(g);
        }
    }
}
