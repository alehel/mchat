package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.messageservice.mqtt3.MqttMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MessageServiceTest {

  private static final String VALID_MQTT_BROKER_URL = "tcp://mqtt.eclipse" + ".org:1883";
  private static final String VALID_TOPIC_NAME = "VALID_TOPIC_NAME";

  @Test
  void differentMessageServicesGetDifferentClientIds() {
    MessageService messageService1 =
        new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    MessageService messageService2 =
        new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertNotEquals(messageService1.getClientId(), messageService2.getClientId());
  }

  @Test
  void messageReceivedListenerCanNotBeNull() {
    MessageService messageService = new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertThrows(
        NullPointerException.class, () -> messageService.addMessageReceivedListener(null));
  }

  @Test
  void messageReceivedListenerCanNotBeDuplicate() {
    MessageService messageService = new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    MessageReceivedListener listener = message -> {};
    messageService.addMessageReceivedListener(listener);
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> messageService.addMessageReceivedListener(listener));
  }

  @Test
  void messageServiceTypeIsSet() {
    MessageService messageService = new MqttMessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertNotNull(messageService.getMessageServiceType());
  }
}
