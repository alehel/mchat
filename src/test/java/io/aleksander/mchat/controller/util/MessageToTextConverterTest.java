package io.aleksander.mchat.controller.util;

import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.MessageType;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class MessageToTextConverterTest {

  @Test
  void messageFromUserAndMessageFromOtherUserHaveDifferentTextColors() {
    String userUuid = UUID.randomUUID().toString();

    Message messageFromUser =
        new Message(MessageType.USER_MESSAGEG, userUuid, "","","");

    Message messageFromOtherUser =
        new Message(MessageType.USER_MESSAGEG, UUID.randomUUID().toString(), "", "", "");

    MessageToTextConverter converter = new MessageToTextConverter();
    Text textForMessageFromUser = converter.convert(userUuid, messageFromUser);
    Text textForMessageFromOtherUser = converter.convert(userUuid, messageFromOtherUser);

    Assertions.assertNotEquals(textForMessageFromUser.getFill(), textForMessageFromOtherUser.getFill());
  }

}