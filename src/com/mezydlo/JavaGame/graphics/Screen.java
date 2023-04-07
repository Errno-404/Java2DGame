package com.mezydlo.JavaGame.graphics;

import java.util.Random;

public class Screen {
    private int width, height;
    public int[] pixels;
    public int[] tiles = new int[64 * 64]; // 64 tiles by 64 tiles
    private Random random = new Random();

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];

        for (int i = 0; i < 64 * 64; i++) {
            tiles[i] = random.nextInt(0xffffff);
        }


    }


    public void render() {

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int tileIndex = (x >> 4) + (y >> 4) * 64; // finding the correct tile - each tile is 32 x 32, tiles = 64
                pixels[x + y * width] = tiles[tileIndex]; // rendering the color
            }
        }
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }
}
