module snowsan0113.clipscreen {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens snowsan0113.clipscreen to javafx.fxml;
    exports snowsan0113.clipscreen;
}