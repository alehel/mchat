package io.aleksander.mchat.controller;

import io.aleksander.mchat.App;
import io.aleksander.mchat.messageservice.MessageReceivedListener;
import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.MessageType;
import io.aleksander.mchat.templateengine.ChatTemplateEngine;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrimaryController implements MessageReceivedListener {

  private final ChatTemplateEngine chatTemplateEngine = new ChatTemplateEngine();
  @FXML TextField messageTextField;
  @FXML WebView chatWebView;
  @FXML Button sendButton;
  @FXML ListView<MessageService> connectionList;
  WebEngine webEngine;
  MessageService activeMessageService;

  public PrimaryController() {
    Platform.runLater(
        () -> {
          webEngine = chatWebView.getEngine();
          webEngine.setUserStyleSheetLocation(
              getClass().getResource(ViewResource.CHAT_THEME_LIGHT_CSS.getPath()).toString());
          chatWebView.setContextMenuEnabled(false);
          sendButton.setDisable(true);
          connectionList
              .getSelectionModel()
              .selectedItemProperty()
              .addListener(
                  (observableValue, oldValue, newValue) -> {
                    if (oldValue != newValue && newValue != null) {
                      setActiveMessageService(newValue);
                    }
                  });
        });
  }

  @FXML
  private void sendMessage() {
    if (activeMessageService != null) {
      Message message =
        new Message(
            MessageType.USER_MESSAGE,
            activeMessageService.getClientId(),
            activeMessageService.getUserName(),
            messageTextField.getText());
      activeMessageService.sendMessage(message);
      messageTextField.clear();
    }
  }

  @FXML
  private void showNewConnectionScreen() {
    try {
      FXMLLoader fxmlLoader =
          new FXMLLoader(App.class.getResource(ViewResource.NEW_CONNECTION_FXML.getPath()));
      Scene scene = new Scene(fxmlLoader.load());
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle("New Connection");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setHeight(250);
      stage.setMinHeight(250);
      stage.setWidth(400);
      stage.setMinWidth(400);
      stage.showAndWait();
      NewConnectionController controller = fxmlLoader.getController();
      if (controller.btnConnectClicked()) {
        MessageService definedMessageService = controller.getDefinedMessageService();
        connectionList.getItems().add(definedMessageService);
        setActiveMessageService(definedMessageService);
        activeMessageService.addMessageReceivedListener(this);
        activeMessageService.connect();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void setActiveMessageService(MessageService definedMessageService) {
    activeMessageService = definedMessageService;
    String chatHtml =
        chatTemplateEngine.generateChatHtml(
            activeMessageService.getDisplayString(), activeMessageService.getChatHistory());
    Platform.runLater(() -> webEngine.loadContent(chatHtml));
    sendButton.setDisable(false);
  }

  @Override
  public void onMessageReceived(Message message) {
    log.info(message.toString());
    String chatHtml =
        chatTemplateEngine.generateChatHtml(
            activeMessageService.getDisplayString(), activeMessageService.getChatHistory());
    Platform.runLater(() -> webEngine.loadContent(chatHtml));
  }
}
