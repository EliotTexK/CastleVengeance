package Shared;

public class Sprite {
	protected byte image;
	protected boolean flip;
	protected int x,y;
	protected byte width,height;

	public Sprite(byte image, int x, int y, byte width, byte height)
	{
		//initializes fields
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		flip = true;
	}

	public byte getImage()
	{
		//encapsulates image (no setter)
		return image;
	}

	public int getX()
	{
		//encapsulates x
		return x;
	}

	public void setX(int x)
	{
		//encapsulates x
		this.x = x;
	}

	public int getY()
	{
		//encapsulates y
		return y;
	}

	public void setY(int y)
	{
		//encapsulates y
		this.y = y;
	}

	public byte getWidth() {
		return width;
	}

	public void setWidth(byte width) {
		this.width = width;
	}

	public byte getHeight() {
		return height;
	}

	public void setHeight(byte height) {
		this.height = height;
	}

	public boolean isFlip() {
		//encapsulates flip
		return flip;
	}

	public void setFlip(boolean flip) {
		//encapsulates flip
		this.flip = flip;
	}

	SimpleRect bounds()
	{
		//maybe could be used as collision detection
		return new SimpleRect(x,y,width,height);
	}
}