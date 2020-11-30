package io.aleksander.mchat.messageservice.mqtt3;

import io.aleksander.mchat.messageservice.MessageServiceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MqttMessageServiceTest {
  private static final String VALID_MQTT_BROKER_URL = "tcp://mqtt.eclipse.org:1883";
  private static final String VALID_TOPIC_NAME = "VALID_TOPIC_NAME";

  @Test
  void validServerUrlGetsSet() {
    MqttMessageService mqttMessageService =
        new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertEquals(VALID_MQTT_BROKER_URL, mqttMessageService.getServerUrl());
  }

  @Test
  void validTopicGetsSet() {
    MqttMessageService mqttMessageService =
        new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertEquals(VALID_TOPIC_NAME, mqttMessageService.getTopic());
  }

  @Test
  void serverUrlCannotBeNull() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          new MqttMessageService(null, VALID_TOPIC_NAME);
        });
  }

  @Test
  void chatTopicCannotBeNull() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          new MqttMessageService("a url", null);
        });
  }

  @Test
  void chatTopicCannotBeEmpty() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          new MqttMessageService("a url", "");
        });
  }

  @Test
  void messageServiceTypeIsCorrect() {
    MqttMessageService mqttMessageService =
        new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertEquals(MessageServiceType.MQQT3, mqttMessageService.getMessageServiceType());
  }
}
