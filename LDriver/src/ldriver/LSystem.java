/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldriver;

import gpdraw.*;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Eleni Oliveira
 */
public class LSystem {

    String vars, consts = "+-[]'\"~`*";
    RuleObj[] rules;
    double angle, length;
    String start;
    int level;
    boolean varsDraw = true;

    SketchPad canvas;
    DrawingTool pen;

    //Variáveis, constantes, padrão inicial, regras, ângulo de giro, comprimento de avanço, nível de recursão
    public void getInput() throws FileNotFoundException {

        System.out.println("---Bem-vindo ao L-System!Eleni---082180021");
        vars = "F G";
        consts = "+ -";
        start = "F";

        //Var1=rule1, var2=rule2
        //Regras: regras separadas por vírgulas para cada variável. As regras começam com uma variável seguida por '=' e uma string de variáveis ​​e constantes
//Exemplo: B = BB, A = B [-A] + A
        //  System.out.print("Insira as regras (ou seja, X = X + Y): ");
        // rules = parseRuleInput(scanner.nextLine());
        try {
            File file = new File("src\\rules.txt");
            Scanner sc = new Scanner(file);
            rules = parseRuleInput(sc.nextLine());
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }

        angle = 90;
        length = 15;
        level = 4;

        canvas = new SketchPad(500, 500);
        pen = new DrawingTool(canvas);

    }

    public void draw(int x, int y, double dir, int width, Color color) {
        initPen(x, y, dir, width, color);
        drawStr(start, level);
    }

    public void initPen(int x, int y, double dir, int width, Color color) {
        pen.up();
        pen.move(x, y);
        pen.setDirection(dir);
        pen.down();

        //Definir cor e espessura
        pen.setColor(color);
        pen.setWidth(width);
    }

    // * Desenhe o sistema com base na regra inicial e nível de recursão * /
    public void drawStr(String str, int level) {
        ArrayDeque<Double> stack = new ArrayDeque<Double>();

        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);

            if (consts.indexOf(c) != -1) {
                if (c == '[') {
                    stack.push(length);
                    stack.push(pen.getXPos());
                    stack.push(pen.getYPos());
                    stack.push(pen.getDirection());
                    stack.push((double) pen.getWidth());
                } else if (c == ']') {
                    double width = stack.pop();
                    double dir = stack.pop();
                    double y = stack.pop();
                    double x = stack.pop();
                    length = stack.pop();
                    pen.up();
                    pen.move(x, y);
                    pen.setDirection(dir);
                    pen.setWidth((int) width);
                    pen.down();
                } else {
                    doConstAction(c);
                }
            } else if (vars.indexOf(c) != -1) {
                if (level == 0) {
                    doVarAction();
                } else {
                    String rule = getRule(c);
                    drawStr(rule, level - 1);
                }
            } else {
                System.out.println("Caractere desconhecido em  drawStr: " + c);
                System.exit(0);
            }
        }
    }

    private void doConstAction(char cons) {
        switch (cons) {
            //Movement
            case '+':
                pen.turnRight(angle);
                break;
            case '-':
                pen.turnLeft(angle);
                break;
            case 'F':
                pen.forward(length);
                changePenColor();
                break;
            case 'M':
                pen.up();
                pen.forward(length);
                pen.down();
                break;

            //Pen
            case '*':
                changePenColor();
                break;
            case '"':
                pen.setWidth(Math.min(100, pen.getWidth() + 1));
                break;
            case '\'':
                pen.setWidth(Math.max(1, pen.getWidth() - 1));
                break;

            //Length
            case '~':
                length = length * 2;
                break;
            case '`':
                length = length / 2;
                break;

            default:
                System.out.println("Caractere desconhecido em  doConstAction: " + cons);
                System.exit(0);
        }
    }

    private void doVarAction() {
        if (varsDraw) {
            pen.forward(length);
            changePenColor();
        }

    }

    // * Dada uma variável, retorna sua regra correspondente * /
    private String getRule(char var) {
        int varIndex = vars.indexOf(var);
        if (varIndex == -1) {
            System.out.println("Caractere desconhecido em getRule: " + var);
            System.exit(0);
            return null;
        } else {
            return rules[varIndex].getRule();
        }
    }

    private String parseCharInput(String input, boolean checkAlpha) {
        input = input.trim();

        for (int i = 0; i < input.length(); ++i) {
            if (i % 2 == 0 && input.charAt(i) == ' ') {
                System.out.println("Cada variável ou constante pode ser apenas um caractere");
                System.exit(0);
            } else if (i % 2 != 0 && input.charAt(i) != ' ') {
                System.out.println("Cada variável ou constante pode ser apenas um caractere");
                System.exit(0);
            }
        }
        String result = input.replaceAll(" ", "");

        if (checkAlpha && result.matches(".*[A-Za-z].*")) {
            varsDraw = false;
        }

        return result;
    }

    private RuleObj[] parseRuleInput(String input) {
        RuleObj[] result = new RuleObj[vars.length()];
        String[] splitInput = input.split(", *"); //ex ["X=X+X-X","Y=X+Y"]

        for (int i = 0; i < splitInput.length; ++i) {
            String rule = splitInput[i];

             // Verifique a formatação
            rule = rule.replace(" ", "");
            if (!rule.matches("[A-Za-z]=.*")) {
                System.out.println("Regra mal formatada: " + rule + "\nExample: 'Y=X+Y-Y'");
                System.exit(0);
            }

            //Parse rule
            char var = rule.charAt(0);
            int varIndex = vars.indexOf(var);
            if (varIndex == -1) {
                System.out.println("Regra incomparável para var " + var);
                System.exit(0);
            } else {
                if (result[varIndex] == null) {
                    result[varIndex] = new RuleObj();
                }
                result[varIndex].addRule(rule.substring(2));
            }
        }
        return result;
    }

    //  método para mudar a cor da caneta
    private void changePenColor() {
        int r = (pen.getColor().getRed());
        int g = (pen.getColor().getGreen());
        int b = (pen.getColor().getBlue());
        int delta;

        if (r == 1) {
            delta = 1;
        } else {
            delta = -1;
        }

        //Oscilar através das cores, passo por 1 ou -1
        if (b > 254) {
            delta = -1;
            r = 0;
        } else if (b < 50) {
            delta = 1;
            r = 1;
        }

        b += delta;
        pen.setColor(new Color(r, b, b));
    }
}
