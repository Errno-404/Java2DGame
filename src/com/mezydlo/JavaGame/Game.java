package com.mezydlo.JavaGame;


import com.mezydlo.JavaGame.graphics.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {

    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3; // window will be 900 x 3 * height so performance will be like it's 300

    private Thread thread;
    private boolean running = false;
    private JFrame frame;

    private Screen screen;


    // raster need to be clarified!

    // we create an image with size of width x height (without scale tho!) and set its type to RGB (without alpha)
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // here we create an integer array which can actually write pixels to the image. To do so we need to
    // firstly we get a raster of this image which represents those pixels, then we get a DataBufferInt out of it, and
    // finally we get raw Data so our integer array.
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public static String title = "Rain";

    public Game() {
        // setting resolution
        Dimension size = new Dimension(width * scale, height * scale);
        // this method is in Canvas (Component)
        setPreferredSize(size);
        screen = new Screen(width, height);

        frame = new JFrame();
    }


    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }


    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();


        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        int frames = 0;
        int updates = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                update();
                updates ++;
                delta --;
            }

            render();
            frames ++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames + ", UPS: " + updates);
                frame.setTitle(title + " | " + "FPS: " + frames + ", UPS: " + updates);
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    public void update() {

    }

    public void render() {
        // rendering uses buffering
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            // this is to make sure that BufferStrategy has been created
            // 3 almost always - it means we use 3 buffers -> the original screen, the first buffer and the second
            // now we don't have to wait with rendering the 3rd image to copy the 2nd buffer to the screen, we can
            // create another frame while copying our buffer
            // more buffers will not change our performance tho, which makes sense.
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        screen.render();
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics(); // linking buffer to graphics (ability to draw things)
        // here we do all graphics

//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, getWidth(), getHeight()); // returns size of the window

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        g.dispose(); //releases resources it's like free() in C
        bs.show(); // shows the buffer

    }

    public static void main(String[] args) {
        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setTitle(Game.title);
        game.frame.add(game);
        game.frame.pack(); // window will be the same size as our Game instance (component)
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game.frame.setLocationRelativeTo(null); // will appear in the center of the screen
        game.frame.setVisible(true); // shows frame

        game.start();
    }

}
