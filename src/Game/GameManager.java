package Game;

import Shared.Constants;
import Shared.Sprite;
import Shared.TileSet;
import Shared.ReadLevel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

class GameManager
{
    private LevelManager level;
    private GameDraw screen;
    private SoundPlayer sound;
    private MusicPlayer music;
    private final int TARGET_FPS = 60;
    private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

    GameManager()
    {
        //adds a screen to draw the game and detect
        //keyboard events
        screen = new GameDraw();
        level = new LevelManager("resources/levels/TestLevel",(byte) 0);
        sound = new SoundPlayer();
        music = new MusicPlayer();
        music.play("resources/sounds/Music1.wav");
    }

    void loop()
    {
            new Thread(() -> {

                long lastLoopTime = System.nanoTime();

                while (true) {
                    long now = System.nanoTime();
                    long updateLength = now - lastLoopTime;
                    lastLoopTime = now;
                    double delta = updateLength / ((double) OPTIMAL_TIME);

                    //updates the game
                    updateGame(delta);

                    //stops the game if the window is closed
                    if (screen.isActive()) {

                        if (!(0 > ((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000))) {
                            try {
                                Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
            }).start();
    }

    private void updateGame(double delta)
    {
        level.update(screen.assembleInput(),delta);
        screen.render();
    }

    private class LevelManager
    {
        private PlayableCharacter player;
        private Camera camera;
        private TileSet tileSet;
        private int scrolledX,scrolledY,playerStartX,playerStartY;
        private byte music, lives;


        public LevelManager(int width, int height, byte music)
        {
            //creates tileset
            tileSet = new TileSet(width,height);

            //creates the player
            player = new PlayableCharacter(new byte[]{5,6,7},playerStartX,playerStartY);
            lives = Constants.DEFAULT_LIVES;

            //and a camera to follow it
            camera = new Camera(player);

            //makes some tiles
            tileSet = new TileSet(40,40);
            tileSet.makePlatform(6,8,20,3);
            tileSet.makePlatform(0,12,20,2);
            tileSet.makePlatform(0,15,40,2);
            tileSet.makeWall(16,10,40,2);

            //the music that should be used in the level
            this.music = music;
        }

        public LevelManager(String fileName, byte music)
        {
            ReadLevel readLevel = new ReadLevel();
            readLevel.read(fileName);

            //creates tileset
            tileSet = new TileSet(readLevel.getTiles().length,readLevel.getTiles()[0].length);

            //creates the player

            player = new PlayableCharacter(new byte[]{3,2,1},readLevel.getPlayerStartX(),readLevel.getPlayerStartY());
            lives = Constants.DEFAULT_LIVES;

            //and a camera to follow it
            camera = new Camera(player);

            //makes some tiles
            tileSet.setTiles(readLevel.getTiles());

            //the music that should be used in the level
            this.music = music;
        }

        void update(Input input, double delta) {

            //moves player
            player.getControls(input);
            player.move(delta);

            //ground collision
            int tileX = (int) Math.floor((player.getX() + (Constants.PLAYER_WIDTH/2)) / Constants.BLOCK_WIDTH);
            int tileY = (int) Math.floor(player.getFeet() / Constants.BLOCK_HEIGHT);

            if (tileSet.getTiles()[tileX][tileY] > 0 && player.getVsp() >= 0)   {
                player.setY((tileY * Constants.BLOCK_HEIGHT) - Constants.PLAYER_HEIGHT);
                player.setVsp(0);
                player.setGrounded(true);
            }

            //lava kills player
            if (tileSet.getTiles()[tileX][tileY] == 3)   {
                player.setX(player.getSpawnX());
                player.setY(player.getSpawnY());
                camera.x = playerStartX;
                camera.y = playerStartY;
                lives-=1;
            }

            if (tileSet.getTiles()[tileX][tileY] <= 0 && player.getGrounded()) {
                player.setGrounded(false);
            }

            //cieling collision
            tileX = (int) Math.floor((player.getX() +(Constants.PLAYER_WIDTH/2)) / Constants.BLOCK_WIDTH);
            tileY = (int) Math.floor((player.getY() + Constants.BLOCK_HEIGHT) / Constants.BLOCK_HEIGHT);

            if (tileSet.getTiles()[tileX][tileY] > 0 && tileSet.getTiles()[tileX][tileY] != 2 && player.getVsp() < 0)   {
                player.setY(tileY * Constants.BLOCK_HEIGHT);
                player.setVsp(0);
            }

            //wall collision
            tileX = (int) Math.floor((player.getX() + Constants.PLAYER_WIDTH) / Constants.BLOCK_WIDTH);
            tileY = (int) Math.floor((player.getFeet()-1) / Constants.BLOCK_HEIGHT);

            if (tileSet.getTiles()[tileX][tileY] > 0 && tileSet.getTiles()[tileX][tileY] != 2 && player.getHsp() >= 0)   {
                player.setX((tileX * Constants.BLOCK_WIDTH) - Constants.PLAYER_WIDTH);
            }

            tileX = (int) Math.floor(player.getX() / Constants.BLOCK_WIDTH);
            tileY = (int) Math.floor((player.getFeet()-1) / Constants.BLOCK_HEIGHT);

            if (tileSet.getTiles()[tileX][tileY] > 0 && tileSet.getTiles()[tileX][tileY] != 2 && player.getHsp() <= 0)   {
                player.setX(tileX * Constants.BLOCK_WIDTH + Constants.PLAYER_WIDTH);
            }

            //room border enforcing
            if (player.getX() + player.getHsp() > ((tileSet.getWidth()*2)-2)*Constants.BLOCK_WIDTH) {
                player.setX((tileSet.getWidth()-2)*Constants.BLOCK_HEIGHT);
            }

            if (player.getFeet() + player.getVsp() > (tileSet.getLength()-2)*Constants.BLOCK_HEIGHT) {
                player.setX(player.getSpawnX());
                player.setY(player.getSpawnY());
                camera.x = playerStartX;
                camera.y = playerStartY;
                lives-=1;
            }

            if (player.getX() + player.getHsp() < 0) {
                player.setX(0);
            }

            if (player.getY() + player.getVsp() < 0) {
                player.setY(0);
                player.setVsp(0);
            }

            //updates sprites

            player.updateSprites();

            //updates Game.Camera

            camera.update(delta);

            scrolledX = camera.x - Constants.SCREEN_WIDTH/2;
            scrolledY = camera.y - Constants.SCREEN_HEIGHT/2;
        }

        Sprite[] getSprites() {

            //gets sprite version of all drawable objects
            return new Sprite[]{player};
        }

        byte[][] getTiles() {
            return tileSet.getTiles();
        }

        int getScrolledX() {
            return scrolledX;
        }

        int getScrolledY() {
            return scrolledY;
        }

        public byte getMusic() {
            return music;
        }

        public byte getLives() {
            return lives;
        }
    }

    private class GameDraw extends JFrame implements KeyListener
    {

        private DrawScreen screen;
        private int key1, key2;

        GameDraw() {

            //makes a screen for drawing sprites
            //and tiles
            screen = new DrawScreen();
            add(screen);
            //used to store keyboard events
            addKeyListener(this);
            key1 = 0;
            key2 = 0;
            //customizes the window
            setBounds(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
            setIconImage(new ImageIcon("resources/images/00_Icon.png").getImage());
            setBackground(Color.black);
            setResizable(false);
            setVisible(true);
            //ends the program when the window is closed
            this.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    System.exit(0);
                }
            });
        }

        void render() {
            screen.repaint();
        }

        public void keyPressed(KeyEvent e) {
            if (key1 == 0) {
                key1 = e.getKeyCode();
            }
            else    {
                if (key2 == 0)
                    key2 = e.getKeyCode();
            }
            if (key1 == key2)
            {
                key2 = 0;
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == key1) {
                key1 = 0;
            }
            if (e.getKeyCode() == key2) {
                key2 = 0;
            }
        }

        public void keyTyped(KeyEvent e) {
            //not used
        }

        private boolean getKey(int keyTesting) {
            //returns whether a key has been pressed or not
            if (key1 == keyTesting || key2 == keyTesting)
            {
                return true;
            }
            return false;
        }

        Input assembleInput() {
            //gets controls
            return new Input(getKey(KeyEvent.VK_LEFT), getKey(KeyEvent.VK_RIGHT), getKey(KeyEvent.VK_UP));
        }

        private class DrawScreen extends JPanel {
            private Sprite[] sprites;
            private byte[][] tiles;
            private int scrolledX, scrolledY;
            private Image[] tileImages;
            private Image[] spriteImages;
            private Image background;
            private byte lives;

            DrawScreen() {
                //customizes the screen
                setBackground(Color.CYAN);
                setDoubleBuffered(true);
                setVisible(true);

                //customizes the screen
                setBackground(Color.CYAN);
                setDoubleBuffered(true);
                setBounds(0,0,Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT);

                //loads all the resources
                String[] s = new File("resources/images/tiles").list();
                tileImages = new Image[s.length];
                for (int i = 0; i < tileImages.length; i++) {
                    tileImages[i] = new ImageIcon("resources/images/tiles/" + s[i]).getImage();
                }

                s = new File("resources/images/sprites").list();
                spriteImages = new Image[s.length];
                for (int i = 0; i < spriteImages.length; i++) {
                    spriteImages[i] = new ImageIcon("resources/images/sprites/" + s[i]).getImage();
                }

                background = new ImageIcon("resources/images/backgrounds/background1.png").getImage();

            }

            public void paintComponent(Graphics g)
            {
                this.scrolledX = level.getScrolledX();
                this.scrolledY = level.getScrolledY();
                this.sprites = level.getSprites();
                this.tiles = level.getTiles();
                this.lives = level.getLives();

                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(background,0,-500,this);

                //draws tiles
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles[1].length; j++) {
                        if ((tiles[i][j]) != 0) {
                            g2d.drawImage(tileImages[tiles[i][j]], i * Constants.BLOCK_WIDTH - scrolledX,
                                    j * Constants.BLOCK_HEIGHT - scrolledY,Constants.BLOCK_WIDTH,Constants.BLOCK_HEIGHT, this);
                        }
                    }
                }

                for (int i = 0; i < sprites.length; i++) {
                    Sprite s = sprites[i];
                    if (!s.isFlip())   {
                        g2d.drawImage(spriteImages[s.getImage()],s.getX()-scrolledX,
                                s.getY()-scrolledY,s.getWidth(),s.getHeight(),this);
                    }
                    if (s.isFlip())    {
                        g2d.drawImage(spriteImages[s.getImage()],s.getX()-scrolledX+s.getWidth(),
                                s.getY()-scrolledY,-s.getWidth(),s.getHeight(),this);
                    }
                }

                for (int i = 0; i < lives; i++) {
                    g2d.drawImage(spriteImages[0],i*32,0,this);
                }
            }
        }
    }

    private class MusicPlayer
    {

        SourceDataLine soundLine = null;
        int BUFFER_SIZE = 64 * 1024;  // 64 KB
        boolean startedFlag;

        void play(String fileName) {
            // Set up an audio input stream piped from the sound file.
            new Thread(() -> {
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName));
                    AudioFormat audioFormat = audioInputStream.getFormat();
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                    soundLine = (SourceDataLine) AudioSystem.getLine(info);
                    soundLine.open(audioFormat);
                    soundLine.start();
                    int nBytesRead = 0;
                    byte[] sampledData = new byte[BUFFER_SIZE];
                    while (nBytesRead != -1) {
                        nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
                        if (nBytesRead >= 0) {
                            // Writes audio data to the mixer via this source data line.
                            soundLine.write(sampledData, 0, nBytesRead);
                        }
                        if (!startedFlag) startedFlag = true;
                    }
                } catch (UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                } finally {
                    soundLine.drain();
                    soundLine.close();
                }
                play(fileName);
            }).start();
        }

        boolean isStartedFlag()
        {
            return startedFlag;
        }

    }

    private class SoundPlayer
    {
        void play(String fileName) {
            try {
                // Open an audio input stream.
                URL url = this.getClass().getClassLoader().getResource(fileName);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioIn);
                clip.start();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}