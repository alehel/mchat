package io.aleksander.mchat.templateengine;

import io.aleksander.mchat.model.Message;
import java.util.List;
import java.util.stream.Collectors;

public class ChatTemplateEngine {

  public String generateChatHtml(String title, List<Message> chatMessages) {
    String head = "<head><title>" + title + "</title></head>";
    String body = "<body>" + convertMessagesToHtml(chatMessages) + "</body>";
    return "<html>" + head + body + "</html>";
  }

  private String convertMessagesToHtml(List<Message> chatMessages) {
    return chatMessages.stream().map(this::convertMessageToHtml).collect(Collectors.joining());
  }

  private String convertMessageToHtml(Message chatMessage) {
    return "<p>" + chatMessage.sender() + ": " + chatMessage.content() + "</p>";
  }
}
