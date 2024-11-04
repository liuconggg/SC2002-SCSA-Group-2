import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AuthenticationService {
 private static final String DEFAULT_PASSWORD = "password";
    private static Scanner scanner = new Scanner(System.in);
    private ArrayList<User> users;

    public AuthenticationService(ArrayList<User> users){
        this.users = users;
    }

    public User authenticate() throws IOException {
        System.out.println("=== Hospital Management System ===");

        System.out.print("Hospital ID: ");
        String id = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for(User user: users){
            if(user.getHospitalID().equals(id) && user.getPassword().equals(password)){
                if(user.getPassword().equals(DEFAULT_PASSWORD)){
                    changePassword(user);
                }
                return user;
            }
        }

        System.out.println("Login failed! Please try again!\n");

        return null;
    }


    private void changePassword(User user) throws IOException{
        System.out.println("=== Please change your password first! ===");
        while(true){
            System.out.print("New Password: ");
            String newPassword = scanner.nextLine();
            System.out.print("Confirm New Password: ");
            String confirmPassword = scanner.nextLine();
            if(newPassword.equals(confirmPassword)){
                user.setPassword(newPassword);
                CsvDB.saveUsers(users);
                System.out.println("Password changed successfully!");
                break;
            }else{
                System.out.println("Passwords do not match. Please try again.\n");
            }
        }

    }

}
