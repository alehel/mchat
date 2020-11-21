module io.aleksander {
    requires javafx.controls;
    requires javafx.fxml;

    opens io.aleksander to javafx.fxml;
    exports io.aleksander;
}