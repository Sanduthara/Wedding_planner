package com.sliit.weddingplanner.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordServiceTest {

    private final PasswordService passwordService = new PasswordService();

    @Test
    void encodesAndVerifiesPasswords() {
        String encoded = passwordService.encode("vendor-password");

        assertTrue(passwordService.isEncoded(encoded));
        assertTrue(passwordService.matches("vendor-password", encoded));
        assertFalse(passwordService.matches("different-password", encoded));
    }

    @Test
    void supportsLegacyPlaintextPasswordForOneTimeMigration() {
        assertTrue(passwordService.matches("legacy-password", "legacy-password"));
        assertFalse(passwordService.isEncoded("legacy-password"));
    }
}
