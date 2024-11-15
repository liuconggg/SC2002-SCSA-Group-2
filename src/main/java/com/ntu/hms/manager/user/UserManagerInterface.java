package com.ntu.hms.manager.user;

/**
 * UserManagerInterface outlines the essential operations for managing users
 * within the system, including adding, displaying, updating, and deleting users.
 */
public interface UserManagerInterface {

  void addUser();

  void showUsers();

  void updateUser();

  void deleteUser();
}
