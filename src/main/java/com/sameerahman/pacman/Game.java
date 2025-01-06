package com.sameerahman.pacman;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.*;
import java.util.List;

public class Game {
    private static final char[][] INITIAL_GRID = {
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
            {'W', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'W'},
            {'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W'},
            {'W', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'W'},
            {'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W'},
            {'W', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'c', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'W'},
            {'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W'},
            {'W', 'f', 'f', 'f', 'f', 'f', 'f', 'f', ' ', ' ', ' ', ' ', 'f', 'f', 'f', 'f', 'f', 'f', 'W'},
            {'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W'},
            {'W', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'W'},
            {'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W'},
            {'W', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'W'},
            {'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W'},
            {'W', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'W'},
            {'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W'},
            {'W', 'f', 'f', 'f', 'c', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'W'},
            {'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'f', 'W', 'W', 'W', 'W', 'W', 'f', 'W'},
            {'W', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
    };

    private char[][] grid = new char[INITIAL_GRID.length][INITIAL_GRID[0].length];

    private PacMan pacman;
    private List<Ghost> ghosts;
    private final List<Ghost> initialGhosts;
    private int points = 0;
    private int highScore = 0;
    private boolean gameOver = false;
    private boolean paused = false;

    public Game(PacMan pacman, List<Ghost> ghosts) {
        this.pacman = pacman;
        this.ghosts = ghosts;
        this.initialGhosts = ghosts.stream()
                .map(ghost -> new Ghost(ghost.getRow(), ghost.getCol(), ghost.getType()))
                .toList();
        this.highScore = readHighScore();
        resetGrid();
    }

    private void resetGrid() {
        for (int i = 0; i < INITIAL_GRID.length; i++) {
            System.arraycopy(INITIAL_GRID[i], 0, grid[i], 0, INITIAL_GRID[i].length);
        }
    }

    public void startGame(Pane root) {
        Thread pacmanThread = new Thread(() -> {
            while (!gameOver) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    if (!gameOver && !paused) {
                        pacman.checkCollision(root, ghosts, this);
                        Renderer.renderGrid(root, grid, pacman, ghosts, points);
                    }
                });
            }
        });
        pacmanThread.setDaemon(true);
        pacmanThread.start();

        Thread ghostThread = new Thread(() -> {
            while (!gameOver) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    if (!gameOver && !paused) {
                        for (Ghost ghost : ghosts) {
                            ghost.move(grid, pacman, ghosts);
                        }
                        Renderer.renderGrid(root, grid, pacman, ghosts, points);
                    }
                });
            }
        });
        ghostThread.setDaemon(true);
        ghostThread.start();
    }

    public void handlePowerFoodCollection() {
        Fruit.handleFruitCollection(pacman, grid, ghosts.toArray(new Ghost[0]), this);
    }

    public void loseLife(Pane root) {
        pacman.loseLife();
        if (pacman.getLives() > 0) {
            pacman.respawn();
            ghosts.forEach(Ghost::resetPosition);
        } else {
            if (points > highScore) {
                highScore = points;
                writeHighScore(highScore);
            }
            gameOver(root);
        }
    }

    public void addPoints(int points) {
        this.points += points;
        System.out.println("addPoints called with points: " + points + ", Total: " + this.points);
    }

    private void gameOver(Pane root) {
        gameOver = true;

        Stage gameOverStage = new Stage();
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: black;");
        Scene gameOverScene = new Scene(vbox, 400, 300);

        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFill(javafx.scene.paint.Color.RED);
        gameOverText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        Text highestScoreText = new Text("Highest Score: " + highScore);
        highestScoreText.setFill(javafx.scene.paint.Color.WHITE);
        highestScoreText.setStyle("-fx-font-size: 24px;");

        Button retryButton = new Button("Retry");
        retryButton.setOnAction(event -> {
            gameOverStage.close();
            restartGame(root);
        });

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(event -> {
            Platform.exit();
        });

        vbox.getChildren().addAll(gameOverText, highestScoreText, retryButton, quitButton);

        gameOverStage.setScene(gameOverScene);
        gameOverStage.setTitle("Game Over");
        gameOverStage.show();
    }

    private void restartGame(Pane root) {
        pacman.respawn();
        pacman.setLives(3);
        ghosts.clear();
        ghosts.addAll(initialGhosts);
        points = 0;
        gameOver = false;
        resetGrid();
        startGame(root);
        root.getChildren().clear();
        Renderer.renderGrid(root, grid, pacman, ghosts, points);
    }

    private int readHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    private void writeHighScore(int highScore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"))) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public char[][] getGrid() {
        return grid;
    }

    public void collectFruit(int points) {
        addPoints(points);
    }
    public int getPoints() {
        return points;
    }

    public void togglePause() {
        paused = !paused;
    }
}