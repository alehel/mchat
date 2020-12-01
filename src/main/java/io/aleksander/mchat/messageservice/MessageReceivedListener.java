package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.model.Message;

@FunctionalInterface
public interface MessageReceivedListener {
  void onMessageReceived(Message message);
}
