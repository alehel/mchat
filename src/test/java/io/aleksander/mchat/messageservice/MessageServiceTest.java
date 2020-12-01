package io.aleksander.mchat.messageservice;

import static io.aleksander.mchat.TestUtil.VALID_MESSAGE;
import static io.aleksander.mchat.TestUtil.VALID_MQTT_BROKER_URL;
import static io.aleksander.mchat.TestUtil.VALID_TOPIC_NAME;
import static org.mockito.Mockito.never;

import io.aleksander.mchat.messageservice.mqtt3.Mqtt3MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MessageServiceTest {

  @Test
  void differentMessageServicesGetDifferentClientIds() {
    MessageService messageService1 =
        new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    MessageService messageService2 =
        new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertNotEquals(messageService1.getClientId(), messageService2.getClientId());
  }

  @Test
  void messageReceivedListenerCanNotBeNull() {
    MessageService messageService = new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertThrows(
        NullPointerException.class, () -> messageService.addMessageReceivedListener(null));
  }

  @Test
  void messageReceivedListenerCanNotBeDuplicate() {
    MessageService messageService = new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    MessageReceivedListener listener = message -> {};
    messageService.addMessageReceivedListener(listener);
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> messageService.addMessageReceivedListener(listener));
  }

  @Test
  void messageServiceTypeIsSetByConstructor() {
    MessageService messageService = new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertNotNull(messageService.getMessageServiceType());
  }

  @Test
  void subscribedMessageReceivedListenerReceivesMessage() {
    MessageService messageService = new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    MessageReceivedListener messageReceivedListener = Mockito.mock(MessageReceivedListener.class);
    messageService.addMessageReceivedListener(messageReceivedListener);
    messageService.handleMessageReceived(VALID_MESSAGE);

    Mockito.verify(messageReceivedListener).onMessageReceived(VALID_MESSAGE);
  }

  @Test
  void multipleSubscribedListenersAllReceiveMessage() {
    MessageService messageService = new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    MessageReceivedListener messageReceivedListenerA = Mockito.mock(MessageReceivedListener.class);
    MessageReceivedListener messageReceivedListenerB = Mockito.mock(MessageReceivedListener.class);
    messageService.addMessageReceivedListener(messageReceivedListenerA);
    messageService.addMessageReceivedListener(messageReceivedListenerB);
    messageService.handleMessageReceived(VALID_MESSAGE);

    Mockito.verify(messageReceivedListenerA).onMessageReceived(VALID_MESSAGE);
    Mockito.verify(messageReceivedListenerB).onMessageReceived(VALID_MESSAGE);
  }

  @Test
  void unsubscribedListenerDoesNotReceiveMessage() {
    MessageService messageService = new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    MessageReceivedListener messageReceivedListenerA = Mockito.mock(MessageReceivedListener.class);
    MessageReceivedListener messageReceivedListenerB = Mockito.mock(MessageReceivedListener.class);
    messageService.addMessageReceivedListener(messageReceivedListenerA);
    messageService.addMessageReceivedListener(messageReceivedListenerB);
    messageService.removeMessageReceivedListener(messageReceivedListenerA);
    messageService.handleMessageReceived(VALID_MESSAGE);

    Mockito.verify(messageReceivedListenerA, never()).onMessageReceived(VALID_MESSAGE);
    Mockito.verify(messageReceivedListenerB).onMessageReceived(VALID_MESSAGE);
  }

  @Test
  void canNotRemoveListenerWhichWasNotAttached() {
    MessageService messageService = new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    MessageReceivedListener messageReceivedListener = Mockito.mock(MessageReceivedListener.class);

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> messageService.removeMessageReceivedListener(messageReceivedListener));
  }

  @Test
  void noExceptionIfListOfListenersIsEmptyWhenMessageIsHandled() {
    MessageService messageService = new Mqtt3MessageService(VALID_MQTT_BROKER_URL, VALID_TOPIC_NAME);
    Assertions.assertDoesNotThrow(() -> messageService.handleMessageReceived(VALID_MESSAGE));
  }
}
