package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.MessageType;
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
}
