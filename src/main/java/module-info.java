module com.sameerahman.pacman {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.sameerahman.pacman to javafx.fxml;
    exports com.sameerahman.pacman;
}