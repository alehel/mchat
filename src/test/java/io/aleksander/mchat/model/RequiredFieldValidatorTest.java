package io.aleksander.mchat.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequiredFieldValidatorTest {

  private final RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();

  @Test
  public void emptyStringIsNotValid() {
    Assertions.assertFalse(requiredFieldValidator.settingIsValid(null));
    Assertions.assertFalse(requiredFieldValidator.settingIsValid(""));
    Assertions.assertFalse(requiredFieldValidator.settingIsValid("     "));
  }

  @Test
  public void regularStringIsValid() {
    Assertions.assertTrue(requiredFieldValidator.settingIsValid("A String"));
  }


}