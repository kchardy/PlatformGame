package com.kchardy.game;

import com.kchardy.game.com.kchardy.game.Sound;
import com.kchardy.game.com.kchardy.game.graphics.Sprite;
import com.kchardy.game.com.kchardy.game.graphics.SpriteSheet;
import com.kchardy.game.com.kchardy.game.graphics.gui.Launcher;
import com.kchardy.game.com.kchardy.game.input.KeyInput;
import com.kchardy.game.com.kchardy.game.input.MouseInput;
import com.kchardy.game.entity.Entity;
import com.kchardy.game.tile.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Game extends Canvas implements Runnable{

    public static final int WIDTH = 320;
    public static final int HEIGHT = 180;
    public static final int SCALE = 3;
    public static final String TITLE = "Mag Eryk";

    private Thread thread;
    private boolean running = false;

    private static BufferedImage[] imageLevel;
    private static BufferedImage background;

    private static int playerX, playerY;
    private static int level = 0;

    public static int coins = 0;
    public static int lives = 5;
    public static int deathScreenTime = 0;
    public static int deathY = 0;

    public static boolean deathScreen = true;
    public static boolean gameOver = false;
    public static boolean playing = false;

    public static Handler handler;
    public static SpriteSheet sheet;
    public static Camera cam;
    public static Launcher launcher;
    public static MouseInput mouseInput;

    public static Sprite player[] = new Sprite[12];
    public static Sprite playerStaff[] = new Sprite[12];
    public static Sprite goblin[] = new Sprite[10];
    public static Sprite particle[] = new Sprite[6];
//    public static Sprite fireball[] = new Sprite[3];
    public static Sprite fireball;


    public static Sprite brick;
    public static Sprite growPotion;
    public static Sprite lifePotion;
    public static Sprite magicBean;
    public static Sprite chest;
    public static Sprite openedChest;
    public static Sprite coin;
    public static Sprite staff;
    public static Sprite gate;


    public static Sound jump;
    public static Sound goombastomp;
    public static Sound levelcomplite;
    public static Sound losealife;
    public static Sound themesong;


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
        sheet = new SpriteSheet("/spritesheet2.png");
        cam = new Camera();
        launcher = new Launcher();
        mouseInput = new MouseInput();
        imageLevel = new BufferedImage[2];

        addKeyListener(new KeyInput());
        addMouseMotionListener(mouseInput);
        addMouseListener(mouseInput);

        brick = new Sprite(sheet ,1, 1);
        growPotion = new Sprite(sheet,2, 1);
        chest = new Sprite(sheet, 3, 1);
        openedChest = new Sprite(sheet, 4, 1);
        coin = new Sprite(sheet, 5, 1);
        lifePotion = new Sprite(sheet, 6,1);
        gate = new Sprite(sheet,7,1 );
        magicBean = new Sprite(sheet,8,1);
            fireball = new Sprite(sheet, 1, 12);

        for(int i = 0; i < player.length; i++)
        {
            player[i] = new Sprite(sheet, i+1, 16);
        }

        for(int i = 0; i < goblin.length; i++)
        {
            goblin[i] = new Sprite(sheet, i+1, 15);
        }

        for(int i = 0; i < playerStaff.length; i++)
        {
            playerStaff[i] = new Sprite(sheet, i+1, 14);
        }

        for(int i = 0; i < particle.length; i++)
        {
            particle[i] = new Sprite(sheet, i+1, 13);
        }

//        for(int i = 0; i < fireball.length; i++)
//        {
//            fireball[i] = new Sprite(sheet, i+1, 12);
//        }



        try
        {
//            for (int i = 0;i<imageLevel.length;i++)
//            {
//                imageLevel[i] = ImageIO.read(getClass().getResource("/level" + level + ".png"));//level3
//            }
                imageLevel[1] = ImageIO.read(getClass().getResource("/level3.png" )); //"/level" + level + ".png"));//level3
                imageLevel[0] = ImageIO.read(getClass().getResource("/level0.png" )); //"/level" + level + ".png"));//level3
            background = ImageIO.read(getClass().getResource("/background.jpg"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

     //   jump = new Sound("/jump.mp3"); zmienic na .wave lub "/audio/jump.wav
//        jump = new Sound("/jump.mp3"); make this same for others
//        jump = new Sound("/jump.mp3");
//        jump = new Sound("/jump.mp3");
//        jump = new Sound("/jump.mp3");

   //     handler.createLevel(imageLevel);

       // handler.addEntity(new Player(100, 500, 64, 64, Id.player, handler));//
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
        g.setColor(Color.BLACK); //black background
        g.fillRect(0,0, getWidth(), getHeight());
        if(!deathScreen)
        {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);

//            g.drawImage(coin.getBufferedImage(),20,20, 75, 75, null);
//            g.setColor(Color.WHITE);
//            g.setFont(new Font("Courier", Font.BOLD, 20));
//            g.drawString("x" + coins, 100, 95);
        }
        if(deathScreen)
        {
            if(!gameOver)
            {
                g.drawImage(player[7].getBufferedImage(),(WIDTH*SCALE)/2,(HEIGHT*SCALE)/2, 75, 75, null);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Georgia", Font.BOLD, 50));
                g.drawString("x " + lives, (WIDTH*SCALE)/2 + 100, (HEIGHT*SCALE)/2 + 70);
            }
            else
            {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Georgia", Font.BOLD, 50));
                g.drawString("GAME OVER", (WIDTH*SCALE)/2 + 100, (HEIGHT*SCALE)/2 + 70);
            }
        }
        if(playing)
            g.translate(cam.getX(), cam.getY());
        if(!deathScreen && playing)
            handler.render(g);
        else if(!playing)
            launcher.render(g);
        g.dispose();
        bs.show();
    }

    public void update()
    {
        if(playing)
            handler.tick();

        for(Entity e:handler.entity)
        {
            if(e.getId() == Id.player)
            {
                if(!e.goingDown)
                    cam.tick(e);
            }
        }
        if(deathScreen && !gameOver && playing)
            deathScreenTime++;
        if(deathScreenTime >= 180)
        {
            if(!gameOver)
            {
                deathScreen = false;
                deathScreenTime = 0;
                handler.clearLevel();
                handler.createLevel(imageLevel[level]);

              //  themesong.play();
            }
            else if(gameOver)
            {
                deathScreen = false;
                deathScreenTime = 0;
                playing = false;
                gameOver = false;
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

    public static void switchLevel()
    {
        level++;
        handler.clearLevel();
        handler.createLevel(imageLevel[level]);

        themesong.close();
        levelcomplite.play();
    }

    public static Rectangle getVisiableArea()
    {
        for(int i =0; i<handler.entity.size();i++)
        {
            Entity e = handler.entity.get(i);
            if(e.getId() == Id.player)
            {
                if(!e.goingDown)
                playerX = e.getX();
                playerY = e.getY();
                return new Rectangle(playerX - (getFrameWidth()/2-5), playerY - (getFrameHeight()/2-5),
                        getFrameWidth()+5, getFrameHeight()+5);
            }
        }
        return new Rectangle(playerX - (getFrameWidth()/2-5), playerY - (getFrameHeight()/2-5),
                getFrameWidth()+5, getFrameHeight()+5);
    }

    public static int getDeathY()
    {
        LinkedList<Tile> tempList = handler.tile;

        Comparator <Tile> tileSorier = new Comparator<Tile>() {
            @Override
            public int compare(Tile t1, Tile t2) {
                if(t1.getY()>t2.getY()) return -1;
                if(t1.getY()<t2.getY()) return 1;
                return 0;
            }
        };
        Collections.sort(tempList,tileSorier);
        return tempList.getFirst().getY() + tempList.getFirst().height;
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
