package io.aleksander.mchat.model;

import java.io.Serializable;

public record Message(String uuid, String sender, String content, String timestamp) implements Serializable {
}
