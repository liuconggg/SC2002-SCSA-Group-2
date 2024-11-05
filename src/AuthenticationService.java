
import java.io.IOException;
import java.util.ArrayList;
import java.io.Console;

public class AuthenticationService {

    private static final String DEFAULT_PASSWORD = "password";
    private static final Console console = System.console();

    public User authenticate(ArrayList<User> users) throws IOException {

        if(console == null) {
            System.out.println("No console available. Please run in a terminal. ");
            return null;
        }
        System.out.println("=== Hospital Management System ===");

        String id = console.readLine("Hospital ID: ");        
        char[] passwordArray = console.readPassword("Password: ");

        String password = new String(passwordArray);

        for (User user : users) {
            if (user.getHospitalID().equals(id) && user.getPassword().equals(password)) {
                if (user.getPassword().equals(DEFAULT_PASSWORD)) {
                    changePassword(user, users);
                }
                return user;
            }
        }

        System.out.println("Login failed! Please try again!\n");

        return null;
    }

    private void changePassword(User user, ArrayList<User> users) throws IOException {
        System.out.println("=== Please change your password first! ===");
        while (true) {
            char[] newPasswordArray = console.readPassword("New Password: ");
            String newPassword = new String(newPasswordArray);
            char[] confirmPasswordArray = console.readPassword("Confirm New Password: ");
            String confirmPassword = new String(confirmPasswordArray);

            if (newPassword.equals(confirmPassword)) {
                user.setPassword(newPassword);
                CsvDB.saveUsers(users);
                System.out.println("Password changed successfully!");
                break;
            } else {
                System.out.println("Passwords do not match. Please try again.\n");
            }
        }

    }

}
