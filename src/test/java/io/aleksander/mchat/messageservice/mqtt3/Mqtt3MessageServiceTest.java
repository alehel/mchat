package io.aleksander.mchat.messageservice.mqtt3;

import static io.aleksander.mchat.TestUtil.VALID_MESSAGE;
import static io.aleksander.mchat.TestUtil.VALID_MQTT_BROKER_URL;
import static io.aleksander.mchat.TestUtil.VALID_TOPIC_NAME;

import io.aleksander.mchat.messageservice.MessageServiceType;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Mqtt3MessageServiceTest {
  @Test
  void validServerUrlGetsSet() {
    Mqtt3MessageService mqtt3MessageService =
        new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertEquals(VALID_MQTT_BROKER_URL, mqtt3MessageService.getServerUrl());
  }

  @Test
  void validTopicGetsSet() {
    Mqtt3MessageService mqtt3MessageService =
        new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertEquals(VALID_TOPIC_NAME, mqtt3MessageService.getChatRoom());
  }

  @Test
  void serverUrlCannotBeNull() {
    Assertions.assertThrows(
        NullPointerException.class, () -> new Mqtt3MessageService((String) null, VALID_TOPIC_NAME));
  }

  @Test
  void mqttClientCanNotBeNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> new Mqtt3MessageService((MqttClient) null, VALID_TOPIC_NAME));
  }

  @Test
  void chatTopicCannotBeNull() {
    Assertions.assertThrows(
        NullPointerException.class, () -> new Mqtt3MessageService("a url", null));
  }

  @Test
  void chatTopicCannotBeEmpty() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> new Mqtt3MessageService("a url", ""));
  }

  @Test
  void messageCanNotBeNull() {
    Mqtt3MessageService mqtt3MessageService =
        new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertThrows(NullPointerException.class, () -> mqtt3MessageService.sendMessage(null));
  }

  @Test
  void illegalStateToSendMessageBeforeConnecting() {
    Mqtt3MessageService mqtt3MessageService =
        new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertThrows(
        IllegalStateException.class, () -> mqtt3MessageService.sendMessage(VALID_MESSAGE));
  }

  @Test
  void messageServiceTypeIsCorrect() {
    Mqtt3MessageService mqtt3MessageService =
        new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertEquals(MessageServiceType.MQQT3, mqtt3MessageService.getMessageServiceType());
  }

  @Test
  void isConnected_returnsTrueIfMqttClientIsConnected() {
    MqttClient client = Mockito.mock(MqttClient.class);
    Mqtt3MessageService mqtt3MessageService = new Mqtt3MessageService(client, VALID_TOPIC_NAME);
    Mockito.when(client.getCurrentServerURI()).thenReturn(VALID_MQTT_BROKER_URL);
    Mockito.when(client.isConnected()).thenReturn(true);

    boolean isConnected = mqtt3MessageService.isConnected();

    Mockito.verify(client).isConnected();
    Assertions.assertTrue(isConnected);
  }

  @Test
  void isConnected_returnsFalseifMqttClientIsNotConnected() {
    MqttClient client = Mockito.mock(MqttClient.class);
    Mqtt3MessageService mqtt3MessageService = new Mqtt3MessageService(client, VALID_TOPIC_NAME);
    Mockito.when(client.getCurrentServerURI()).thenReturn(VALID_MQTT_BROKER_URL);
    Mockito.when(client.isConnected()).thenReturn(false);

    boolean isConnected = mqtt3MessageService.isConnected();

    Mockito.verify(client).isConnected();
    Assertions.assertFalse(isConnected);
  }
}
