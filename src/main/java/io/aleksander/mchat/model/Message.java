package io.aleksander.mchat.model;

import java.util.Objects;
import java.io.Serializable;

public record Message(String uuid, String sender, String content, String timestamp) implements Serializable {
  public Message {
    Objects.requireNonNull(uuid);
    Objects.requireNonNull(sender);
    Objects.requireNonNull(content);
    Objects.requireNonNull(timestamp);
  }
}
