package com.sameerahman.pacman;

import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PacMan {
    private int row;
    private int col;
    private char direction;
    private int lives;
    private boolean invincible;

    public PacMan(int row, int col, char direction) {
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.lives = 3;
        this.invincible = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getDirection() {
        return direction;
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        if (!invincible) {
            setLives(getLives() - 1);
        }
    }

    public void respawn() {
        row = 1;
        col = 1;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void move(int rowDelta, int colDelta, char direction, char[][] grid, List<Ghost> ghosts, Game game, Pane root) {
        int newRow = row + rowDelta;
        int newCol = col + colDelta;

        if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length && grid[newRow][newCol] != 'W') {
            row = newRow;
            col = newCol;
            this.direction = direction;


            if (grid[row][col] == 'c' || grid[row][col] == 'f') {
                game.handlePowerFoodCollection();
            }
        }
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void checkCollision(Pane root, List<Ghost> ghosts, Game game) {

        for (Ghost ghost : ghosts) {
            if (ghost.getRow() == this.getRow() && ghost.getCol() == this.getCol()) {
                if (!isInvincible()) {
                    game.loseLife(root);
                } else {
                    ghosts.remove(ghost);
                    break;
                }
            }
        }
    }
}