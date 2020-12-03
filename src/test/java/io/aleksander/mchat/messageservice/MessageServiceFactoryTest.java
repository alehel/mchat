package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.messageservice.mqtt3.Mqtt3MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MessageServiceFactoryTest {

  @Test
  void nullCausesIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      MessageServiceFactory.getMessageService(null);
    });
  }

  @Test
  void returnsCorrectType() {
    MessageService messageService = MessageServiceFactory
        .getMessageService(MessageServiceType.MQQT3);
    Assertions.assertTrue(messageService instanceof Mqtt3MessageService);
  }
}