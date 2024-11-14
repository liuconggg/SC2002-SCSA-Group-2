package com.ntu.hms.authentication;

import static com.ntu.hms.TestManager.provideInput;
import static com.ntu.hms.TestManager.startApplication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthenticationActionsTest {

  @Test
  @DisplayName("Test Case 25: First-Time Login and Password Change")
  public void testFirstTimeLoginAndPasswordChange() {
    provideInput("login\nP0005\npassword\n123\n123\n9\nexit\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 26: Login with Incorrect Credentials")
  public void testLoginWithIncorrectCredentials() {
    provideInput("login\nA0001\nqwerty\nexit\n");
    startApplication();
  }
}
