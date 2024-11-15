package com.ntu.hms.authentication;

import static com.ntu.hms.TestManager.provideInput;
import static com.ntu.hms.TestManager.startApplication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The type Authentication actions test.
 * This class contains test cases for authentication actions,
 * such as first-time login and password changes, and login with incorrect credentials.
 */
public class AuthenticationActionsTest {

  /**
   * Test first time login and password change.
   * This test case simulates a user's first-time login and subsequent password change.
   */
  @Test
  @DisplayName("Test Case 25: First-Time Login and Password Change")
  public void testFirstTimeLoginAndPasswordChange() {
    provideInput("login\nP0005\npassword\n123\n123\n9\nexit\n");
    startApplication();
  }

  /**
   * Test login with incorrect credentials.
   * This test case simulates a login attempt with incorrect credentials.
   */
  @Test
  @DisplayName("Test Case 26: Login with Incorrect Credentials")
  public void testLoginWithIncorrectCredentials() {
    provideInput("login\nA0001\nqwerty\nexit\n");
    startApplication();
  }
}