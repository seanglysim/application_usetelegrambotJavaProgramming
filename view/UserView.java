package application_usetelegrambot.view;

import application_usetelegrambot.model.User;

import java.util.Collection;
import java.util.List;

public class UserView {

    // Display User details in a table-like format
    public void displayUserDetails(User user) {
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("User not found.");
        }
    }

    // Display message to the user
    public void displayMessage(String message) {
        System.out.println(message);
    }

    // Get user input (for creating/updating users)
    public String getInput(String prompt) {
        System.out.print(prompt);
        return new java.util.Scanner(System.in).nextLine();
    }

    // Get boolean input for deleted status
    public boolean getBooleanInput(String prompt) {
        System.out.print(prompt);
        return new java.util.Scanner(System.in).nextBoolean();
    }
}