package games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends JPanel implements KeyListener, ActionListener {
    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int GRID_WIDTH = WIDTH / TILE_SIZE;
    private static final int GRID_HEIGHT = HEIGHT / TILE_SIZE;

    private int[] snakeX = new int[500];
    private int[] snakeY = new int[500];
    private int snakeLength = 3;
    private int foodX, foodY;
    private char direction = 'R'; // U, D, L, R
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private Timer timer;
    private int score = 0;
    private int level = 1;
    private int speed = 150;
    private List<Point> obstacles = new ArrayList<>();
    private JButton restartButton;
    private JLabel scoreLabel, levelLabel;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        setLayout(null);

        // Initialize UI components
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(10, 10, 100, 20);
        add(scoreLabel);

        levelLabel = new JLabel("Level: 1");
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setBounds(120, 10, 100, 20);
        add(levelLabel);

        restartButton = new JButton("New Game");
        restartButton.setBounds(WIDTH/2 - 80, HEIGHT/2 + 50, 160, 30);
        restartButton.addActionListener(e -> resetGame());
        restartButton.setVisible(false);
        add(restartButton);

        initGame();
    }

    private void initGame() {
        // Initialize snake position
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = 10 - i;
            snakeY[i] = 10;
        }

        spawnFood();
        createObstacles();

        // Game loop
        timer = new Timer(speed, this);
    }

    private void createObstacles() {
        obstacles.clear();
        Random rand = new Random();

        // Level-specific obstacles
        if (level == 2) {
            // Horizontal walls
            for (int i = 5; i < GRID_WIDTH - 5; i++) {
                obstacles.add(new Point(i, 5));
                obstacles.add(new Point(i, GRID_HEIGHT - 6));
            }
        } else if (level == 3) {
            // Cross pattern
            for (int i = 5; i < GRID_HEIGHT - 5; i++) {
                obstacles.add(new Point(GRID_WIDTH/2, i));
                obstacles.add(new Point(i, GRID_HEIGHT/2));
            }
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        boolean onSnakeOrObstacle;

        do {
            onSnakeOrObstacle = false;
            foodX = rand.nextInt(GRID_WIDTH);
            foodY = rand.nextInt(GRID_HEIGHT);

            // Check if food spawns on snake
            for (int i = 0; i < snakeLength; i++) {
                if (snakeX[i] == foodX && snakeY[i] == foodY) {
                    onSnakeOrObstacle = true;
                    break;
                }
            }

            // Check if food spawns on obstacle
            for (Point p : obstacles) {
                if (p.x == foodX && p.y == foodY) {
                    onSnakeOrObstacle = true;
                    break;
                }
            }
        } while (onSnakeOrObstacle);
    }

    private void move() {
        // Move body
        for (int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i-1];
            snakeY[i] = snakeY[i-1];
        }

        // Move head based on direction
        switch(direction) {
            case 'U': snakeY[0]--; break;
            case 'D': snakeY[0]++; break;
            case 'L': snakeX[0]--; break;
            case 'R': snakeX[0]++; break;
        }
    }

    private void checkCollision() {
        // Check wall collision
        if (snakeX[0] < 0 || snakeX[0] >= GRID_WIDTH ||
            snakeY[0] < 0 || snakeY[0] >= GRID_HEIGHT) {
            gameOver = true;
            return;
        }

        // Check self collision
        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                gameOver = true;
                return;
            }
        }

        // Check obstacle collision
        for (Point p : obstacles) {
            if (snakeX[0] == p.x && snakeY[0] == p.y) {
                gameOver = true;
                return;
            }
        }
    }

    private void checkFood() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            score += 10 * level;
            scoreLabel.setText("Score: " + score);

            // Level progression
            if (score >= level * 50 && level < 3) {
                level++;
                levelLabel.setText("Level: " + level);
                speed -= 30; // Increase speed
                timer.setDelay(speed);
                createObstacles();
            }

            spawnFood();
        }
    }

    private void resetGame() {
        gameOver = false;
        gameStarted = true;
        score = 0;
        level = 1;
        speed = 150;
        snakeLength = 3;
        direction = 'R';
        scoreLabel.setText("Score: 0");
        levelLabel.setText("Level: 1");
        restartButton.setVisible(false);

        initGame();
        timer.start();
        requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameStarted) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("SNAKE GAME", WIDTH/2 - 150, HEIGHT/2 - 50);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press any key to start", WIDTH/2 - 100, HEIGHT/2 + 20);
            return;
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", WIDTH/2 - 120, HEIGHT/2 - 50);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, WIDTH/2 - 50, HEIGHT/2);
            restartButton.setVisible(true);
            return;
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillOval(foodX * TILE_SIZE, foodY * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw obstacles
        g.setColor(Color.GRAY);
        for (Point p : obstacles) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw snake
        for (int i = 0; i < snakeLength; i++) {
            if (i == 0) {
                g.setColor(new Color(0, 150, 0)); // Darker green for head
            } else {
                g.setColor(Color.GREEN);
            }
            g.fillRect(snakeX[i] * TILE_SIZE, snakeY[i] * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            // Draw eyes on head
            if (i == 0) {
                g.setColor(Color.BLACK);
                g.fillRect(snakeX[i] * TILE_SIZE + 5, snakeY[i] * TILE_SIZE + 5, 5, 5);
                g.fillRect(snakeX[i] * TILE_SIZE + 10, snakeY[i] * TILE_SIZE + 5, 5, 5);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && gameStarted) {
            move();
            checkCollision();
            checkFood();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameStarted) {
            gameStarted = true;
            timer.start();
            repaint();
            return;
        }

        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}