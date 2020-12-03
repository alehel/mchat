package io.aleksander.mchat.messageservice;

import io.aleksander.mchat.messageservice.mqtt3.Mqtt3MessageService;

public class MessageServiceFactory {
  private MessageServiceFactory() {}

  public static MessageService getMessageService(MessageServiceType type) {
    if (type == MessageServiceType.MQQT3) {
      return new Mqtt3MessageService();
    } else {
      throw new IllegalArgumentException("No such MessageService type.");
    }
  }

}
