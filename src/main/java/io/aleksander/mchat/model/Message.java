package io.aleksander.mchat.model;

import java.io.Serializable;

@SuppressWarnings("squid:S1128")
public record Message(String uuid, String sender, String content, String timestamp) implements Serializable {
}
