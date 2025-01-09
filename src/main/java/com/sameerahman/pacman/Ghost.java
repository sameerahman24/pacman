package com.sameerahman.pacman;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Ghost {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private int initialRow;
    private int initialCol;
    private int row;
    private int col;
    private char type;
    private boolean scared;
    private Timer scareTimer;

    public Ghost(int row, int col, char type) {
        this.initialRow = row;
        this.initialCol = col;
        this.row = row;
        this.col = col;
        this.type = type;
        this.scared = false;
        this.scareTimer = new Timer();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getType() {
        return type;
    }

    public boolean isScared() {
        return scared;
    }

    public void setScared(boolean scared) {
        this.scared = scared;
        if (scared) {
            Sound.playIntermissionSound();
            scareTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setScared(false);
                }
            }, 5000);
        }
    }

    public void move(char[][] grid, PacMan pacman, List<Ghost> ghosts) {
        int[] direction = findBestPath(grid, pacman.getRow(), pacman.getCol(), scared);
        if (direction != null) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (isValidMove(grid, newRow, newCol) && !isCollidingWithGhost(newRow, newCol, ghosts)) {
                updateGrid(grid);
                row = newRow;
                col = newCol;
                updateGrid(grid);
            }
        }

        // Check if PacMan eats a cherry
        if (grid[pacman.getRow()][pacman.getCol()] == 'c') {
            ghosts.remove(this);
        }
    }

    private int[] findBestPath(char[][] grid, int targetRow, int targetCol, boolean away) {
        int bestDistance = away ? -1 : Integer.MAX_VALUE;
        int[] bestDirection = null;

        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (isValidMove(grid, newRow, newCol)) {
                int distance = Math.abs(newRow - targetRow) + Math.abs(newCol - targetCol);
                if ((away && distance > bestDistance) || (!away && distance < bestDistance)) {
                    bestDistance = distance;
                    bestDirection = direction;
                }
            }
        }

        return bestDirection;
    }

    private boolean isValidMove(char[][] grid, int newRow, int newCol) {
        return newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length && grid[newRow][newCol] != 'W';
    }

    private boolean isCollidingWithGhost(int newRow, int newCol, List<Ghost> ghosts) {
        for (Ghost ghost : ghosts) {
            if (ghost != this && ghost.getRow() == newRow && ghost.getCol() == newCol) {
                return true;
            }
        }
        return false;
    }

    private void updateGrid(char[][] grid) {
        if (grid[row][col] != 'f') {
            grid[row][col] = (grid[row][col] == type) ? ' ' : type;
        }
    }

    public void resetPosition() {
        this.row = initialRow;
        this.col = initialCol;
    }
}
