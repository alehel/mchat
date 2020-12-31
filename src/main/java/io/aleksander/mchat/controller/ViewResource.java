package io.aleksander.mchat.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ViewResource {
  NEW_CONNECTION_FXML("/io/aleksander/mchat/view/new_connection.fxml"),
  PRIMARY_FXML("/io/aleksander/mchat/view/primary.fxml"),
  CHAT_THEME_LIGHT_CSS("/io/aleksander/mchat/styles/theme_light.css");

  @Getter
  private final String path;
}
