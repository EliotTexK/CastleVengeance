package Game;

class Camera {
    private PlayableCharacter player;
    private int xplus;
    private int yplus;
    private int ground;
    int x;
    int y;

    Camera(PlayableCharacter player)
    {
        //initializes fields
        this.player = player;
        xplus = 0;
        yplus = 0;
        ground = player.getX();
        x = player.getX();
        y = player.getY();
    }

    void update(double delta)
    {
        //convert player.flip to negative or positive 1
        int flip;
        if (player.isFlip()) flip = 1;
        else flip = -1;

        /*HORIZONTAL SCROLLING
        If the player object is moving, scroll the
        camera in the opposite direction of where it's going
        in order to make scrolling easier to react to.
        If the player is standing still and on the ground,
        slowly scroll in the direction it is facing */

        if ((player.hsp == 0))
            xplus -= flip;
        if (player.hsp > 0)
            xplus += 1;
        if (player.hsp < 0)
            xplus -= 1;

        if (xplus > 32)
            xplus = 32;
        if (xplus < -32)
            xplus = -32;

        x = player.getX() + xplus;

        /*VERTICAL SCROLLING
        If the player is grounded, move the camera to it's y position
        gradually
        */
        if (player.getY() > ground +32 || player.getY() < ground -96) ground = player.getY();

        if (player.getGrounded())
        {
            if (y > ground) {
                yplus = -8;
                if (y + yplus < ground)
                {
                    y = ground;
                    yplus = 0;
                }
            }
        }

        if (y < ground) {
            yplus = 8;
            if (y + yplus > ground)
            {
                y = ground;
                yplus = 0;
            }
        }

        if ((y + yplus > ground && yplus > 0) || (y + yplus < ground && yplus < 0))
        {
            yplus = 0;
        }

        y += yplus*delta;
    }
}