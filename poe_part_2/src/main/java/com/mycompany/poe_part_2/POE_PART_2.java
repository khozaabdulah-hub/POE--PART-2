package com.mycompany.poe_part_2;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// Message class as required
class Message {
    private static int messageCounter = 0;
    private static int totalMessagesSent = 0;
    
    private String messageID;
    private int numMessagesSent;
    private String recipient;
    private String messageText;
    private String messageHash;
    private boolean sent;
    private boolean stored;
    
    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.numMessagesSent = ++messageCounter;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        this.sent = false;
        this.stored = false;
    }
    
    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }
    
    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }
    
    public int checkRecipientCell() {
        if (recipient == null || recipient.isEmpty()) {
            return -1;
        }
        
        // Remove any spaces or dashes
        String cleanNumber = recipient.replaceAll("[\\s-]", "");
        
        // Check if it starts with RSA international code +27 and has exactly 9 numbers after
        if (cleanNumber.startsWith("+27") && cleanNumber.length() == 12) {
            return 1; // Success for RSA numbers with +27 (9 numbers after)
        } else if (cleanNumber.startsWith("27") && cleanNumber.length() == 11) {
            return 1; // Success for RSA numbers with 27 (9 numbers after)
        } else if (cleanNumber.startsWith("0") && cleanNumber.length() == 10) {
            return 1; // Success for local format 0XXXXXXXXX (9 numbers after)
        }
        
        return -1; // Failure
    }
    
    public String createMessageHash() {
        String firstTwoID = messageID.substring(0, 2);
        String[] words = messageText.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        return (firstTwoID + ":" + numMessagesSent + ":" + firstWord + lastWord).toUpperCase();
    }
    
    public String sentMessage() {
        String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose an action for the message:",
                "Message Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        
        switch (choice) {
            case 0: // Send
                this.sent = true;
                totalMessagesSent++;
                return "Message successfully sent.";
            case 1: // Disregard
                return "Press 0 to delete message.";
            case 2: // Store
                this.stored = true;
                return "Message successfully stored.";
            default:
                return "Action cancelled.";
        }
    }
    
    public String printMessageIt() {
        if (sent) {
            return "MessageID: " + messageID + 
                   "\nMessage Hash: " + messageHash + 
                   "\nRecipient: " + recipient + 
                   "\nMessage: " + messageText;
        }
        return "Message not sent yet.";
    }
    
    public static int returnTotalMessageIt() {
        return totalMessagesSent;
    }
    
    public JSONObject toJSON() {
        // Using org.json.JSONObject for JSON serialization
        // Library: org.json (https://github.com/stleary/JSON-java)
        JSONObject json = new JSONObject();  // org.json.JSONObject
        json.put("messageID", messageID);
        json.put("numMessagesSent", numMessagesSent);
        json.put("recipient", recipient);
        json.put("messageText", messageText);
        json.put("messageHash", messageHash);
        json.put("sent", sent);
        json.put("stored", stored);
        return json;
    }
    
    // Getters
    public String getMessageID() { return messageID; }
    public int getNumMessagesSent() { return numMessagesSent; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public boolean isSent() { return sent; }
    public boolean isStored() { return stored; }
}

// Main QuickChat application
public class POE_PART_2 {
    private List<Message> messages = new ArrayList<>();
    private String registeredUsername = null;
    private String registeredPassword = null;
    
    public static void main(String[] args) {
        POE_PART_2 app = new POE_PART_2();
        app.run();
    }
    
    public void run() {
        boolean exit = false;
        
        while (!exit) {
            String choice = JOptionPane.showInputDialog(
                "Welcome to QuickChat!\n\n" +
                "Choose an option:\n" +
                "1. REGISTER\n" +
                "2. LOGIN\n" +
                "3. EXIT"
            );
            
            switch (choice) {
                case "1":
                    register();
                    break;
                case "2":
                    if (login()) {
                        showQuickChatMenu();
                    } else {
                        JOptionPane.showMessageDialog(null, "Login failed! Please check your credentials.");
                    }
                    break;
                case "3":
                    exit = true;
                    JOptionPane.showMessageDialog(null, "Thank you for using QuickChat!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
            }
        }
    }
    
    private void register() {
        String username;
        do {
            username = JOptionPane.showInputDialog("Enter username (must contain underscore and be 5 characters long):");
            if (username == null) return;
        } while (!checkUserName(username));
        
        String password;
        do {
            password = JOptionPane.showInputDialog("Enter password (min 8 characters, with capital, number, special char):");
            if (password == null) return;
        } while (!validatePassword(password));
        
        registeredUsername = username;
        registeredPassword = password;
        JOptionPane.showMessageDialog(null, "Registration successful!");
    }
    
    private boolean login() {
        if (registeredUsername == null) {
            JOptionPane.showMessageDialog(null, "Please register first!");
            return false;
        }
        
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");
        
        return username != null && username.equals(registeredUsername) && 
               password != null && password.equals(registeredPassword);
    }
    
    private void showQuickChatMenu() {
        boolean logout = false;
        
        while (!logout) {
            String choice = JOptionPane.showInputDialog(
                "Welcome to QuickChat!\n\n" +
                "Choose an option:\n" +
                "1. Send Messages\n" +
                "2. Show recently sent messages\n" +
                "3. Quit"
            );
            
            switch (choice) {
                case "1":
                    sendMessages();
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, "Coming Soon.");
                    break;
                case "3":
                    logout = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
            }
        }
    }
    
