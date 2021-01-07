package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.MessageType;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MessageServiceTest {

  @Test
  public void addMessageReceivedListener_duplicateListener_throwsException() {
    MessageReceivedListener listener = Mockito.mock(MessageReceivedListener.class);
    MessageService messageService = new Mqtt3MessageService();
    messageService.addMessageReceivedListener(listener);

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      messageService.addMessageReceivedListener(listener);
    });
  }

  @Test
  public void addMessageReceivedListener_nullValue_throwsException() {
    MessageService messageService = new Mqtt3MessageService();

    Assertions.assertThrows(
        NullPointerException.class, () -> messageService.addMessageReceivedListener(null));
  }

  @Test
  public void handleMessageReceived_passesMessageToListeners() {
    MessageReceivedListener listenerA = Mockito.mock(MessageReceivedListener.class);
    MessageReceivedListener listenerB = Mockito.mock(MessageReceivedListener.class);
    MessageService messageService = new Mqtt3MessageService();
    messageService.addMessageReceivedListener(listenerA);
    messageService.addMessageReceivedListener(listenerB);
    Message message = new Message(MessageType.USER_MESSAGE, "uuid", "sender", "content");
    messageService.handleMessageReceived(message);

    // verify that listeners are passed the message.
    Mockito.verify(listenerA).onMessageReceived(message);
    Mockito.verify(listenerB).onMessageReceived(message);
  }

  @Test
  public void connect_hasInvalidSettings_throwsIllegalStateException() {
    MessageService messageService = new Mqtt3MessageService();

    // Mqtt3MessageService has settings which require values.
    Assertions.assertThrows(
        IllegalStateException.class, messageService::connect, "Not all settings are valid.");
  }

  @Test
  public void validateSettings_3settingsInvalid_returns3ValidationMessages() {
    MessageService messageService = new Mqtt3MessageService();
    List<String> validationMessages = messageService.validateSettings();
    Assertions.assertEquals(3, validationMessages.size());
  }
}
