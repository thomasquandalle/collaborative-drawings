package utils;

import java.io.Serializable;

public class NetworkImage implements Serializable {
    private int width, height;
    private byte[] pixelsBuffer;

    public NetworkImage(byte[] pixelsBuffer,int width, int height ){
        this.width = width;
        this.pixelsBuffer = pixelsBuffer;
        this.height = height;
        System.out.println("iuo");
    }

    public byte[] getBuffer() {
        return pixelsBuffer;
    }
    public int getWidth(){
        return width;
    }


    public int getHeight() {
        return height;
    }
}
