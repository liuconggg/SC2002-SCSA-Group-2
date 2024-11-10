package com.ntu.hns.manager.application;

import com.ntu.hns.AuthenticationService;
import com.ntu.hns.model.users.*;
import com.ntu.hns.util.ScannerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ApplicationManager {
  private final AnnotationConfigApplicationContext applicationContext;
  private final AuthenticationService authenticationService;
  private final ContextManager contextManager;
  private final ScannerWrapper scanner;

  @Autowired
  public ApplicationManager(
      AnnotationConfigApplicationContext applicationContext,
      AuthenticationService authenticationService,
      ContextManager contextManager,
      ScannerWrapper scanner) {
    this.applicationContext = applicationContext;
    this.authenticationService = authenticationService;
    this.contextManager = contextManager;
    this.scanner = scanner;
  }

  public void start() {
    // Load all CSV data
    User user = null;

    while (true) {
      if (user == null) {
        user = authenticationService.authenticate();
        continue;
      }

      handleUserContext(user);
      user = null;
    }
  }

  private void handleUserContext(User user) {
    if (user instanceof Patient) {
      contextManager.beginPatient((Patient) user);
    } else if (user instanceof Doctor) {
      contextManager.beginDoctor((Doctor) user);
    } else if (user instanceof Pharmacist) {
      contextManager.beginPharmacist((Pharmacist) user);
    } else if (user instanceof Administrator) {
      contextManager.beginAdministrator((Administrator) user);
    }
  }

  public void stop() {
    applicationContext.close();
    scanner.close();
  }
}
