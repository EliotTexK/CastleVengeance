package Shared;

import Shared.Sprite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Integer;

public class ReadLevel {
    private byte[][]tiles;
    private Sprite[] sprites;
    private int playerStartX;
    private int playerStartY;

    public ReadLevel() {
        this.tiles = null;
        this.sprites = null;
        this.playerStartX = 0;
        this.playerStartY = 0;
    }

    public void read(String filename) {
        String  thisLine = null;
        try{
            // open input stream test.txt for reading purpose.
            BufferedReader br = new BufferedReader(new FileReader(filename));
            int width = Integer.parseInt(br.readLine());
            int height = Integer.parseInt(br.readLine());
            tiles = new byte[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    tiles[i][j] = (byte) Integer.parseInt(br.readLine());
                }
            }
            sprites = new Sprite[]{};
            br.readLine();
            playerStartX = Integer.parseInt(br.readLine());
            playerStartY = Integer.parseInt(br.readLine());
        }
        catch(IOException e){}
    }

    public byte[][] getTiles() {
        return tiles;
    }

    public Sprite[] getSprites() {
        return sprites;
    }

    public int getPlayerStartX() {
        return playerStartX;
    }

    public int getPlayerStartY() {
        return playerStartY;
    }

    public static void main(String[] args)
    {
        ReadLevel r = new ReadLevel();
        r.read("File");
    }
}
