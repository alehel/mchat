package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.messageservice.mqtt3.MqttMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AbstractMessageServiceTest {
  private static final String VALID_MQTT_BROKER_URL = "tcp://mqtt.eclipse.org:1883";
  private static final String VALID_TOPIC_NAME = "VALID_TOPIC_NAME";

  @Test
  void differentMessageServicesGetDifferentClientIds() {
    AbstractMessageService abstractMessageService1 = new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    AbstractMessageService abstractMessageService2 = new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertNotEquals(abstractMessageService1.getClientId(), abstractMessageService2.getClientId());
  }

  @Test
  void messageReceivedListenerCanNotBeNull() {
    AbstractMessageService abstractMessageService = new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertThrows(NullPointerException.class, () -> abstractMessageService.addMessageReceivedListener(null));
  }

  @Test
  void messageReceivedListenerCanNotBeDuplicate() {
    AbstractMessageService abstractMessageService = new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    MessageReceivedListener listener = message -> {};
    abstractMessageService.addMessageReceivedListener(listener);
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        abstractMessageService.addMessageReceivedListener(listener));
  }

}