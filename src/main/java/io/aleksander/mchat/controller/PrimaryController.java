package io.aleksander.mchat.controller;

import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.messageservice.mqtt3.Mqtt3MessageService;
import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.MessageType;
import io.aleksander.mchat.templateengine.ChatTemplateEngine;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrimaryController {

  private final ChatTemplateEngine chatTemplateEngine = new ChatTemplateEngine();
  private final List<Message> chatHistory = new ArrayList<>();
  @FXML TextField messageTextField;
  @FXML WebView chatWebView;
  WebEngine webEngine;
  MessageService messageService = new Mqtt3MessageService("tcp://mqtt.eclipse.org:1883", "TEST_TOPIC");

  public PrimaryController() {
    messageService.addMessageReceivedListener(this::handleMessage);
    messageService.connect();
    Platform.runLater(
        () -> {
          webEngine = chatWebView.getEngine();
          webEngine.setUserStyleSheetLocation(
              getClass().getResource("/io/aleksander/mchat/styles/theme_light.css").toString());
          chatWebView.setContextMenuEnabled(false);
        });
  }

  private void handleMessage(Message message) {
    chatHistory.add(message);
    String chatHtml = chatTemplateEngine.generateChatHtml("TEST_TOPIC", chatHistory);
    Platform.runLater(() -> webEngine.loadContent(chatHtml));
  }

  @FXML
  private void sendMessage() {
    Message message =
        new Message(
            MessageType.USER_MESSAGE,
            messageService.getClientId(),
            "Aleksander",
            messageTextField.getText());
    messageService.sendMessage(message);
    messageTextField.clear();
  }
}
