    package application_usetelegrambot.controller;

    import application_usetelegrambot.model.User;
    import application_usetelegrambot.model.UserModel;
    import application_usetelegrambot.view.UserView;

    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.List;
    import java.util.Scanner;

    public class UserController {

        private final UserModel model;
        private final UserView view;
        private final TelegramService telegramService;

        public UserController(UserModel model, UserView view, TelegramService telegramService) {
            this.model = model;
            this.view = view;
            this.telegramService = telegramService;
        }

        // Create a new user
        public void createUser() {
            String name = view.getInput("Enter Name: ");
            String email = view.getInput("Enter Email: ");
            User newUser = model.createUser(name, email);
            view.displayMessage("User created successfully!");

            // Send notification about the new user creation
            String message = "New user created:\nName: " + name + "\nEmail: " + email + "\nUUID: " + newUser.getUuid() + "\nID: " + newUser.getId();
            boolean success = telegramService.sendNotification(message);
            if (success) {
                view.displayMessage("Notification sent to Telegram.");
            } else {
                view.displayMessage("Failed to send notification to Telegram.");
            }
        }

        // Search for a user by UUID
        public void searchUserByUUID() {
            String uuid = view.getInput("Enter UUID: ");
            User user = model.findUserByUUID(uuid);
            view.displayUserDetails(user);
        }

        // Update user details by UUID
        public void updateUserByUUID() {
            String uuid = view.getInput("Enter UUID to update: ");
            User user = model.findUserByUUID(uuid); // Find the user by UUID
            if (user != null) {
                // Display current user information
                String currentName = user.getName();
                String currentEmail = user.getEmail();

                // Get new inputs
                String newName = view.getInput("Enter new Name (or press Enter to keep current): ");
                if (newName.isEmpty()) {
                    newName = currentName;  // If user presses Enter, keep the old name
                }

                String newEmail = view.getInput("Enter new Email (or press Enter to keep current): ");
                if (newEmail.isEmpty()) {
                    newEmail = currentEmail;  // If user presses Enter, keep the old email
                }

                // Get the deletion status
                boolean isDeleted = view.getBooleanInput("Mark user as deleted? (true/false): ");

                // Update user model
                boolean success = model.updateUser(uuid, newName, newEmail, isDeleted);
                if (success) {
                    view.displayMessage("User updated successfully!");

                    // Send notification about the update to Telegram
                    String message = "User updated:\nUUID: " + uuid + "\nName: " + newName + "\nEmail: " + newEmail + "\nStatus: " + (isDeleted ? "Deleted" : "Active");
                    telegramService.sendNotification(message);
                } else {
                    view.displayMessage("User not found.");
                }
            } else {
                view.displayMessage("User not found with UUID: " + uuid);
            }
        }

        // Delete a user by UUID
        public void deleteUserByUUID() {
            // Step 1: Get the UUID input from the user
            String uuid = view.getInput("Enter UUID to delete: ");

            // Step 2: Attempt to delete the user via the model
            boolean isDeleted = model.deleteUser(uuid);

            if (isDeleted) {
                // Step 3: Send notification about the deletion
                String message = String.format("User with UUID: %s has been deleted successfully.", uuid);
                boolean notificationSuccess = telegramService.sendNotification(message);

                if (notificationSuccess) {
                    view.displayMessage("User deleted and notification sent.");
                } else {
                    view.displayMessage("User deleted, but failed to send notification.");
                }
            } else {
                // Step 4: Notify if the user was not found
                view.displayMessage("User not found with UUID: " + uuid);
            }
        }
        // Display all users

        public void displayAllUsers() {
            // Get all users
            Collection<User> usersCollection = model.getAllUsers();

            // Convert to list for easy pagination
            List<User> users = new ArrayList<>(usersCollection);

            // Filter out deleted users
            users.removeIf(User::isDeleted);

            // If no active users are found
            if (users.isEmpty()) {
                view.displayMessage("No active users found.");
                return;
            }

            // Pagination logic
            int pageSize = 5;  // Display 5 users per page
            int totalPages = (int) Math.ceil(users.size() / (double) pageSize);
            int currentPage = 1;  // Start at the first page

            while (true) {
                // Calculate start and end index for the current page
                int startIndex = (currentPage - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, users.size());

                // Print table header
                System.out.println("+-----+--------------------------------------+---------------------+---------------------------+-----------+");
                System.out.println("| ID  | UUID                                 | Name                | Email                     | isDeleted   |");
                System.out.println("+-----+--------------------------------------+---------------------+---------------------------+-----------+");

                // Display users on the current page in a table format
                for (int i = startIndex; i < endIndex; i++) {
                    User user = users.get(i);
                    System.out.printf("| %-3d | %-36s | %-19s | %-25s | %-9s |\n",
                            user.getId(),
                            user.getUuid(),
                            user.getName(),
                            user.getEmail(),
                            (user.isDeleted() ? "True" : "False"));
                }

                // Print table footer
                System.out.println("+-----+--------------------------------------+---------------------+---------------------------+-----------+");

                // Show pagination details
                System.out.println("Page " + currentPage + " of " + totalPages);

                // Get user input for pagination (next, previous, exit)
                String choice = view.getInput("Enter command: (n)next, (p)previous, (e)exit");

                if ("n".equalsIgnoreCase(choice)) {
                    if (currentPage < totalPages) {
                        currentPage++;  // Move to next page
                    } else {
                        view.displayMessage("You are already on the last page.");
                    }
                } else if ("p".equalsIgnoreCase(choice)) {
                    if (currentPage > 1) {
                        currentPage--;  // Move to previous page
                    } else {
                        view.displayMessage("You are already on the first page.");
                    }
                } else if ("e".equalsIgnoreCase(choice)) {
                    break;  // Exit pagination loop
                } else {
                    view.displayMessage("Invalid command. Please enter 'n', 'p', or 'e'.");
                }
            }
        }
    }
