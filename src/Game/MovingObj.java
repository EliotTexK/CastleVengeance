package Game;

import Shared.Sprite;

class MovingObj extends Sprite {
	protected int hsp;
	protected int vsp;
	protected int grav;

	MovingObj(byte superimage, int superx, int supery, byte width, byte height)
	{
		//adds parameters to superclass constructor
		super(superimage, superx, supery, width, height);
		hsp = 0;
		vsp = 0;
		grav = 2;
	}

	void move()
	{
		//adds hsp and vsp to x and y
		x += hsp;
		y += vsp;
	}

	void setHsp(int hsp)
	{
		//sets gravity
		this.hsp = hsp;
	}

	void setVsp(int vsp)
	{
		//sets gravity
		this.vsp = vsp;
	}

	int getVsp()
	{
		return vsp;
	}

	int getHsp()
	{
		return hsp;
	}
}