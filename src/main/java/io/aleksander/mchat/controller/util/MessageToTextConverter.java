package io.aleksander.mchat.controller.util;

import io.aleksander.mchat.model.Message;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageToTextConverter {

  public Text convert(String userUuid, Message message) {
    Text text = new Text(message.sender() + "\n" + message.content() + "\n\n");
    if(messageBelongsToUser(userUuid, message)) {
      text.setFill(Color.GREEN);
    } else {
      text.setFill(Color.BLACK);
    }

    return text;
  }

  private boolean messageBelongsToUser(String userUuid, Message message) {
    return message.uuid().equals(userUuid);
  }
}
