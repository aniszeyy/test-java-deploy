package games;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SudokuGame extends JFrame {

    private static final int ROWS = 5;
    private static final int COLS = 5;
    private JTextField[][] grid;
    private Random random;
    private JPanel panel;

    public SudokuGame() {
        super("Sudoku 5x5");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialisation du panel
        panel = new JPanel(new GridLayout(ROWS, COLS));
        add(panel, BorderLayout.CENTER);

        createGrid();
        createMenuBar();
        setLocationRelativeTo(null); // Centrer la fenêtre
    }

    private void createGrid() {
        grid = new JTextField[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 18));
                grid[i][j] = cell;
                panel.add(cell); // ✅ Ajout dans le panel
                addTextChangeListener(cell, i, j);
            }
        }
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> restartGame());
        fileMenu.add(newGameItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sudoku Game - v1.0"));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
    }

    private void restartGame() {
        random = new Random();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int number = random.nextInt(25) + 1;
                grid[i][j].setText(String.valueOf(number));
                grid[i][j].setEditable(false); // Les cellules sont non modifiables
            }
        }
    }

    private void addTextChangeListener(JTextField field, int row, int col) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkValidInput(row, col, field.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Rien à faire
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Rien à faire
            }
        });
    }

    private void checkValidInput(int row, int col, String input) {
        if (input.isEmpty()) return;

        try {
            int number = Integer.parseInt(input);
            if (number < 1 || number > 25) {
                showError("Entrez un nombre entre 1 et 25");
                grid[row][col].setText("");
            } else if (!isValidInput(row, col, number)) {
                showError("Ce nombre existe déjà dans la ligne ou la colonne !");
                grid[row][col].setText("");
            }
        } catch (NumberFormatException ex) {
            showError("Veuillez entrer un nombre valide");
            grid[row][col].setText("");
        }
    }

    private boolean isValidInput(int row, int col, int number) {
        // Vérifie la ligne
        for (int j = 0; j < COLS; j++) {
            if (j != col && grid[row][j].getText().equals(String.valueOf(number))) {
                return false;
            }
        }

        // Vérifie la colonne
        for (int i = 0; i < ROWS; i++) {
            if (i != row && grid[i][col].getText().equals(String.valueOf(number))) {
                return false;
            }
        }

        return true; // Si tout va bien
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuGame().setVisible(true));
    }
}