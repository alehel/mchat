package io.aleksander.mchat.controller;

import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.messageservice.MessageServiceFactory;
import io.aleksander.mchat.messageservice.MessageServiceType;
import io.aleksander.mchat.model.Setting;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class NewConnectionController {
  @FXML
  Pane settingsPane;

  @FXML
  ChoiceBox<MessageServiceType> connectionTypeSelector;

  private boolean userRequestedConnection = false;

  private MessageService messageService;

  public NewConnectionController() {
    Platform.runLater(this::initializeChoiceBox);
  }

  private void initializeChoiceBox() {
    ObservableList<MessageServiceType> messageTypes =
        FXCollections.observableArrayList(MessageServiceType.values());
    connectionTypeSelector.setItems(messageTypes);
    connectionTypeSelector.getSelectionModel()
        .selectedItemProperty()
        .addListener((listener, oldValue, newValue) -> {
          settingsPane.getChildren().clear();
          messageService = MessageServiceFactory.getMessageService(newValue);
          ConnectionSettingsGuiBuilder builder = new ConnectionSettingsGuiBuilder();
          Pane pane = builder.buildGuiForSettings(messageService.getSettings());
          settingsPane.getChildren().add(pane);
        });
  }

  @FXML
  private void connect() {
    mapSettings();
    userRequestedConnection = true;
    Stage stage = (Stage) settingsPane.getScene().getWindow();
    stage.close();
  }

  private void mapSettings() {
    if (messageService != null) {
      for(Setting setting : messageService.getSettings()) {
        Node node = settingsPane.lookup("#" + setting.getId());
        if (node instanceof TextField textField) {
          setting.setValue(textField.getText());
        }
      }
    }
  }

  public boolean btnConnectClicked() {
    return userRequestedConnection;
  }

  public MessageService getDefinedMessageService() {
    return messageService;
  }
}

