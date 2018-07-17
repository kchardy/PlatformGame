package com.kchardy.game;

import com.kchardy.game.com.kchardy.game.graphics.Sprite;
import com.kchardy.game.com.kchardy.game.graphics.SpriteSheet;
import com.kchardy.game.com.kchardy.game.input.KeyInput;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends Canvas implements Runnable{

    public static final int WIDTH = 270;
    public static final int HEIGHT = WIDTH/14*10;
    public static final int SCALE = 3;//4
    public static final String TITLE = "Mag Eryk";

    private Thread thread;
    private boolean running = false;
    private BufferedImage image;

    public static Handler handler;
    public static SpriteSheet sheet;
    public static Sprite player[] = new Sprite[12];
    public static Sprite grass;
    public static Sprite mushroom;
    public static Camera cam;

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
        sheet = new SpriteSheet("/spritesheet.png");
        cam = new Camera();

        addKeyListener(new KeyInput());

        grass = new Sprite(sheet ,1, 1);
        mushroom = new Sprite(sheet,2, 1);


        for(int i = 0; i < player.length; i++)
        {
            player[i] = new Sprite(sheet, i+1, 16);
        }

        try
        {
            image = ImageIO.read(getClass().getResource("/level3.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        handler.createLevel(image);

        handler.addEntity(new Player(100, 500, 64, 64, false, Id.player, handler));// on go nie ma, ale mi z tym dzialalo(do sprawdzenia)
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
        g.setColor(Color.BLACK);
        g.fillRect(0,0, getWidth(), getHeight());
        g.translate(cam.getX(), cam.getY());


        handler.render(g);


        g.dispose();
        bs.show();

    }

    public void update()
    {
        handler.tick();

        for(Entity e:handler.entity)
        {
            if(e.getId() == Id.player)
            {
                cam.tick(e);
            }
        }
        render();
    }

    public static int getFrameWidth()
    {
        return WIDTH*SCALE;
    }

    public static int getFrameHeight()
    {
        return HEIGHT*SCALE;
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
