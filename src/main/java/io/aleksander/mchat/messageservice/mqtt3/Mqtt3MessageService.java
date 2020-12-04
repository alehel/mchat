package io.aleksander.mchat.messageservice.mqtt3;

import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.messageservice.MessageServiceType;
import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.RequiredFieldValidator;
import io.aleksander.mchat.model.Setting;
import java.util.Map;
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
  private IMqttClient mqttClient;

  public Mqtt3MessageService() {
    super(MessageServiceType.MQQT3);
    getSettings()
        .put(
            SETTING_SERVER_URL,
            new Setting(SETTING_SERVER_URL, "Server URL", true, new RequiredFieldValidator()));
    getSettings()
        .put(
            SETTING_CHAT_ROOM,
            new Setting(SETTING_CHAT_ROOM, "Chat Room", true, new RequiredFieldValidator()));
  }

  public Mqtt3MessageService(@NonNull MqttClient client, @NonNull Map<String, Setting> settings) {
    super(MessageServiceType.MQQT3);
    mqttClient = client;
    setSettings(settings);
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
      String server = getSettings().get(SETTING_SERVER_URL).getValue();
      String chatRoom = getSettings().get(SETTING_CHAT_ROOM).getValue();

      mqttClient = new MqttClient(server, getClientId(), new MemoryPersistence());
      mqttClient.connect(getDefaultConnectionSettings());
      mqttClient.subscribe(
          chatRoom,
          (payloadTopic, payload) -> {
            Message message = SerializationUtils.deserialize(payload.getPayload());
            handleMessageReceived(message);
          });
      log.info("Connected to chat room.");
    } catch (MqttException e) {
      log.warn("Error connecting.");
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
        String chatRoom = getSettings().get(SETTING_CHAT_ROOM).getValue();
        mqttClient.publish(chatRoom, mqttMessage);
      } catch (MqttException e) {
        log.warn("Failed to send message.");
      }
    } else {
      throw new IllegalStateException("Can not send message without an active connection.");
    }
  }
}
