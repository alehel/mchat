package io.aleksander.mchat;

import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.MessageType;
import java.util.UUID;

public class TestUtil {
  public static final String VALID_MQTT_BROKER_URL = "tcp://mqtt.eclipse.org:1883";
  public static final String VALID_TOPIC_NAME = "VALID_TOPIC_NAME";
  public static final Message VALID_MESSAGE =
      new Message(MessageType.USER_MESSAGE, UUID.randomUUID().toString(), "Aleksander", "Test");
}
