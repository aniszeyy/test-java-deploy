package games;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class menu_game {
    public static void main(String[] args) {
        // Game 1: Maze Game
        new MazeGameGUI().setVisible(true);

        // Wait for user to close maze window
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Game 2: Snake Game
        new SnakeGame().setVisible(true);

        // Wait for user to close snake window
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Game 3: Tennis Game
        new TennisGame().setVisible(true);
    }
}