package io.aleksander.mchat.model;

import java.util.Objects;
import java.io.Serializable;

public record Message(MessageType messageType, String uuid, String sender, String content, String timestamp) implements Serializable {
  public Message {
    Objects.requireNonNull(messageType);
    Objects.requireNonNull(uuid);
    Objects.requireNonNull(sender);
    Objects.requireNonNull(content);
    Objects.requireNonNull(timestamp);
  }
}
