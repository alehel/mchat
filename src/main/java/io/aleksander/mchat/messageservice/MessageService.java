package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.Setting;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MessageService {

  private final List<MessageReceivedListener> messageReceivedListeners = new ArrayList<>();
  @Getter private final String clientId = UUID.randomUUID().toString();
  @Getter private final MessageServiceType messageServiceType;
  @Getter private final List<Message> chatHistory = new ArrayList<>();
  @Getter @Setter(AccessLevel.PROTECTED) private List<Setting> settings = new ArrayList<>();

  protected MessageService(MessageServiceType messageServiceType) {
    this.messageServiceType = messageServiceType;
  }

  public abstract void connect();

  public abstract void sendMessage(Message message);

  public abstract String getDisplayString();

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
    for(Setting setting : settings) {
      if (!setting.valueIsValid()) {
        log.warn("Value " + setting.getValue() + " not valid for setting " + setting.getId());
        return false;
      }
    }

    return true;
  }

  public abstract String getUserName();
}
