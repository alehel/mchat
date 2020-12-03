package io.aleksander.mchat.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Setting {
  @NonNull
  @Getter
  private final String id;

  @NonNull
  @Getter
  private final String name;

  @NonNull
  @Getter
  private final boolean required;

  @Getter
  @Setter
  private String value;

  @NonNull
  @Getter
  private final SettingValidator settingValidator;

  public boolean valueIsValid() {
    return settingValidator.settingIsValid(value);
  }
}
