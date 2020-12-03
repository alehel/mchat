package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.Setting;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MessageService {

  private final List<MessageReceivedListener> messageReceivedListeners = new ArrayList<>();
  @Getter private final String clientId = UUID.randomUUID().toString();
  @Getter private final MessageServiceType messageServiceType;
  @Getter private final List<Message> chatHistory = new ArrayList<>();

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
    log.info("Got message!");
    chatHistory.add(message);
    messageReceivedListeners.forEach(
        messageReceivedListener -> messageReceivedListener.onMessageReceived(message));
  }

  protected boolean allSettingsAreValid() {
    for(Setting setting : getSettings().values()) {
      if (!setting.valueIsValid()) {
        log.warn("Value " + setting.getValue() + " not valid for setting " + setting.getId());
        return false;
      }
    }

    return true;
  }

  public void removeMessageReceivedListener(MessageReceivedListener messageReceivedListener) {
    if (!messageReceivedListeners.contains(messageReceivedListener)) {
      throw new IllegalArgumentException("Specified MessageReceivedListener was not registered.");
    }

    messageReceivedListeners.remove(messageReceivedListener);
  }

  public abstract boolean isConnected();

  public abstract Map<String, Setting> getSettings();

  public abstract void setSettings(Map<String, Setting> settings);
}
