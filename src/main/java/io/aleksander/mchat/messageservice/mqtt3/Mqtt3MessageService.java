package io.aleksander.mchat.messageservice.mqtt3;

import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.messageservice.MessageServiceType;
import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.RequiredFieldValidator;
import io.aleksander.mchat.model.Setting;
import java.util.Arrays;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public class Mqtt3MessageService extends MessageService {

  private static final String SETTING_SERVER_URL = "SETTING_SERVER_URL";
  private static final String SETTING_CHAT_ROOM = "SETTING_CHAT_ROOM";
  private static final String SETTING_USER_NAME = "SETTING_USER_NAME";
  private IMqttClient mqttClient;
  private final Setting serverUrl;
  private final Setting chatRoom;
  private final Setting userName;

  public Mqtt3MessageService() {
    super(MessageServiceType.MQQT3);
    serverUrl = new Setting(SETTING_SERVER_URL, "Server URL", true, new RequiredFieldValidator());
    chatRoom = new Setting(SETTING_CHAT_ROOM, "Chat Room", true, new RequiredFieldValidator());
    userName = new Setting(SETTING_USER_NAME, "User Name", true, new RequiredFieldValidator());
    getSettings().addAll(Arrays.asList(userName, serverUrl, chatRoom));
  }

  @Override
  public void connect() {
    if (mqttClient == null) {
      if (allSettingsAreValid()) {
        connectAndSubscribe();
      } else {
        throw new IllegalStateException("Not all settings are valid.");
      }
    } else {
      log.info("Client already connected.");
    }
  }

  private void connectAndSubscribe() {
    try {
      log.info(
          "Attempting to connect to room "
              + chatRoom.getValue()
              + " on URL "
              + serverUrl.getValue());
      mqttClient = new MqttClient(serverUrl.getValue(), getClientId(), new MemoryPersistence());
      mqttClient.connect(getDefaultConnectionSettings());
      mqttClient.subscribe(
          chatRoom.getValue(),
          (payloadTopic, payload) -> {
            Message message = SerializationUtils.deserialize(payload.getPayload());
            handleMessageReceived(message);
          });
      log.info("Connected to chat room.");
    } catch (MqttException e) {
      log.warn("Error connecting. " + e.getMessage());
    }
  }

  private MqttConnectOptions getDefaultConnectionSettings() {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    return options;
  }

  @Override
  public void sendMessage(@NonNull Message message) {
    if (mqttClient.isConnected()) {
      byte[] payload = SerializationUtils.serialize(message);
      MqttMessage mqttMessage = new MqttMessage(payload);
      try {
        mqttClient.publish(chatRoom.getValue(), mqttMessage);
      } catch (MqttException e) {
        log.warn("Failed to send message.");
      }
    } else {
      throw new IllegalStateException("Can not send message without an active connection.");
    }
  }

  @Override
  public String getDisplayString() {
    return serverUrl.getValue() + "\n" + chatRoom.getValue();
  }

  @Override
  public String getUserName() {
    return userName.getValue();
  }
}
