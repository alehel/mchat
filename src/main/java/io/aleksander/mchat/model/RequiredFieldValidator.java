package io.aleksander.mchat.model;

import org.apache.commons.lang3.StringUtils;

public class RequiredFieldValidator implements SettingValidator {

  @Override
  public boolean settingIsValid(String setting) {
    return !StringUtils.isBlank(setting);
  }
}
