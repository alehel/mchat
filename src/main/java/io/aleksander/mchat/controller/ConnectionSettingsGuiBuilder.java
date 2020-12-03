package io.aleksander.mchat.controller;

import io.aleksander.mchat.model.Setting;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Constructs a settings view for insertion into a scene. The settings view is constructed
 * based on the available/required setting.
 */
public class ConnectionSettingsGuiBuilder {
  public Pane buildGuiForSettings(Map<String, Setting> settings) {
    Pane pane = new Pane();
    VBox container = new VBox();
    pane.getChildren().add(container);
    for(Setting setting : settings.values()) {
      container.getChildren().add(createSetting(setting));
    }
    return pane;
  }

  private HBox createSetting(Setting setting) {
    HBox container = new HBox();
    Label label = new Label(setting.getName());
    TextField textField = new TextField();
    textField.setId(setting.getId());
    container.getChildren().addAll(label, textField);
    return container;
  }
}
