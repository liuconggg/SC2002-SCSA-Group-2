package com.ntu.hns;

import com.ntu.hns.manager.application.ContextManager;
import com.ntu.hns.spring.AppConfig;
import com.ntu.hns.model.users.User;
import com.ntu.hns.model.users.Patient;
import com.ntu.hns.model.users.Doctor;
import com.ntu.hns.model.users.Pharmacist;
import com.ntu.hns.model.users.Administrator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static final String[] sessionTimings = {
            "09:00 - 10:00",
            "10:00 - 11:00",
            "11:00 - 12:00",
            "12:00 - 13:00",
            "13:00 - 14:00",
            "14:00 - 15:00",
            "15:00 - 16:00",
            "16:00 - 17:00",
            "17:00 - 18:00"
    };

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        AuthenticationService authenticationService = context.getBean(AuthenticationService.class);
        ContextManager contextManager = context.getBean(ContextManager.class);

        // Load all CSV data
        User user = null;
        while (true) {
            if (user != null) {
                if (user instanceof Patient) {
                    contextManager.beginPatient((Patient) user);
                } else if (user instanceof Doctor) {
                    contextManager.beginDoctor((Doctor) user);
                } else if (user instanceof Pharmacist) {
                    contextManager.beginPharmacist((Pharmacist) user);
                } else if (user instanceof Administrator) {
                    contextManager.beginAdministrator((Administrator) user);
                }
            } else {
                user = authenticationService.authenticate();
            }

        }
    }
}
