package com.kchardy.game;

import com.kchardy.game.com.kchardy.game.input.KeyInput;
import com.kchardy.game.entity.Player;
import com.kchardy.game.tile.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{

    public static final int WIDTH = 270;
    public static final int HEIGHT = WIDTH/14*10;
    public static final int SCALE = 3;//4
    public static final String TITLE = "Mario kiepska kopia";

    private Thread thread;
    private boolean running = false;

    public static Handler handler;

    public Game()
    {
        Dimension size = new Dimension(WIDTH*SCALE, HEIGHT *SCALE);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    private void init()
    {
        handler = new Handler();

        addKeyListener(new KeyInput());

        handler.addEntity(new Player(100, 500, 64, 64, true, Id.player, handler));
        handler.addTile(new Wall(200, 200, 64, 64, true, Id.wall, handler));
    }

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
        init();
        requestFocus();
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0.0;
        double ns = 1_000_000_000.0 / 60.0;
        int frames = 0;
        int updates = 0;
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime/ns);
            lastTime = now;
            while (delta>=1)
            {
                update();
                updates ++;
                delta --;
            }
            render();
            frames ++;
            if(System.currentTimeMillis() - timer > 1_000)
            {
                timer += 1000;
                frames = 0;
                updates = 0;
            }
        }
        stop();
    }

    public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(new Color(124, 28, 97));//new Color(124, 28, 97)
        g.fillRect(0,0, getWidth(), getHeight());
        g.setColor(Color.MAGENTA);
        g.fillRect(200, 200, getWidth()-400, getHeight()-400);
        handler.render(g);
        g.dispose();
        bs.show(); // wyświetlenie
    }

    public void update() //tick()
    {
        handler.tick();
        render();

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
