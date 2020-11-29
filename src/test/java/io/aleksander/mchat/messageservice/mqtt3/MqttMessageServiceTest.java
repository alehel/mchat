package io.aleksander.mchat.messageservice.mqtt3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MqttMessageServiceTest {

  @Test
  void serverUrlCannotBeNull() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new MqttMessageService(null);
    });
  }
}