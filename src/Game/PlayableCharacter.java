package Game;

import Shared.Constants;

import java.awt.*;

class PlayableCharacter extends MovingObj	{

	private boolean grounded;
	private byte[] images;
	private boolean leftKey, rightKey, upKey;
	private boolean addLifeFlag, subLifeFlag;
	private int spawnX, spawnY;
	
	PlayableCharacter(byte[] images,int spawnX, int spawnY)
	{

		//adds parameters to superclass constructor
		super(0, 0, 2, images[0], spawnX, spawnY);

		//initializes fields
		grounded = false;
		this.images = images;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
	}

	void getControls(Input input)
	{
        leftKey = input.get(0);
        rightKey = input.get(1);
        upKey = input.get(2);
	}

	void move(double delta)
	{
		//if not on the ground, apply gravity
		if (!grounded)
		{
			vsp += grav;
		}

		//go left
		if (leftKey)
		{
			hsp -= 3;
			flip = true;
		}

		//go right
		if (rightKey)
		{
			hsp += 3;
			flip = false;
		}

		//jump
		if (upKey && grounded)
		{
			vsp = -20;
		}

		//limit hsp
		if (hsp > 8) {
			hsp = 8;
		}
		if (hsp < -8) {
			hsp = -8;
		}

		//friction
		if (hsp > 0) hsp -= 1;
		if (hsp < 0) hsp += 1;

		//limit vsp
		if (vsp > 12) vsp = 12;

		hsp*=delta;
		vsp*=delta;

		//add vsp and hsp to x and y
		super.move();
	}

	void updateSprites()
	{
		//if player is standing still on the ground, display standing animation
		//if player is moving while on the ground, display walking animation
		//if player is off the ground, display jumping/falling animation

		if (hsp == 0) image = images[0];
		else image = images[1];
		if (!grounded) image = images[2];
	}

	int getFeet()
	{
		//returns y + height
		return y + Constants.PLAYER_HEIGHT;
	}

	private int getrightSide()
	{
		//returns x + width
		return x + Constants.PLAYER_WIDTH;
	}

	boolean getGrounded()
	{
		//encapsulates grounded
		return grounded;
	}

	void setGrounded(boolean grounded)
	{
		//encapsulates grounded
		this.grounded = grounded;
	}

    Rectangle getBounds()
    {
		//returns the player's collision mask in the form of a Rectangle
    	return new Rectangle(x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
    }

	public int getSpawnX()
	{
		//
		return spawnX;
	}

	public void setSpawnX(int spawnX)
	{
		//
		this.spawnX = spawnX;
	}

	public int getSpawnY()
	{
		//
		return spawnY;
	}

	public void setSpawnY(int spawnY)
	{
		//
		this.spawnY = spawnY;
	}
}