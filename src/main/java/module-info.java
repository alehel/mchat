module mchat {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  requires org.slf4j;
  requires org.tinylog.api.slf4j;
  requires org.apache.commons.lang3;
  requires org.eclipse.paho.client.mqttv3;
  requires static lombok;

  exports io.aleksander.mchat;

  opens io.aleksander.mchat.controller to javafx.fxml;
}