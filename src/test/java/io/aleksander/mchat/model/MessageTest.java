package io.aleksander.mchat.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageTest {

  @Test
  public void messageTypeMayNotBeNull() {
    Assertions.assertThrows(NullPointerException.class, () -> new Message(null, "", "", ""));
  }

  @Test
  public void uuidMayNotBeNull() {
    Assertions.assertThrows(
        NullPointerException.class, () -> new Message(MessageType.USER_MESSAGE, null, "", ""));
  }

  @Test
  public void senderMayNotBeNull() {
    Assertions.assertThrows(
        NullPointerException.class, () -> new Message(MessageType.USER_MESSAGE, "", null, ""));
  }

  @Test
  public void contentMayNotBeNull() {
    Assertions.assertThrows(
        NullPointerException.class, () -> new Message(MessageType.USER_MESSAGE, "", "", null));
  }
}
