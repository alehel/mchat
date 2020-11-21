module io.aleksander {
    requires javafx.controls;
    requires javafx.fxml;

    opens io.aleksander.mchat.controller to javafx.fxml;
    exports io.aleksander.mchat.controller;
}