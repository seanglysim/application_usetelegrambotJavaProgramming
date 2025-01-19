package application_usetelegrambot;

import application_usetelegrambot.controller.TelegramService;
import application_usetelegrambot.controller.UserController;
import application_usetelegrambot.model.UserModel;
import application_usetelegrambot.view.UserView;

import java.util.Scanner;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        UserModel model = new UserModel();
        UserView view = new UserView();
        TelegramService telegramService = new TelegramService();
        UserController controller = new UserController(model, view, telegramService);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nUser Management Console:");
            System.out.println("1. Create User");
            System.out.println("2. Search User by UUID");
            System.out.println("3. Update User by UUID");
            System.out.println("4. Delete User by UUID");
            System.out.println("5. Display All Users");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> controller.createUser();
                case 2 -> controller.searchUserByUUID();
                case 3 -> controller.updateUserByUUID();
                case 4 -> controller.deleteUserByUUID();
                case 5 -> controller.displayAllUsers();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}