package Game;

import java.awt.*;

public class Game_Main {
	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {

            GameManager m = new GameManager();
            m.loop();
        });
	}
}