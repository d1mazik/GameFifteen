package SlideGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Game extends JFrame {

    //skapar button planka
    private final JPanel panel = new JPanel(new GridLayout(4, 4, 1, 1)); //skapar panel,4 rader,4 kolumner och avstånd
    static int[][] numbers = new int[4][4]; //skapar dubbel array motsvarar spelplanen


    public Game() {
        super("Slide fifteen game"); //skapar titel på rutan

        setBounds(200, 200, 300, 300); //metod som sätter storlek på rutan
        setLocationRelativeTo(null); //här placerar vi rutan i mitten av skärmen
        setResizable(false); //storlek kan inte ändras
        setDefaultCloseOperation(EXIT_ON_CLOSE); //så att man kan stänga av rutan

        createMenu(); //här anropar vi metoden createMenu

        Container container = getContentPane();
        panel.setBackground(Color.lightGray);
        container.add(panel);

        generate(); //anropar metod generate
        repaintField(); //anropar repaintField
    }

    public void createMenu() {
        //skapar menuBar
        JMenuBar menu = new JMenuBar();
        //skapar en menu
        JMenu fileMenu = new JMenu("Menu");
        //skapar menu alternativ

        JMenuItem fileNewGame = new JMenuItem("New game");
        //sätter ett namn som vi kan referera till
        fileNewGame.setActionCommand("new game");
        fileNewGame.addActionListener(new NewMenuListener());
        fileMenu.add(fileNewGame);


        JMenuItem fileSolution = new JMenuItem("Solution");
        fileSolution.setActionCommand("solution");
        fileSolution.addActionListener(new NewMenuListener());
        fileMenu.add(fileSolution);
        Game.gameSolution();

        JMenuItem gameInfo = new JMenuItem("Info");
        gameInfo.setActionCommand("info");
        gameInfo.addActionListener(new NewMenuListener());
        fileMenu.add(gameInfo);


        JMenuItem exit = new JMenuItem("Exit");
        exit.setActionCommand("exit");
        exit.addActionListener(new NewMenuListener());
        fileMenu.add(exit);

        //lägger till menu i menubar
        menu.add(fileMenu);
        //lägger till menuBar
        setJMenuBar(menu);
    }

    //inre klass för menu event
    public class NewMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //sätter menu valets namn till en sträng
            String command = e.getActionCommand();
            if ("exit".equals(command)) {
                System.exit(0);
            }
            if ("new game".equals(command)) {
                generate();
                repaintField();
            }
            if ("info".equals(command)) {
                JOptionPane.showMessageDialog(null, "Game creators:\n" +
                        "Dima and Valeria.");
            }
            if ("solution".equals(command)) {
                gameSolution();
                repaintField();
            }
               /*
                if (checkWin()) { //om vunnit, skriv ut meddelande
                    JOptionPane.showMessageDialog(null, "YOU WIN!",
                            "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                    generate();
                    repaintField();
                }
              */
            }
        }


    //metoden genererar siffror från 1 till 15
    public void generate() {
        //lista som representerar ordningen på knapparna
        ArrayList<Integer> sortingList = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
        Collections.shuffle(sortingList);
        if (!canBeSolved(sortingList)) {
            System.out.println("false");
            generate(); //startar om shuffle tills det blir möjligt att klara av spelet
        }
        System.out.println("true");
        int r = 0;
        int c = 0;
        for (int i = 0; i < 16; i++) {
            numbers[r][c] = sortingList.get(i);
            r++;
            if (r == 4) {
                c++;
                r = 0;
            }
        }
    }

    //kollar om den går att lösa
    public static boolean canBeSolved(ArrayList<Integer> sortingList) {
        int sum = 0;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (i < j) {
                    if (sortingList.get(i) > sortingList.get(j)) {
                        sum++;
                    }
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            if (sortingList.get(i) == 0) {
                if (i == 4 || i == 5 || i == 6 || i == 7
                        || i == 12 || i == 13 || i == 14 || i == 15) {
                    if ((sum & 1) == 0) {
                        return false;
                    }
                } else {
                    if ((sum & 1) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void repaintField() { // metod som ordnar knappar
        panel.removeAll();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JButton button = new JButton(Integer.toString(numbers[i][j]));
                button.setFocusable(false);
                panel.add(button);
                button.setBackground(Color.darkGray);
                button.setOpaque(true);
                if (numbers[i][j] == 0) {
                    button.setVisible(false);
                } else
                    button.addActionListener(new ClickListener());
            }
        }
        panel.validate(); //validerar vår container och alla kompomenter
        panel.repaint();
    }

    public boolean checkWin() { //metod kollar om man har vunnit
        boolean status = true;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j > 2)
                    break;
                if (numbers[i][j] != i * 4 + j + 1) {
                    status = false;
                }
            }
        }
        return status;
    }


    public class ClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource(); //sätter värdet på knappobjektet till den knappen som trycks
            button.setVisible(false); //visa inte den nya knappen
            String name = button.getText(); //sätter knappens namn till det hämtade knappens text, funkar som ett id
            change(Integer.parseInt(name)); //knappens namn blir int

        }
    }

    public void change(int num) { //metod som flyttar på knappar
        int i = 0, j = 0;
        for (int k = 0; k < 4; k++) {
            for (int l = 0; l < 4; l++) {
                if (numbers[k][l] == num) {
                    i = k;
                    j = l;
                }
            }
        }
        //här flyttar jag på knappar
        //flytta upp
        if (i > 0) {
            //om knappen ovanför är lika med noll, så är den blank
            if (numbers[i - 1][j] == 0) {
                //sätter knappen ovanför till den tryckta knappen
                numbers[i - 1][j] = num;
                //sätter den tryckta knappen till blank
                numbers[i][j] = 0;
            }
        }
        //flytta ner
        if (i < 3) {
            if (numbers[i + 1][j] == 0) {
                numbers[i + 1][j] = num;
                numbers[i][j] = 0;
            }
        }
        //flytta till vänster
        if (j > 0) {
            if (numbers[i][j - 1] == 0) {
                numbers[i][j - 1] = num;
                numbers[i][j] = 0;
            }
        }
        //flytta till höger
        if (j < 3) {
            if (numbers[i][j + 1] == 0) {
                numbers[i][j + 1] = num;
                numbers[i][j] = 0;
            }
        }
        repaintField();

        if (checkWin()) { //om vunnit, skriv ut meddelande
            JOptionPane.showMessageDialog(null, "YOU WIN!",
                    "Congratulations", JOptionPane.INFORMATION_MESSAGE);
            generate();
            repaintField();
        }
    }

    public static int[][] gameSolution() {
        ArrayList<Integer> sortingList = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0));
        //sätter i rätt ordning från 1 till 15 för Solution button
        if (!canBeSolved(sortingList)) {
            System.out.println("true");
            gameSolution(); //sätter i rätt ordning
        }
        System.out.println("true");
        int r = 0;
        int c = 0;
        for (int j = 0; j < 16; j++) {
            numbers[r][c] = sortingList.get(j);
            c++;
            if (c == 4) {
                r++;
                c = 0;
            }
        }
        return numbers;

    }
}