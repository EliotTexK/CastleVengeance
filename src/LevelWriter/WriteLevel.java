package LevelWriter;

import Shared.Sprite;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class WriteLevel {
    public static void write (String filename, byte[][]tiles, Sprite[] sprites,
                              int playerStartX, int playerStartY) throws IOException {
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));

        System.out.println();
        System.out.println("exporting level...");

        outputWriter.write(tiles.length+"");
        outputWriter.newLine();
        outputWriter.write(tiles[0].length+"");
        outputWriter.newLine();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                outputWriter.write(tiles[i][j]+"");
                outputWriter.newLine();
            }
        }
        for (int i = 0; i < sprites.length; i++) {
            outputWriter.write(sprites[i].getImage()+"");
            outputWriter.write(sprites[i].getX()+"");
            outputWriter.write(sprites[i].getY()+"");
            outputWriter.newLine();
        }
        outputWriter.newLine();
        outputWriter.write(playerStartX+"");
        outputWriter.newLine();
        outputWriter.write(playerStartY+"");

        outputWriter.flush();
        outputWriter.close();

        System.out.println("finished");
        System.out.println();
    }
}
