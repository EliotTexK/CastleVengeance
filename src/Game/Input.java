package Game;

import java.lang.Boolean;

class Input {

    private Boolean[] controls;

    public Input(boolean leftKey,boolean rightKey,boolean upKey)
    {
        controls = new Boolean[]{leftKey,rightKey,upKey};
    }

    boolean get(int index)
    {
        return controls[index];
    }
}