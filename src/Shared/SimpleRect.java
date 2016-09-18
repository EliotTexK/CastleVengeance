package Shared;

class SimpleRect {
    private int x;
    private int y;
    private byte width;
    private byte height;

    public SimpleRect(int x, int y, byte width, byte height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    int getX()
    {
        //encapsulates x
        return x;
    }

    int getY()
    {
        //encapsulates y
        return y;
    }

    byte getWidth()
    {
        //encapsulates width
        return width;
    }

    byte getHeight()
    {
        //encapsulates height
        return height;
    }

    boolean intersects(SimpleRect rect)
    {
        if ((((rect.getX()>=x)&&(rect.getX()<=x+width))
                &&((rect.getY()>=y)&&(rect.getY()<=y+height)))
                || (((x>=rect.getX())&&(x<=rect.getX()+rect.getWidth()))
                &&((y>=rect.getY())&&(y<=rect.getY()+rect.getHeight()))))
        {
            return true;
        }
        return false;
    }
}
