package com.mycompany.poe_part_2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class MessageIntegrationTest {
    
    @TempDir
    Path tempDir;
    
    @Test
    public void testMessageLifecycle() {
        Message message = new Message("+27831234567", "Integration test message");
        
        assertTrue(message.checkMessageID());
        assertEquals(1, message.checkRecipientCell());
        assertNotNull(message.createMessageHash());
        
        org.json.JSONObject json = message.toJSON();
        assertNotNull(json);
        assertEquals(message.getMessageID(), json.getString("messageID"));
    }
    
    @Test
    public void testMultipleMessagesCounter() {
        Message m1 = new Message("0831234567", "First message");
        Message m2 = new Message("0831234568", "Second message");
        
        assertTrue(m2.getNumMessagesSent() > m1.getNumMessagesSent());
    }
}