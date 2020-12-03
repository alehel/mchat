package io.aleksander.mchat.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RequiredFieldValidatorTest {

  private final RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();

  @Test
  void emptyStringIsNotValid() {
    Assertions.assertFalse(requiredFieldValidator.settingIsValid(null));
    Assertions.assertFalse(requiredFieldValidator.settingIsValid(""));
    Assertions.assertFalse(requiredFieldValidator.settingIsValid("     "));
  }

  @Test
  void regularStringIsValid() {
    Assertions.assertTrue(requiredFieldValidator.settingIsValid("A String"));
  }


}