package com.ntu.hms.enums;

/**
 * Enum representing the different environments in which an application can operate.
 *
 * <ul>
 *   <li><b>PROD</b> - Represents the production environment.
 *   <li><b>DEV</b> - Represents the development environment.
 * </ul>
 *
 * This enum is used to control application behavior based on the environment context. The behavior
 * of various methods changes based on whether the environment is set to PROD or DEV.
 */
public enum Environment {
  PROD,
  DEV
}
