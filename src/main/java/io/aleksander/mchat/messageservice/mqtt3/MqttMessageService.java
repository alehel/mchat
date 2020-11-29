package io.aleksander.mchat.messageservice.mqtt3;

import io.aleksander.mchat.messageservice.MessageService;
import io.aleksander.mchat.model.Message;
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
  private static final String TOPIC = "MCHAT TEST CHANNEL";

  public MqttMessageService(String serverUrl) {
    if(serverUrl == null) {
      throw new IllegalArgumentException("serverUrl can not be null!");
    }

    try {
      mqttClient = new MqttClient(serverUrl, getClientId(), new MemoryPersistence());
    } catch (MqttException e) {
      log.warn("Failed to instantiate MqttClient.");
    }
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
        mqttClient.subscribe(TOPIC, (topic, payload) -> {
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
    Message message = new Message(getClientId(), "Aleksander", messageContent, "");
    byte[] payload = SerializationUtils.serialize(message);
    MqttMessage mqttMessage = new MqttMessage(payload);
    try {
      mqttClient.publish(TOPIC, mqttMessage);
    } catch (MqttException e) {
      log.warn("Failed to send message.");
    }
  }
}
