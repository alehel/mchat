package io.aleksander.mchat.controller;

import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.messageservice.mqtt3.MqttMessageService;
import io.aleksander.mchat.model.Message;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrimaryController {
    @FXML
    TextField messageTextField;
    @FXML
    TextArea messagesTextArea;

    MessageService mqttService = new MqttMessageService("tcp://mqtt.eclipse.org:1883");


    public PrimaryController() {
        mqttService.addMessageReceivedListener(this::handleMessage);
        mqttService.connect();
    }

    private void handleMessage(Message message) {
        messagesTextArea.appendText(message.sender() + ": " + message.content() + "\n");
    }

    @FXML
    private void sendMessage() {
        mqttService.sendMessage(messageTextField.getText());
        messageTextField.clear();
    }
}
