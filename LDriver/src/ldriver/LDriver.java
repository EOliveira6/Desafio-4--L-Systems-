/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldriver;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Eleni Oliveira
 */
public class LDriver {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here

        try {
            File file = new File("src\\teste.txt");

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {

            System.out.println("Error");
        }

        LSystem sys = new LSystem();
        Random rand = new Random();
        sys.getInput();
     // desenhar (x, y, direção, largura, cor
        for (int i = 0; i < 7; ++i) {
            int x = rand.nextInt(500) - 250;
            int y = rand.nextInt(100) - 50;
            sys.draw(x, y, 90, 2, new Color(1, 150, 150));
        }
    }
}
