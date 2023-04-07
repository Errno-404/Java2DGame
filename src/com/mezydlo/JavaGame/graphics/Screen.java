package com.mezydlo.JavaGame.graphics;

public class Screen {
    private int width, height;
    public int[] pixels;

    int time =0, count = 0, ytime = 0;

    public Screen(int width, int height){
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }


    public void render(){
        count ++;
        if(count % 100 == 0) time++;
        if(count % 80 == 0) ytime++;
        for(int y = 0; y < height; y++){
            if(ytime < 0 || ytime >= height) break;
            for(int x = 0 ; x < width; x++){
                if(time < 0 || time >= width) break;
                pixels[time + ytime *width] = 0xff00ff;
            }
        }
    }

    public void clear(){
        for(int i = 0; i < pixels.length; i++){
            pixels[i] = 0;
        }
    }
}
