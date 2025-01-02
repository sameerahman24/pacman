package com.sameerahman.pacman;

public class Fruit {
    public static void handleFruitCollection(PacMan pacman, char[][] grid, Ghost[] ghosts, Game game) {
        int row = pacman.getRow();
        int col = pacman.getCol();
        if (grid[row][col] == 'f') {
            grid[row][col] = ' ';
            game.collectFruit(10);
        } else if (grid[row][col] == 'c') {
            grid[row][col] = ' ';
            game.collectFruit(50);
            for (Ghost ghost : ghosts) {
                ghost.setScared(true);
            }
        }
    }
}