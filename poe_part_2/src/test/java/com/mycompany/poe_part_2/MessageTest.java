package com.mycompany.poe_part_2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    private Message message;
    
    @BeforeEach
    public void setUp() {
        message = new Message("+27831234567", "Hello this is a test message");
    }
    
    @Test
    public void testMessageCreation() {
        assertNotNull(message.getMessageID());
        assertEquals("+27831234567", message.getRecipient());
        assertEquals("Hello this is a test message", message.getMessageText());
        assertFalse(message.isSent());
        assertFalse(message.isStored());
    }
    
    @Test
    public void testCheckMessageID() {
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }
    
    @Test
    public void testCheckRecipientCell() {
        // Test valid RSA numbers
        Message m1 = new Message("+27831234567", "test");
        assertEquals(1, m1.checkRecipientCell());
        
        Message m2 = new Message("0831234567", "test");
        assertEquals(1, m2.checkRecipientCell());
        
        Message m3 = new Message("27831234567", "test");
        assertEquals(1, m3.checkRecipientCell());
        
        // Test invalid numbers
        Message m4 = new Message("12345", "test");
        assertEquals(-1, m4.checkRecipientCell());
        
        Message m5 = new Message("", "test");
        assertEquals(-1, m5.checkRecipientCell());
    }
    
    @Test
    public void testCreateMessageHash() {
        String hash = message.createMessageHash();
        assertNotNull(hash);
        assertTrue(hash.contains(":"));
        assertTrue(hash.matches("^[0-9]{2}:[0-9]+:.*$"));
    }
    
    @Test
    public void testPrintMessageIt() {
        assertEquals("Message not sent yet.", message.printMessageIt());
    }
    
    @Test
    public void testToJSON() {
        org.json.JSONObject json = message.toJSON();
        assertNotNull(json);
        assertEquals(message.getMessageID(), json.getString("messageID"));
        assertEquals(message.getRecipient(), json.getString("recipient"));
        assertEquals(message.getMessageText(), json.getString("messageText"));
        assertEquals(message.getMessageHash(), json.getString("messageHash"));
    }
}