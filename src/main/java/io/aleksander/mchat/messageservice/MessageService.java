package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.model.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;

public abstract class MessageService {

  private final List<MessageReceivedListener> messageReceivedListeners = new ArrayList<>();
  @Getter private final String clientId = UUID.randomUUID().toString();
  @Getter private final MessageServiceType messageServiceType;

  protected MessageService(MessageServiceType messageServiceType) {
    this.messageServiceType = messageServiceType;
  }

  public abstract void connect();

  public abstract void sendMessage(Message message);

  public void addMessageReceivedListener(@NonNull MessageReceivedListener messageReceivedListener) {
    if (messageReceivedListeners.contains(messageReceivedListener)) {
      throw new IllegalArgumentException("specified MessageReceivedListener is already attached.");
    }

    messageReceivedListeners.add(messageReceivedListener);
  }

  protected void handleMessageReceived(Message message) {
    messageReceivedListeners.forEach(
        messageReceivedListener -> messageReceivedListener.onMessageReceived(message));
  }

  public void removeMessageReceivedListener(MessageReceivedListener messageReceivedListener) {
    if (!messageReceivedListeners.contains(messageReceivedListener)) {
      throw new IllegalArgumentException("Specified MessageReceivedListener was not registered.");
    }

    messageReceivedListeners.remove(messageReceivedListener);
  }

  public abstract boolean isConnected();
}
