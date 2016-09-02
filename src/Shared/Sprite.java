package Shared;

public class Sprite {
	protected byte image;
	protected boolean flip;
	protected int x;
	protected int y;

	public Sprite(byte image, int x, int y)
	{
		//initializes fields
		this.image = image;
		this.x = x;
		this.y = y;

		flip = true;
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

	public byte getImage()
	{
		//encapsulates image (no setter)
		return image;
	}

	public boolean isFlip() {
		//encapsulates flip
		return flip;
	}

	public void setFlip(boolean flip) {
		//encapsulates flip
		this.flip = flip;
	}
}