    private void sendMessages() {
        String numMessagesStr = JOptionPane.showInputDialog("How many messages do you wish to enter?");
        if (numMessagesStr == null) return;
        
        try {
            int numMessages = Integer.parseInt(numMessagesStr);
            
            for (int i = 0; i < numMessages; i++) {
                boolean validMessage = false;
                
                while (!validMessage) {
                    // Get recipient number
                    String recipient;
                    do {
                        recipient = JOptionPane.showInputDialog("Enter recipient phone number (RSA +27 format):");
                        if (recipient == null) return;
                        if (!isValidPhoneNumber(recipient)) {
                            JOptionPane.showMessageDialog(null, 
                                "Invalid RSA phone number format.\n" +
                                "Accepted formats:\n" +
                                "+27XXXXXXXXX (12 digits total - 9 numbers after +27)\n" +
                                "0XXXXXXXXX (10 digits total - 9 numbers after 0)");
                        }
                    } while (!isValidPhoneNumber(recipient));
                    
                    // Get message text
                    String messageText;
                    do {
                        messageText = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
                        if (messageText == null) return;
                        
                        if (messageText.length() > 250) {
                            int excess = messageText.length() - 250;
                            JOptionPane.showMessageDialog(null, 
                                "Message exceeds 250 characters by " + excess + ", please reduce size.");
                        }
                    } while (messageText.length() > 250);
                    
                    // Create message
                    Message message = new Message(recipient, messageText);
                    
                    // Validate message
                    if (message.checkMessageID() && message.checkRecipientCell() == 1) {
                        String actionResult = message.sentMessage();
                        JOptionPane.showMessageDialog(null, actionResult);
                        
                        if (message.isSent()) {
                            messages.add(message);
                            // Display message details
                            JOptionPane.showMessageDialog(null, message.printMessageIt());
                            
                            // Store in JSON file
                            storeMessageToJSON(message);
                        }
                        
                        validMessage = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Message validation failed. Please try again.");
                    }
                }
            }
            
            // Show total messages sent
            JOptionPane.showMessageDialog(null, 
                "Total messages sent in this session: " + Message.returnTotalMessageIt());
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number entered.");
        }
    }
    
    private void storeMessageToJSON(Message message) {
        try {
            // Using org.json library for JSON file operations
            // Library: org.json (https://github.com/stleary/JSON-java)
            JSONArray jsonArray;  // org.json.JSONArray
            String filePath = "messages.json";
            
            // Read existing messages if file exists using org.json
            if (Files.exists(Paths.get(filePath))) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                jsonArray = new JSONArray(content);  // org.json.JSONArray
            } else {
                jsonArray = new JSONArray();  // org.json.JSONArray
            }
            
            // Add new message using org.json
            jsonArray.put(message.toJSON());  // org.json.JSONObject
            
            // Write back to file with pretty printing
            try (FileWriter file = new FileWriter(filePath)) {
                file.write(jsonArray.toString(4)); // org.json pretty print with indentation
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage());
        }
    }
    
    // Validation methods from original code
    public static boolean checkUserName(String username) {
        return username != null && username.length() == 5 && username.contains("_");
    }
    
    // FIXED PASSWORD VALIDATION - Added lowercase check
    public static boolean validatePassword(String password) {
        return password != null && 
               password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&      // At least one uppercase
               password.matches(".*[a-z].*") &&      // At least one lowercase - THIS WAS ADDED!
               password.matches(".*\\d.*") &&        // At least one digit
               password.matches(".*[^A-Za-z0-9].*"); // At least one special character
    }
    
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return false;
        String cleanNumber = phoneNumber.replaceAll("[\\s-]", "");
        
        // RSA South Africa phone number validation
        // Format 1: +27XXXXXXXXX (12 digits total = +27 + 9 numbers)
        // Format 2: 27XXXXXXXXX (11 digits total = 27 + 9 numbers)
        // Format 3: 0XXXXXXXXX (10 digits total = 0 + 9 numbers)
        return cleanNumber.matches("^\\+27[0-9]{9}$") ||    // +27 followed by exactly 9 digits
               cleanNumber.matches("^27[0-9]{9}$") ||       // 27 followed by exactly 9 digits  
               cleanNumber.matches("^0[0-9]{9}$");          // 0 followed by exactly 9 digits
    }
}