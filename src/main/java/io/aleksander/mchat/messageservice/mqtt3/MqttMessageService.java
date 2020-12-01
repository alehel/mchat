package io.aleksander.mchat.messageservice.mqtt3;

import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.messageservice.MessageServiceType;
import io.aleksander.mchat.model.Message;
import lombok.Getter;
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
public class MqttMessageService extends MessageService {

  private IMqttClient mqttClient;
  @Getter private String chatRoom;
  @Getter private String serverUrl;

  public MqttMessageService(@NonNull String serverUrl, @NonNull String chatRoom) {
    super(MessageServiceType.MQQT3);
    this.serverUrl = serverUrl;
    validateAndSetTopic(chatRoom);

    try {
      mqttClient = new MqttClient(serverUrl, getClientId(), new MemoryPersistence());
    } catch (MqttException e) {
      log.warn("Failed to instantiate MqttClient.");
    }
  }

  public MqttMessageService(@NonNull MqttClient mqttClient, @NonNull String chatRoom) {
    super(MessageServiceType.MQQT3);
    this.mqttClient = mqttClient;
    this.serverUrl = mqttClient.getCurrentServerURI();
    validateAndSetTopic(chatRoom);
  }

  private void validateAndSetTopic(String topic) {
    if (topic.isBlank()) {
      throw new IllegalArgumentException("Topic must be specified.");
    }

    this.chatRoom = topic;
  }

  @Override
  public void connect() {
    if (!mqttClient.isConnected()) {
      try {
        mqttClient.connect(getConnectionSettings());
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
  }

  private MqttConnectOptions getConnectionSettings() {
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
        mqttClient.publish(chatRoom, mqttMessage);
      } catch (MqttException e) {
        log.warn("Failed to send message.");
      }
    } else {
      throw new IllegalStateException("Can not send message without an active connection.");
    }
  }
}
