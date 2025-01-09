module com.sameerahman.pacman {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    opens com.sameerahman.pacman to javafx.fxml;
    exports com.sameerahman.pacman;
}