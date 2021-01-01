package io.aleksander.mchat.templateengine;

import io.aleksander.mchat.model.Message;
import io.aleksander.mchat.model.MessageType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChatTemplateEngineTest {

  @Test
  public void generatedHtmlContainsChatRoomAsTitle() {
    ChatTemplateEngine templateEngine = new ChatTemplateEngine();
    Message message =
        new Message(
            MessageType.USER_MESSAGE, UUID.randomUUID().toString(), "Aleksander", "Hello friend");
    List<Message> chatHistory = new ArrayList<>(Collections.singletonList(message));
    String chatRoomName = "Room 1";
    String chatHtml = templateEngine.generateChatHtml(chatRoomName, chatHistory);

    String expectedTitleString = "<title>" + chatRoomName + "</title>";
    Assertions.assertTrue(chatHtml.contains(expectedTitleString));
  }

  @Test
  public void generatedHtmlContainsChatMessageContent() {
    ChatTemplateEngine templateEngine = new ChatTemplateEngine();
    Message message =
        new Message(
            MessageType.USER_MESSAGE, UUID.randomUUID().toString(), "Aleksander", "Hello friend");
    List<Message> chatHistory = new ArrayList<>(Collections.singletonList(message));
    String chatRoomName = "Room 1";
    String chatHtml = templateEngine.generateChatHtml(chatRoomName, chatHistory);

    String expectedBodyString = "<body><p>Aleksander: Hello friend</p></body>";
    Assertions.assertTrue(chatHtml.contains(expectedBodyString));
  }
}
