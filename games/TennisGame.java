package games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TennisGame extends JPanel implements KeyListener, ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 20;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;
    private static final int PLAYER_SPEED = 8;
    private static final int MAX_SCORE = 5;

    // Game objects
    private Rectangle playerPaddle;
    private Rectangle aiPaddle;
    private Rectangle ball;
    private Point ballVelocity;

    // Game state
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private boolean playerWon = false;
    private int playerScore = 0;
    private int aiScore = 0;
    private int level = 1;
    private int aiDifficulty = 5; // Lower is harder

    // UI components
    private JButton startButton;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private Timer gameTimer;
    private List<Rectangle> obstacles = new ArrayList<>();

    public TennisGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setLayout(null);
        addKeyListener(this);
        setFocusable(true);

        // Initialize game objects
        playerPaddle = new Rectangle(50, HEIGHT/2 - PADDLE_HEIGHT/2, PADDLE_WIDTH, PADDLE_HEIGHT);
        aiPaddle = new Rectangle(WIDTH - 70, HEIGHT/2 - PADDLE_HEIGHT/2, PADDLE_WIDTH, PADDLE_HEIGHT);
        ball = new Rectangle(WIDTH/2 - BALL_SIZE/2, HEIGHT/2 - BALL_SIZE/2, BALL_SIZE, BALL_SIZE);
        resetBall();

        // Initialize UI
        scoreLabel = new JLabel("Player: 0 - AI: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBounds(WIDTH/2 - 100, 10, 200, 30);
        add(scoreLabel);

        levelLabel = new JLabel("Level: 1");
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 20));
        levelLabel.setBounds(10, 10, 100, 30);
        add(levelLabel);

        startButton = new JButton("New Game");
        startButton.setBounds(WIDTH/2 - 100, HEIGHT/2 + 50, 200, 50);
        startButton.addActionListener(e -> startGame());
        startButton.setVisible(true);
        add(startButton);

        gameTimer = new Timer(16, this); // ~60 FPS
    }

    private void resetBall() {
        ball.x = WIDTH/2 - BALL_SIZE/2;
        ball.y = HEIGHT/2 - BALL_SIZE/2;

        Random rand = new Random();
        int speed = 3 + level;
        ballVelocity = new Point(
            rand.nextBoolean() ? speed : -speed,
            rand.nextBoolean() ? speed : -speed
        );
    }

    private void createObstacles() {
        obstacles.clear();

        switch(level) {
            case 2:
                // Add a center line
                for (int y = 0; y < HEIGHT; y += 40) {
                    obstacles.add(new Rectangle(WIDTH/2 - 5, y, 10, 20));
                }
                break;
            case 3:
                // Add a rectangle in center
                int size = 100;
                obstacles.add(new Rectangle(WIDTH/2 - size/2, HEIGHT/2 - size/2, size, size));
                break;
        }
    }

    private void startGame() {
        gameStarted = true;
        gameOver = false;
        playerScore = 0;
        aiScore = 0;
        level = 1;
        aiDifficulty = 5;

        playerPaddle.y = HEIGHT/2 - PADDLE_HEIGHT/2;
        aiPaddle.y = HEIGHT/2 - PADDLE_HEIGHT/2;

        scoreLabel.setText("Player: 0 - AI: 0");
        levelLabel.setText("Level: " + level);
        startButton.setVisible(false);

        createObstacles();
        resetBall();
        gameTimer.start();
        requestFocus();
    }

    private void checkCollisions() {
        // Ball with walls
        if (ball.y <= 0 || ball.y >= HEIGHT - BALL_SIZE) {
            ballVelocity.y *= -1;
        }

        // Ball with player paddle
        if (ball.intersects(playerPaddle)) {
            ballVelocity.x = Math.abs(ballVelocity.x);
            // Add some spin based on paddle movement
            if (ballVelocity.y > 0) ballVelocity.y += 1;
            else ballVelocity.y -= 1;
        }

        // Ball with AI paddle
        if (ball.intersects(aiPaddle)) {
            ballVelocity.x = -Math.abs(ballVelocity.x);
        }

        // Ball with obstacles
        for (Rectangle obstacle : obstacles) {
            if (ball.intersects(obstacle)) {
                // Simple bounce - could be improved
                ballVelocity.x *= -1;
                ballVelocity.y *= -1;
                break;
            }
        }

        // Scoring
        if (ball.x <= 0) {
            aiScore++;
            checkGameOver();
        } else if (ball.x >= WIDTH - BALL_SIZE) {
            playerScore++;
            checkGameOver();
        }
    }

    private void checkGameOver() {
        if (playerScore >= MAX_SCORE) {
            gameOver = true;
            playerWon = true;
            if (level < 3) {
                level++;
                levelLabel.setText("Level: " + level);
                aiDifficulty = Math.max(2, aiDifficulty - 1); // AI gets harder
                createObstacles();
                playerScore = 0;
                aiScore = 0;
                scoreLabel.setText("Player: 0 - AI: 0");
                resetBall();
                gameOver = false;
            }
        } else if (aiScore >= MAX_SCORE) {
            gameOver = true;
            playerWon = false;
        }

        if (gameOver) {
            gameTimer.stop();
            startButton.setVisible(true);
        } else {
            resetBall();
        }
    }

    private void moveAI() {
        // Simple AI that follows the ball with some delay based on difficulty
        if (aiPaddle.y + PADDLE_HEIGHT/2 < ball.y &&
            new Random().nextInt(aiDifficulty) != 0) {
            aiPaddle.y += PLAYER_SPEED - 1;
        } else if (aiPaddle.y + PADDLE_HEIGHT/2 > ball.y &&
                  new Random().nextInt(aiDifficulty) != 0) {
            aiPaddle.y -= PLAYER_SPEED - 1;
        }

        // Keep AI paddle in bounds
        if (aiPaddle.y < 0) aiPaddle.y = 0;
        if (aiPaddle.y > HEIGHT - PADDLE_HEIGHT) aiPaddle.y = HEIGHT - PADDLE_HEIGHT;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameStarted) {
            // Draw title screen
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("TENNIS GAME", WIDTH/2 - 180, HEIGHT/2 - 50);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Press Start or Space to begin", WIDTH/2 - 180, HEIGHT/2 + 20);
            return;
        }

        // Draw paddles
        g.setColor(Color.WHITE);
        g.fillRect(playerPaddle.x, playerPaddle.y, playerPaddle.width, playerPaddle.height);
        g.fillRect(aiPaddle.x, aiPaddle.y, aiPaddle.width, aiPaddle.height);

        // Draw ball
        g.fillOval(ball.x, ball.y, ball.width, ball.height);

        // Draw obstacles
        g.setColor(Color.GRAY);
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        // Draw center line
        g.setColor(new Color(50, 50, 50));
        g.drawLine(WIDTH/2, 0, WIDTH/2, HEIGHT);

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            String message = playerWon ? (level > 3 ? "YOU WON!" : "LEVEL UP!") : "GAME OVER";
            g.drawString(message, WIDTH/2 - 150, HEIGHT/2 - 50);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Final Score: " + playerScore + " - " + aiScore, WIDTH/2 - 100, HEIGHT/2 + 20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && gameStarted) {
            // Move ball
            ball.x += ballVelocity.x;
            ball.y += ballVelocity.y;

            // AI movement
            moveAI();

            // Check collisions
            checkCollisions();

            // Update score display
            scoreLabel.setText("Player: " + playerScore + " - AI: " + aiScore);
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameStarted && e.getKeyCode() == KeyEvent.VK_SPACE) {
            startGame();
            return;
        }

        if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            startGame();
            return;
        }

        // Player controls
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            playerPaddle.y -= PLAYER_SPEED;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            playerPaddle.y += PLAYER_SPEED;
        }

        // Keep paddle in bounds
        if (playerPaddle.y < 0) playerPaddle.y = 0;
        if (playerPaddle.y > HEIGHT - PADDLE_HEIGHT) playerPaddle.y = HEIGHT - PADDLE_HEIGHT;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tennis Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new TennisGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}