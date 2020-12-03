package io.aleksander.mchat.model;

@FunctionalInterface
public interface SettingValidator {
  boolean settingIsValid(String setting);
}
