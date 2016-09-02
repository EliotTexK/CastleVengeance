package Shared;

public class TileSet
{
    private byte[][] tiles;

    public TileSet(int width, int height)
    {
        tiles = new byte[width+1][height+1];
        System.out.println("TileSet created");
        System.out.println("Width (in blocks): " + getWidth());
        System.out.println("Height (in blocks): " + getLength());
    }

    public void makeRect(int startx, int starty, int endx, int endy, int kindoftile)
    {
        //makes a rectangle of tiles
        int rectwidth = endx-startx;
        int rectheight = endy-starty;
        for (int i = 0; i < rectwidth; i++) {
            for (int j = 0; j < rectheight; j++) {
                try {
                    tiles[startx + i][starty + j] = (byte) kindoftile;
                }
                catch (ArrayIndexOutOfBoundsException a) {}
            }
        }
    }

    public void makePlatform(int startx, int starty, int end, int kindoftile)
    {
        //makes a platform (horizontal line of tiles)
        int linewidth = end-startx;
        for (int i = 0; i < linewidth; i++) {
            try{
                tiles[startx+i][starty] =(byte) kindoftile;
            }
            catch (ArrayIndexOutOfBoundsException a) {}
        }
    }

    public void makeWall(int startx, int starty, int end, int kindoftile)
    {
        //makes a wall (vertical line of tiles)
        int length = end-startx;
        for (int j = 0; j < length; j++) {
            try {
                tiles[startx][starty + j] = (byte) kindoftile;
            }
            catch (ArrayIndexOutOfBoundsException a) {}
        }
    }

    public void makeTile(int x, int y, int kindoftile)
    {
        try {
            tiles[x][y] = (byte) kindoftile;
        }
        catch (ArrayIndexOutOfBoundsException a) {}
    }

    public int getLength()
    {
        return tiles.length;
    }

    public int getWidth()
    {
        return tiles[0].length;
    }

    public byte[][] getTiles()
    {
        return tiles;
    }

    public void setTiles(byte[][] tiles)
    {
        this.tiles = tiles;
    }
}