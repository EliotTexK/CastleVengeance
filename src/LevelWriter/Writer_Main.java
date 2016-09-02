package LevelWriter;

import java.awt.*;
import java.util.Scanner;

import Shared.ReadLevel;

public class Writer_Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Scanner input = new Scanner(System.in);
            int w = 0;
            int h = 0;
            String s = null;
            while (true) {
                System.out.println("If you would like to load a level,");
                System.out.println("Type \' load \' ");
                System.out.println("If you would like to create a new level,");
                System.out.println("Type \' new \' ");

                if (input.hasNext("new")) {
                    while (w < 96 || h < 96 || w > 50000 || h > 50000) {
                        input.nextLine();

                        System.out.print("Level Width: ");
                        if (input.hasNextInt()) {
                            w = input.nextInt();
                        }

                        System.out.print("Level Height: ");
                        if (input.hasNextInt()) {
                            h = input.nextInt();
                        }
                        if (w < 96 || h < 96) {
                            System.out.println("level too small");
                        }
                        if (w > 50000 || h > 50000) {
                            System.out.println("level too big");
                        }
                    }

                    System.out.println();

                    java.awt.Window wind = new Window(w, h);
                }
                else if (input.hasNext("load")) {
                    System.out.println("specify the level's filepath");
                    input.nextLine();
                    if (input.hasNext()) {
                        String fileName = input.next();
                        ReadLevel r = new ReadLevel();
                        r.read(fileName);

                        java.awt.Window load = new Window(r.getTiles(), r.getSprites(), r.getPlayerStartX(), r.getPlayerStartY());
                    }
                }
                break;
            }
            input.close();
        });
    }
}
