package Game;

import Shared.Sprite;

class MovingObj extends Sprite {
	//could be used to achieve particle effects
	protected int hsp;
	protected int vsp;
	protected int grav;
	
	MovingObj(int initialhsp, int initialvsp, int initialgravity,
			byte superimage, int superx, int supery)
	{
		//adds parameters to superclass constructor
		super(superimage, superx, supery);

		//declares initial movement and gravity fields
		hsp = initialhsp;
		vsp = initialvsp;
		grav = initialgravity;
	}

	void move()
	{
		//changes position on screen according
		//to hsp and vsp values then applies gravity
		x += hsp;
		y += vsp;
	}

	protected void applyGravity()
	{
		//apply gravity
		vsp += grav;
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