package com.kchardy.game;

import javax.swing.*;
import java.awt.*;

public class Game extends Canvas implements Runnable{

    public static final int WIDTH = 270;
    public static final int HEIGHT = WIDTH/14*10;
    public static final int SCALE = 4;
    public static final String TITLE = "Mario kiepska kopia";

    private Thread thread;
    private boolean running = false;

    private synchronized void start()
    {
        if(running) return;
        running = true;
        thread = new Thread(this, "Thread");
        thread.start();
    }

    private synchronized void stop()
    {
        if(!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while(running)
        {
            render();
            update();
        }
    }

    public void render()
    {

    }

    public void update() //tick()
    {

    }
    public Game()
    {
        Dimension size = new Dimension(WIDTH*SCALE, HEIGHT *SCALE);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    public static void main(String[] args) {

        Game game = new Game();
        JFrame frame = new JFrame(TITLE);
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.start();
    }


}
