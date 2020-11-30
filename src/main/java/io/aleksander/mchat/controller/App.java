package io.aleksander.mchat.controller;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(App.class.getResource("/io/aleksander/mchat/view/primary.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 640, 480);
    stage.setScene(scene);
    stage.setOnCloseRequest(
        e -> {
          Platform.exit();
          System.exit(0);
        });
    stage.show();
  }
}
