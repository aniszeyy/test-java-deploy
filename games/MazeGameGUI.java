package games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MazeGameGUI extends JFrame {

    private MazePanel mazePanel;
    private JLabel moveCounterLabel;
    private int moveCounter = 0;

    public MazeGameGUI() {
        super("Jeu de Labyrinthe");

        // === Barre de menu ===
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Jeu");
        JMenuItem newGameItem = new JMenuItem("Nouveau jeu");
        JMenuItem quitItem = new JMenuItem("Quitter");

        newGameItem.addActionListener(e -> restartGame());
        quitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(quitItem);

        JMenu helpMenu = new JMenu("Aide");
        JMenuItem aboutItem = new JMenuItem("À propos");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Jeu de Labyrinthe 1.0\nDéveloppé avec Java Swing.",
                "À propos", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // === Interface principale ===
        mazePanel = new MazePanel();
        moveCounterLabel = new JLabel("Coups : 0");

        JButton resetButton = new JButton("Nouveau jeu");
        resetButton.addActionListener(e -> restartGame());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(resetButton);
        controlPanel.add(moveCounterLabel);

        add(controlPanel, BorderLayout.NORTH);
        add(mazePanel, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void restartGame() {
        moveCounter = 0;
        moveCounterLabel.setText("Coups : 0");
        mazePanel.generateMaze();
        mazePanel.resetPlayer();
        mazePanel.repaint();
    }

    // === Panneau du labyrinthe ===
    private class MazePanel extends JPanel implements KeyListener {
        private final int SIZE = 10;
        private final int CELL_SIZE = 40;

        private int playerX = 0, playerY = 0;
        private int goalX, goalY;

        private boolean[][] walls = new boolean[SIZE][SIZE];
        private Random rand = new Random();

        public MazePanel() {
            setPreferredSize(new Dimension(SIZE * CELL_SIZE, SIZE * CELL_SIZE));
            setBackground(Color.BLACK);
            addKeyListener(this);
            setFocusable(true);
            generateMaze();
        }

        public void generateMaze() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    walls[i][j] = rand.nextInt(100) < 20;
                }
            }
            walls[0][0] = false;
            do {
                goalX = rand.nextInt(SIZE);
                goalY = rand.nextInt(SIZE);
            } while (walls[goalY][goalX] || (goalX == 0 && goalY == 0));
        }

        public void resetPlayer() {
            playerX = 0;
            playerY = 0;
            requestFocusInWindow();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (walls[row][col]) {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }

            // Objectif
            g.setColor(Color.GREEN);
            g.fillRect(goalX * CELL_SIZE + 10, goalY * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20);

            // Joueur
            g.setColor(Color.RED);
            g.fillOval(playerX * CELL_SIZE + 5, playerY * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }

        private boolean canMove(int x, int y) {
            return x >= 0 && x < SIZE && y >= 0 && y < SIZE && !walls[y][x];
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int newX = playerX;
            int newY = playerY;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> newY--;
                case KeyEvent.VK_DOWN -> newY++;
                case KeyEvent.VK_LEFT -> newX--;
                case KeyEvent.VK_RIGHT -> newX++;
            }

            if (canMove(newX, newY)) {
                playerX = newX;
                playerY = newY;
                moveCounter++;
                moveCounterLabel.setText("Coups : " + moveCounter);
                repaint();
                checkWin();
            }
        }

        private void checkWin() {
            if (playerX == goalX && playerY == goalY) {
                JOptionPane.showMessageDialog(this, "🎉 Bravo, vous avez gagné en " + moveCounter + " coups !");
                generateMaze();
                resetPlayer();
                moveCounter = 0;
                moveCounterLabel.setText("Coups : 0");
                repaint();
            }
        }

        @Override public void keyReleased(KeyEvent e) {}
        @Override public void keyTyped(KeyEvent e) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeGameGUI::new);
    }
}