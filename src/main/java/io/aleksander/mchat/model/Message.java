package io.aleksander.mchat.model;

import java.io.Serializable;
import java.util.Objects;

public record Message(MessageType messageType, String uuid, String sender, String content) implements Serializable {

  public Message {
    Objects.requireNonNull(messageType);
    Objects.requireNonNull(uuid);
    Objects.requireNonNull(sender);
    Objects.requireNonNull(content);
  }
}
