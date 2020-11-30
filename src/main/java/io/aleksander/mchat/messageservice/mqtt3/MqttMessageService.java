package io.aleksander.mchat.messageservice.mqtt3;

import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.messageservice.MessageServiceType;
import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.MessageType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public class MqttMessageService extends MessageService {

  private IMqttClient mqttClient;
  @Getter private String topic;
  @Getter private String serverUrl;

  public MqttMessageService(String serverUrl, String topic) {
    super(MessageServiceType.MQQT3);
    verifyAndSetServerUrl(serverUrl);
    verifyAndSetTopic(topic);

    try {
      mqttClient = new MqttClient(serverUrl, getClientId(), new MemoryPersistence());
    } catch (MqttException e) {
      log.warn("Failed to instantiate MqttClient.");
    }
  }

  private void verifyAndSetServerUrl(String serverUrl) {
    if (serverUrl == null) {
      throw new IllegalArgumentException("serverUrl can not be null!");
    }

    this.serverUrl = serverUrl;
  }

  private void verifyAndSetTopic(String topic) {
    if (StringUtils.isEmpty(topic)) {
      throw new IllegalArgumentException("Topic must be specified.");
    }

    this.topic = topic;
  }

  @Override
  public void connect() {
    if (!mqttClient.isConnected()) {
      MqttConnectOptions options = new MqttConnectOptions();
      options.setAutomaticReconnect(true);
      options.setCleanSession(true);
      options.setConnectionTimeout(10);
      try {
        mqttClient.connect(options);
        mqttClient.subscribe(
            topic,
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

  @Override
  public void sendMessage(String messageContent) {
    Message message =
        new Message(MessageType.USER_MESSAGE, getClientId(), "Aleksander", messageContent);
    byte[] payload = SerializationUtils.serialize(message);
    MqttMessage mqttMessage = new MqttMessage(payload);
    try {
      mqttClient.publish(topic, mqttMessage);
    } catch (MqttException e) {
      log.warn("Failed to send message.");
    }
  }
}
