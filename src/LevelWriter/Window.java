package LevelWriter;

import Shared.Sprite;
import Shared.TileSet;
import Shared.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

class Window extends JFrame implements MouseListener, ActionListener{
    private DrawScreen screen;
    private TileSet tileSet;
    private Sprite[] sprites;
    private int playerStartX, playerStartY,scrolledX,scrolledY;
    private boolean down;
    private long numOfTiles;
    private JButton exportButton;
    private JButton changeKind;
    private byte kind;
    private JTextField textField;
    private JTextField textField2;
    private JFrame frame2;
    private boolean textFieldToInput;

    public Window(int width, int height)
    {
        //constructs a new blank room
        sprites = new Sprite[]{};
        tileSet = new TileSet(width/32, height/32);
        this.playerStartX = 0;
        this.playerStartY = 0;
        init();

    }

    public Window(byte[][] tiles, Sprite[] sprites, int playerStartX, int playerStartY)
    {
        //constructs a room from a file
        this.sprites = sprites;
        this.tileSet = new TileSet(tiles.length,tiles[0].length);
        this.tileSet.setTiles(tiles);
        this.playerStartX = playerStartX;
        this.playerStartY = playerStartY;
        init();
    }

    private void init()
    {
        kind = 0;
        numOfTiles = new File("resources/images/tiles").listFiles().length;
        System.out.println("number of tile types: " + numOfTiles);

        setBounds(0,0,Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT+32);
        setAlwaysOnTop(true);
        setTitle("LevelManager Editor");
        addMouseListener(this);
        setVisible(true);

        frame2 = new JFrame();
        frame2.setTitle("tools");

        exportButton = new JButton("Export");
        exportButton.setBounds(32,0,96,32);
        exportButton.addActionListener(this);
        frame2.add(exportButton);

        textFieldToInput = false;
        textField = new JTextField();
        textField.addActionListener(this);
        textField.setPreferredSize(new Dimension(96,32));
        textField.setText("PlayerStartY:");
        frame2.add(textField);

        textField2 = new JTextField();
        textField2.addActionListener(this);
        textField2.setPreferredSize(new Dimension(96,32));
        textField2.setText("File Name");
        frame2.add(textField2);

        changeKind = new JButton();
        changeKind.addActionListener(this);
        changeKind.setBounds(0,0,32,32);
        changeKind.setIcon(new ImageIcon());
        this.add(changeKind);

        screen = new DrawScreen();
        add(screen);

        frame2.setAlwaysOnTop(true);
        frame2.setSize(256,128);
        frame2.setLayout(new FlowLayout());
        frame2.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //unused
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            if (kind >= 0) {
                tileSet.makeTile((e.getX() + scrolledX) / 32, (e.getY() + scrolledY) / 32 - 1, kind);
            }
            else if (kind == 0) {
                tileSet.makeTile((e.getX() + scrolledX) / 32, (e.getY() + scrolledY) / 32 - 1, 0);
            }
            screen.repaint();
        }
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            down = true;
            scroll();
        }
        else if (e.getButton() == MouseEvent.BUTTON2)
        {
            kind += 1;
            if (kind >= numOfTiles)  {
                kind = 0;
            }
            screen.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            down = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }

    private void scroll()
    {
        //scrolls the screen
        new Thread(() -> {

            while (down) {
                scrolledX += ((int) MouseInfo.getPointerInfo().getLocation().getX()-Constants.SCREEN_WIDTH/2)/25;
                scrolledY +=((int) MouseInfo.getPointerInfo().getLocation().getY()-Constants.SCREEN_HEIGHT/2)/25;

                screen.repaint();

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exportButton)
        {
            WriteLevel w = new WriteLevel();
            try {
                w.write(textField2.getText(),tileSet.getTiles(),sprites,playerStartX,playerStartY);
            } catch (IOException e1) {
                System.out.println("error");
            }
        }
        if (e.getSource() == textField)
        {
            try {
                if (textFieldToInput == true){
                    int txtin = Integer.parseInt(textField.getText());
                    playerStartY = txtin;
                    textField.setText("PlayerStartY:");
                    textFieldToInput = false;
                }
                else if (textFieldToInput == false){
                    int txtin = Integer.parseInt(textField.getText());
                    playerStartX = txtin;
                    textField.setText("PlayerStartX:");
                    textFieldToInput = true;
                }
            }
            catch (NumberFormatException n)
            {
                textField.setText("Invalid!");
            }
        }
        if (e.getSource() == changeKind)
        {
            kind += 1;
            if (kind >= numOfTiles)  {
                kind = 0;
            }
            screen.repaint();
        }
    }

    private class DrawScreen extends JPanel {
        private Image[] tileImages;
        private Image[] spriteImages;
        private Image background;

        DrawScreen() {
            //customizes the screen
            setBackground(Color.CYAN);
            setDoubleBuffered(true);
            setBounds(0,0,Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT);

            //loads all the resources
            String[] s = new File("resources/images/tiles").list();
            tileImages = new Image[s.length];
            for (int i = 1; i < tileImages.length; i++) {
                tileImages[i] = new ImageIcon("resources/images/tiles/" + s[i]).getImage();
            }

            s = new File("resources/images/sprites").list();
            spriteImages = new Image[s.length];
            for (int i = 0; i < spriteImages.length; i++) {
                spriteImages[i] = new ImageIcon("resources/images/sprites/" + s[i]).getImage();
            }

            background = new ImageIcon("resources/images/backgrounds/background1.png").getImage();

        }

        public void paintComponent(Graphics g) {

            //gets graphics drawer
            Graphics2D g2d = (Graphics2D) g;

            //draws grid
            g2d.drawImage(background, 0, -500, this);

            //draws grid
            for (int i = 0; i < tileSet.getTiles().length; i++) {
                for (int j = 0; j < tileSet.getTiles()[1].length; j++) {
                    g2d.drawRect(i * Constants.BLOCK_WIDTH-scrolledX, j * Constants.BLOCK_HEIGHT-scrolledY, Constants.BLOCK_WIDTH, Constants.BLOCK_HEIGHT);
                }
            }

            //draws tiles
            for (int i = 0; i < tileSet.getTiles().length; i++) {
                for (int j = 0; j < tileSet.getTiles()[1].length; j++) {
                    if ((tileSet.getTiles()[i][j]) != 0) {
                        g2d.drawImage(tileImages[tileSet.getTiles()[i][j]], i * Constants.BLOCK_WIDTH - scrolledX,
                                j * Constants.BLOCK_HEIGHT - scrolledY, this);
                    }
                }
            }

            //draws sprites
            for (int i = 0; i < sprites.length; i++) {
                if (!sprites[i].isFlip())   {
                    g2d.drawImage(tileImages[sprites[i].getImage()], sprites[i].getX() - scrolledX, sprites[i].getY() - scrolledY, this);
                }
                if (sprites[i].isFlip())    {
                    g2d.drawImage(tileImages[sprites[i].getImage()], sprites[i].getY() - scrolledX+32, sprites[i].getY() - scrolledY, -32, 48,this);
                }
            }

            //draws player
            g2d.drawImage(spriteImages[1],playerStartX-scrolledX,playerStartY-scrolledY,this);

            //draws kind of tile
            if (kind != 0) {
                g2d.drawImage(tileImages[kind], 0, 0, this);
            }
        }
    }
}
