package com.sameerahman.pacman;

import java.util.Timer;
import java.util.TimerTask;

public class Fruit {
    public static void handleFruitCollection(PacMan pacman, char[][] grid, Ghost[] ghosts, Game game) {
        int row = pacman.getRow();
        int col = pacman.getCol();
        if (grid[row][col] == 'f') {
            grid[row][col] = ' ';
            game.collectFruit(10);
        } else if (grid[row][col] == 'c') {
            grid[row][col] = ' ';
            pacman.setInvincible(true);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    pacman.setInvincible(false);
                }
            }, 5000); // 5 seconds
            game.collectFruit(50);
            for (Ghost ghost : ghosts) {
                ghost.setScared(true);
            }
        }
    }
}