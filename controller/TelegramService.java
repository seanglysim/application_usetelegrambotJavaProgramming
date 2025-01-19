package application_usetelegrambot.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TelegramService {

    private static final String BOT_TOKEN = "YOUR TOKEN TELEGRAM"; // Replace with your Telegram bot token
    private static final String CHAT_ID = "1256440162"; // Replace with your Telegram chat ID

    // Send notification to Telegram
    public static boolean sendNotification(String message) {
        int retries = 3;
        while (retries > 0) {
            try {
                String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
                String apiUrl = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=" + encodedMessage;
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return true; // Notification sent successfully
                } else {
                    System.out.println("Failed to send notification. Response code: " + responseCode);
                }
            } catch (IOException e) {
                System.out.println("Error sending Telegram notification: " + e.getMessage());
            }
            retries--;
            if (retries > 0) {
                System.out.println("Retrying... (" + retries + " attempts left)");
            }
        }
        return false; // Failed after retries
    }
}
