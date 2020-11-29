package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.model.Message;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractMessageService {
  private final List<MessageReceivedListener> messageReceivedListeners = new ArrayList<>();
  @Getter
  private final String clientId = UUID.randomUUID().toString();

  public abstract void connect();

  public abstract void sendMessage(String message);

  public void addMessageReceivedListener(@NonNull MessageReceivedListener messageReceivedListener) {
    if (messageReceivedListeners.contains(messageReceivedListener)) {
      throw new IllegalArgumentException("specified MessageReceivedListener is already attached.");
    }

    messageReceivedListeners.add(messageReceivedListener);

  }

  protected void handleMessageReceived(Message message) {
    messageReceivedListeners.forEach(messageReceivedListener -> messageReceivedListener.onMessageReceived(message));
  }

}
