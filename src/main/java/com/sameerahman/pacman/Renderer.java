package com.sameerahman.pacman;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;

public class Renderer {
    private static final int CELL_SIZE = 32;

    public static void renderGrid(Pane root, char[][] grid, PacMan pacman, List<Ghost> ghosts, int points) {
        root.getChildren().clear();

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
                    String imagePath = null;
                    switch (cell) {
                        case 'W':
                            imagePath = "/com/sameerahman/pacman/textures/wall.png";
                            break;
                        case 'c':
                            imagePath = "/com/sameerahman/pacman/textures/cherry.png";
                            break;
                        case 'f':
                            imagePath = "/com/sameerahman/pacman/textures/powerFood.png";
                            imageView.setFitWidth(CELL_SIZE / 4);
                            imageView.setFitHeight(CELL_SIZE / 4);
                            break;
                    }
                    if (imagePath != null) {
                        URL resource = Renderer.class.getResource(imagePath);
                        if (resource != null) {
                            imageView.setImage(new Image(resource.toExternalForm()));
                        } else {
                            System.err.println("Image resource not found for cell: " + cell + " at path: " + imagePath);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error loading image for cell: " + cell);
                    e.printStackTrace();
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
        try {
            String pacmanImagePath = null;
            switch (pacman.getDirection()) {
                case 'L':
                    pacmanImagePath = "/com/sameerahman/pacman/textures/pacmanLeft.png";
                    break;
                case 'R':
                    pacmanImagePath = "/com/sameerahman/pacman/textures/pacmanRight.png";
                    break;
                case 'U':
                    pacmanImagePath = "/com/sameerahman/pacman/textures/pacmanUp.png";
                    break;
                case 'D':
                    pacmanImagePath = "/com/sameerahman/pacman/textures/pacmanDown.png";
                    break;
            }
            if (pacmanImagePath != null) {
                URL resource = Renderer.class.getResource(pacmanImagePath);
                if (resource != null) {
                    pacmanView.setImage(new Image(resource.toExternalForm()));
                } else {
                    System.err.println("PacMan image resource not found at path: " + pacmanImagePath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading PacMan image");
            e.printStackTrace();
        }
        root.getChildren().add(pacmanView);

        // Render ghosts
        for (Ghost ghost : ghosts) {
            ImageView ghostView = new ImageView();
            ghostView.setFitWidth(CELL_SIZE);
            ghostView.setFitHeight(CELL_SIZE);
            ghostView.setX(ghost.getCol() * CELL_SIZE);
            ghostView.setY(ghost.getRow() * CELL_SIZE);
            try {
                String ghostImagePath = ghost.isScared() ? "/com/sameerahman/pacman/textures/scaredGhost.png" : "/com/sameerahman/pacman/textures/" + getGhostImage(ghost.getType());
                URL resource = Renderer.class.getResource(ghostImagePath);
                if (resource != null) {
                    ghostView.setImage(new Image(resource.toExternalForm()));
                } else {
                    System.err.println("Ghost image resource not found for type: " + ghost.getType() + " at path: " + ghostImagePath);
                }
            } catch (Exception e) {
                System.err.println("Error loading ghost image for type: " + ghost.getType());
                e.printStackTrace();
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