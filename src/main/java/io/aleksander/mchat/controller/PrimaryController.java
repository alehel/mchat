package io.aleksander.mchat.controller;

import io.aleksander.mchat.messageservice.MessageReceivedListener;
import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.MessageType;
import io.aleksander.mchat.templateengine.ChatTemplateEngine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrimaryController implements MessageReceivedListener {

  private final ChatTemplateEngine chatTemplateEngine = new ChatTemplateEngine();
  private final List<Message> chatHistory = new ArrayList<>();
  @FXML TextField messageTextField;
  @FXML WebView chatWebView;
  @FXML Button sendButton;
  WebEngine webEngine;
  private final List<MessageService> messageServices = new ArrayList<>();
  MessageService activeMessageService;

  public PrimaryController() {
    Platform.runLater(
        () -> {
          webEngine = chatWebView.getEngine();
          webEngine.setUserStyleSheetLocation(
              getClass().getResource("/io/aleksander/mchat/styles/theme_light.css").toString());
          chatWebView.setContextMenuEnabled(false);
          sendButton.setDisable(true);
        });
  }

  @FXML
  private void sendMessage() {
    if (activeMessageService != null) {
      Message message =
        new Message(
            MessageType.USER_MESSAGE,
            activeMessageService.getClientId(),
            "Aleksander",
            messageTextField.getText());
      activeMessageService.sendMessage(message);
      messageTextField.clear();
    }
  }

  @FXML
  private void showNewConnectionScreen() {
    try {
      FXMLLoader fxmlLoader =
          new FXMLLoader(App.class.getResource("/io/aleksander/mchat/view/new_connection.fxml"));
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
        messageServices.add(definedMessageService);
        setActiveMessageService(definedMessageService);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void setActiveMessageService(MessageService definedMessageService) {
    activeMessageService = definedMessageService;
    sendButton.setDisable(false);
    activeMessageService.addMessageReceivedListener(this);
    activeMessageService.connect();
  }

  @Override
  public void onMessageReceived(Message message) {
    log.info(message.toString());
    chatHistory.add(message);
    String chatHtml = chatTemplateEngine.generateChatHtml("TEST_TOPIC", chatHistory);
    Platform.runLater(() -> webEngine.loadContent(chatHtml));
  }
}
