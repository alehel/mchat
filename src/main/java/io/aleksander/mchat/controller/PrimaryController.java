package io.aleksander.mchat.controller;

import io.aleksander.mchat.messageservice.AbstractMessageService;
import io.aleksander.mchat.messageservice.mqtt3.MqttMessageService;
import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.controller.util.MessageToTextConverter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrimaryController {
    @FXML
    TextField messageTextField;
    @FXML
    TextFlow messageLog;
    @FXML
    ScrollPane messageLogScrollPane;

    AbstractMessageService mqttService = new MqttMessageService("tcp://mqtt.eclipse.org:1883", "TEST_TOPIC");
    MessageToTextConverter messageToTextConverter = new MessageToTextConverter();


    public PrimaryController() {
        mqttService.addMessageReceivedListener(this::handleMessage);
        mqttService.connect();

        Platform.runLater(() -> messageLogScrollPane.setFitToWidth(true));
    }

    private void handleMessage(Message message) {
        Text text = messageToTextConverter.convert(mqttService.getClientId(), message);
        Platform.runLater(() -> messageLog.getChildren().addAll(text));
    }

    @FXML
    private void sendMessage() {
        mqttService.sendMessage(messageTextField.getText());
        messageTextField.clear();
    }
}
