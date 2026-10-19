   package com.mycompany.poe_part_2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {
    
    @Test
    public void testCheckUserName() {
        System.out.println("Testing checkUserName...");
        
        // Test valid usernames
        assertTrue(POE_PART_2.checkUserName("user_"), "Username with underscore at end should be valid");
        assertTrue(POE_PART_2.checkUserName("_name"), "Username with underscore at start should be valid");
        assertTrue(POE_PART_2.checkUserName("a_b_c"), "Username with underscore in middle should be valid");
        
        // Test invalid usernames
        assertFalse(POE_PART_2.checkUserName("username"), "Username without underscore should be invalid");
        assertFalse(POE_PART_2.checkUserName("user"), "Username shorter than 5 characters should be invalid");
        assertFalse(POE_PART_2.checkUserName("name"), "Username without underscore should be invalid");
        assertFalse(POE_PART_2.checkUserName(null), "Null username should be invalid");
        
        System.out.println("checkUserName tests passed!");
    }
    
    @Test
    public void testValidatePassword() {
        System.out.println("Testing validatePassword...");
        
        // Test valid passwords
        assertTrue(POE_PART_2.validatePassword("Pass123!"), "Password with all requirements should be valid");
        assertTrue(POE_PART_2.validatePassword("A1@bcdefg"), "Password with all requirements should be valid");
        assertTrue(POE_PART_2.validatePassword("Test@123"), "Password with all requirements should be valid");
        
        // Test invalid passwords
        assertFalse(POE_PART_2.validatePassword("short"), "Password shorter than 8 characters should be invalid");
        assertFalse(POE_PART_2.validatePassword("nouppercase123!"), "Password without uppercase should be invalid");
        assertFalse(POE_PART_2.validatePassword("NOLOWERCASE123!"), "Password without lowercase should be invalid");
        assertFalse(POE_PART_2.validatePassword("NoSpecial123"), "Password without special character should be invalid");
        assertFalse(POE_PART_2.validatePassword("NoNumber!"), "Password without number should be invalid");
        assertFalse(POE_PART_2.validatePassword(null), "Null password should be invalid");
        
        System.out.println("validatePassword tests passed!");
    }
    
    @Test
    public void testIsValidPhoneNumber() {
        System.out.println("Testing isValidPhoneNumber...");
        
        // Test valid RSA numbers
        assertTrue(POE_PART_2.isValidPhoneNumber("+27831234567"), "+27 format should be valid");
        assertTrue(POE_PART_2.isValidPhoneNumber("0831234567"), "0XXXXXXXXX format should be valid");
        assertTrue(POE_PART_2.isValidPhoneNumber("27831234567"), "27XXXXXXXXX format should be valid");
        assertTrue(POE_PART_2.isValidPhoneNumber("083 123 4567"), "Number with spaces should be valid");
        assertTrue(POE_PART_2.isValidPhoneNumber("083-123-4567"), "Number with dashes should be valid");
        
        // Test invalid numbers
        assertFalse(POE_PART_2.isValidPhoneNumber("12345"), "Too short number should be invalid");
        assertFalse(POE_PART_2.isValidPhoneNumber("+278312345678"), "Too long number should be invalid");
        assertFalse(POE_PART_2.isValidPhoneNumber("+44831234567"), "Wrong country code should be invalid");
        assertFalse(POE_PART_2.isValidPhoneNumber(""), "Empty string should be invalid");
        assertFalse(POE_PART_2.isValidPhoneNumber(null), "Null should be invalid");
        
        System.out.println("isValidPhoneNumber tests passed!");
    }
}