package com.mezydlo.JavaGame;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3; // window will be 900 x 3 * height so performace will be like it's 300

    private Thread thread;
    private boolean running = false;
    private JFrame frame;



    public Game(){
        // setting resolution
        Dimension size = new Dimension(width * scale, height * scale);
        // this method is in Canvas (Component)
        setPreferredSize(size);
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
        while(running){
            update();
            render();

        }
    }

    public void update(){

    }

    public void render(){
        // rendering uses buffering
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            // this is to make sure that BufferStrategy has been created
            // 3 almost always - it means we use 3 buffers -> the original screen, the first buffer and the second
            // now we don't have to wait with rendering the 3rd image to copy the 2nd buffer to the screen, we can
            // create another frame while copying our buffer
            // more buffers will not change our performance tho, which makes sense.
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics(); // linking buffer to graphics (ability to draw things)
        // here we do all graphics

        g.setColor(Color.PINK);
        g.fillRect(0, 0, getWidth(), getHeight()); // returns size of the window
        g.dispose(); //releases resources it's like free() in C
        bs.show(); // shows the buffer

    }

    public static void main(String[] args){
        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setTitle("JavaGame Yay");
        game.frame.add(game);
        game.frame.pack(); // window will be the same size as our Game instance (component)
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game.frame.setLocationRelativeTo(null); // will appear in the center of the screen
        game.frame.setVisible(true); // shows frame

        game.start();
    }

}
