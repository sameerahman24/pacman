package com.sameerahman.pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Root pane
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        // Scene
        Scene scene = new Scene(root, 19 * 32, 21 * 32);

        // Create PacMan and Ghosts
        PacMan pacman = new PacMan(1, 1, 'R');
        List<Ghost> ghosts = new ArrayList<>();
        ghosts.add(new Ghost(7, 8, 'r'));
        ghosts.add(new Ghost(7, 9, 'b'));
        ghosts.add(new Ghost(7, 10, 'g'));
        ghosts.add(new Ghost(7, 11, 'o'));

        // Create Game instance
        Game game = new Game(pacman, ghosts);
        game.startGame(root);


        // Handle key presses
        scene.setOnKeyPressed(event -> {
            if (!game.isGameOver()) {
                switch (event.getCode()) {
                    case W:
                        game.handlePowerFoodCollection();
                        pacman.move(-1, 0, 'U', game.getGrid(), ghosts, game, root);
                        break;
                    case S:
                        game.handlePowerFoodCollection();
                        pacman.move(1, 0, 'D', game.getGrid(), ghosts, game, root);
                        break;
                    case A:
                        game.handlePowerFoodCollection();
                        pacman.move(0, -1, 'L', game.getGrid(), ghosts, game, root);
                        break;
                    case D:
                        game.handlePowerFoodCollection();
                        pacman.move(0, 1, 'R', game.getGrid(), ghosts, game, root);
                        break;
                    case ESCAPE:
                        game.togglePause();
                        break;
                }
                Renderer.renderGrid(root, game.getGrid(), pacman, ghosts, game.getPoints());
            }
        });

        // Close event handler
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        // Stage properties
        primaryStage.setTitle("Pacman");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}