package io.aleksander.mchat.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MessageTest {

  @Test
  void uuidMayNotBeNull() {
    Assertions.assertThrows(NullPointerException.class, () -> new Message(null, "", "", ""));
  }

  @Test
  void senderMayNotBeNull() {
    Assertions.assertThrows(NullPointerException.class, () -> new Message("", null, "", ""));
  }

  @Test
  void contentMayNotBeNull() {
    Assertions.assertThrows(NullPointerException.class, () -> new Message("", "", null, ""));
  }

  @Test
  void timeStampMayNotbeNull() {
    Assertions.assertThrows(NullPointerException.class, () -> new Message("","","",null));
  }
}