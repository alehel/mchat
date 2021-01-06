package io.aleksander.mchat.messageservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageServiceFactoryTest {

  @Test
  public void nullCausesIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      MessageServiceFactory.getMessageService(null);
    });
  }

  @Test
  public void returnsCorrectType() {
    MessageService messageService = MessageServiceFactory
        .getMessageService(MessageServiceType.MQQT3);
    Assertions.assertTrue(messageService instanceof Mqtt3MessageService);
  }
}