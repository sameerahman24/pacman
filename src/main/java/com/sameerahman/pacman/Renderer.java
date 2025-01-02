package com.sameerahman.pacman;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.util.List;

public class Renderer {
    private static final int CELL_SIZE = 32;

    public static void renderGrid(Pane root, char[][] grid, PacMan pacman, List<Ghost> ghosts, int points) {
        root.getChildren().clear(); //

        // Render grid
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                char cell = grid[row][col];
                ImageView imageView = new ImageView();
                imageView.setFitWidth(CELL_SIZE);
                imageView.setFitHeight(CELL_SIZE);
                imageView.setX(col * CELL_SIZE);
                imageView.setY(row * CELL_SIZE);

                try {
                    switch (cell) {
                        case 'W':
                            imageView.setImage(new Image(Renderer.class.getResource("/com/sameerahman/pacman/wall.png").toExternalForm()));
                            break;
                        case 'c':
                            imageView.setImage(new Image(Renderer.class.getResource("/com/sameerahman/pacman/cherry.png").toExternalForm()));
                            break;
                        case 'f':
                            imageView.setFitWidth(CELL_SIZE / 4);
                            imageView.setFitHeight(CELL_SIZE / 4);
                            imageView.setImage(new Image(Renderer.class.getResource("/com/sameerahman/pacman/powerFood.png").toExternalForm()));
                            break;
                        case ' ':
                            // No texture for empty space
                            break;
                    }
                } catch (NullPointerException e) {
                    System.err.println("Image resource not found for cell: " + cell);
                }

                root.getChildren().add(imageView);
            }
        }

        // Render PacMan
        ImageView pacmanView = new ImageView();
        pacmanView.setFitWidth(CELL_SIZE);
        pacmanView.setFitHeight(CELL_SIZE);
        pacmanView.setX(pacman.getCol() * CELL_SIZE);
        pacmanView.setY(pacman.getRow() * CELL_SIZE);
        switch (pacman.getDirection()) {
            case 'L':
                pacmanView.setImage(new Image(Renderer.class.getResource("/com/sameerahman/pacman/pacmanLeft.png").toExternalForm()));
                break;
            case 'R':
                pacmanView.setImage(new Image(Renderer.class.getResource("/com/sameerahman/pacman/pacmanRight.png").toExternalForm()));
                break;
            case 'U':
                pacmanView.setImage(new Image(Renderer.class.getResource("/com/sameerahman/pacman/pacmanUp.png").toExternalForm()));
                break;
            case 'D':
                pacmanView.setImage(new Image(Renderer.class.getResource("/com/sameerahman/pacman/pacmanDown.png").toExternalForm()));
                break;
        }
        root.getChildren().add(pacmanView);

        // Render ghosts
        for (Ghost ghost : ghosts) {
            ImageView ghostView = new ImageView();
            ghostView.setFitWidth(CELL_SIZE);
            ghostView.setFitHeight(CELL_SIZE);
            ghostView.setX(ghost.getCol() * CELL_SIZE);
            ghostView.setY(ghost.getRow() * CELL_SIZE);
            if (ghost.isScared()) {
                ghostView.setImage(new Image(Renderer.class.getResource("/com/sameerahman/pacman/scaredGhost.png").toExternalForm()));
            } else {
                ghostView.setImage(new Image(Renderer.class.getResource("/com/sameerahman/pacman/" + getGhostImage(ghost.getType())).toExternalForm()));
            }
            root.getChildren().add(ghostView);
        }

        // Create pointsText and set properties
        Text pointsText = new Text("Points: " + points);
        pointsText.setFill(Color.YELLOW);
        pointsText.setStyle("-fx-font-size: 20;");
        pointsText.setX(10); // Set it to the bottom left corner
        pointsText.setY(root.getHeight() - 10); // 10px from the bottom
        root.getChildren().add(pointsText);

        // Create livesText and set properties
        Text livesText = new Text("Lives: " + pacman.getLives());
        livesText.setFill(Color.RED);
        livesText.setStyle("-fx-font-size: 20;");

        // Initially set livesText at bottom-right corner
        livesText.setX(root.getWidth() - 10); // Set initially at bottom-right, 10px from the right
        livesText.setY(root.getHeight() - 10); // 10px from the bottom

        // Add a listener to adjust livesText position after it's layout is complete
        livesText.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            livesText.setX(root.getWidth() - newBounds.getWidth() - 10); // 10px from the right edge
        });

        root.getChildren().add(livesText);
    }

    private static String getGhostImage(char type) {
        switch (type) {
            case 'r':
                return "redGhost.png";
            case 'b':
                return "blueGhost.png";
            case 'g':
                return "pinkGhost.png";
            case 'o':
                return "orangeGhost.png";
            default:
                return "";
        }
    }
}
