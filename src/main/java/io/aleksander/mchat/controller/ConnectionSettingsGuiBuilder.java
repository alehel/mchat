package io.aleksander.mchat.controller;

import io.aleksander.mchat.model.Setting;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Constructs a settings view for insertion into a scene. The settings view is constructed
 * based on the available/required setting.
 */
public class ConnectionSettingsGuiBuilder {
  public Pane buildGuiForSettings(List<Setting> settings) {
    GridPane gridPane = new GridPane();
    gridPane.setHgap(10);
    gridPane.setVgap(5);
    gridPane.setPadding(new Insets(20, 10, 10, 20));

    int row = 0;
    for(Setting setting : settings) {
      Label label = new Label(setting.getName() + ": ");
      TextField textField = new TextField();
      textField.setId(setting.getId());
      textField.setPrefWidth(200);
      label.setLabelFor(textField);
      gridPane.add(label, 0,row);
      gridPane.add(textField, 1, row);
      row++;
    }

    return gridPane;
  }
}
