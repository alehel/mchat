package io.aleksander.mchat.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

public class SettingTest {

  @Test
  public void valueIsValid_shouldCallSettingValidator() {
    // prepare test data
    SettingValidator settingValidator = mock(SettingValidator.class);
    Setting setting = new Setting("id", "name", settingValidator);
    String value = "A value!";
    setting.setValue(value);

    // verify that the SettingValidator is called by Setting object during validation.
    setting.valueIsValid();
    verify(settingValidator).settingIsValid(value);
  }
}