package io.github.px1d.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    @DisplayName("generateSalt should create a non-null, non-empty Base64 encoded string")
    void generateSaltShouldCreateValidSalt() {
        // Act
        String salt = PasswordUtil.generateSalt();

        // Assert
        assertNotNull(salt, "Salt should not be null");
        assertFalse(salt.isEmpty(), "Salt should not be empty");

        // Verify it's a valid Base64 string
        assertDoesNotThrow(() -> Base64.getDecoder().decode(salt),
                "Salt should be a valid Base64 encoded string");

        // Verify salt length (16 bytes = 24 Base64 chars with padding)
        assertEquals(24, salt.length(), "Salt should be 24 characters long when Base64 encoded");
    }

    @Test
    @DisplayName("generateSalt should create different salts on each call")
    void generateSaltShouldCreateDifferentSalts() {
        // Act
        String salt1 = PasswordUtil.generateSalt();
        String salt2 = PasswordUtil.generateSalt();
        String salt3 = PasswordUtil.generateSalt();

        // Assert
        assertNotEquals(salt1, salt2, "Different calls should generate different salts");
        assertNotEquals(salt1, salt3, "Different calls should generate different salts");
        assertNotEquals(salt2, salt3, "Different calls should generate different salts");
    }

    @Test
    @DisplayName("hashPassword should create a non-null, non-empty hash")
    void hashPasswordShouldCreateValidHash() {
        // Arrange
        String password = "securePassword123";
        String salt = PasswordUtil.generateSalt();

        // Act
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        // Assert
        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertFalse(hashedPassword.isEmpty(), "Hashed password should not be empty");

        // Verify it's a valid Base64 string
        assertDoesNotThrow(() -> Base64.getDecoder().decode(hashedPassword),
                "Hashed password should be a valid Base64 encoded string");
    }

    @Test
    @DisplayName("hashPassword should create the same hash for the same password and salt")
    void hashPasswordShouldBeConsistent() {
        // Arrange
        String password = "securePassword123";
        String salt = PasswordUtil.generateSalt();

        // Act
        String hashedPassword1 = PasswordUtil.hashPassword(password, salt);
        String hashedPassword2 = PasswordUtil.hashPassword(password, salt);

        // Assert
        assertEquals(hashedPassword1, hashedPassword2,
                "Same password and salt should produce the same hash");
    }

    @Test
    @DisplayName("hashPassword should create different hashes for different passwords with the same salt")
    void hashPasswordShouldCreateDifferentHashesForDifferentPasswords() {
        // Arrange
        String password1 = "securePassword123";
        String password2 = "differentPassword456";
        String salt = PasswordUtil.generateSalt();

        // Act
        String hashedPassword1 = PasswordUtil.hashPassword(password1, salt);
        String hashedPassword2 = PasswordUtil.hashPassword(password2, salt);

        // Assert
        assertNotEquals(hashedPassword1, hashedPassword2,
                "Different passwords with the same salt should produce different hashes");
    }

    @Test
    @DisplayName("hashPassword should create different hashes for the same password with different salts")
    void hashPasswordShouldCreateDifferentHashesForDifferentSalts() {
        // Arrange
        String password = "securePassword123";
        String salt1 = PasswordUtil.generateSalt();
        String salt2 = PasswordUtil.generateSalt();

        // Act
        String hashedPassword1 = PasswordUtil.hashPassword(password, salt1);
        String hashedPassword2 = PasswordUtil.hashPassword(password, salt2);

        // Assert
        assertNotEquals(hashedPassword1, hashedPassword2,
                "Same password with different salts should produce different hashes");
    }

    @Test
    @DisplayName("hashPassword should throw RuntimeException for invalid salt")
    void hashPasswordShouldThrowExceptionForInvalidSalt() {
        // Arrange
        String password = "securePassword123";
        String invalidSalt = "not-a-valid-base64-string!";

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> PasswordUtil.hashPassword(password, invalidSalt),
                "Should throw RuntimeException for invalid salt");
    }

    @Test
    @DisplayName("verifyPassword should return true for correct password")
    void verifyPasswordShouldReturnTrueForCorrectPassword() {
        // Arrange
        String password = "securePassword123";
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        // Act
        boolean result = PasswordUtil.verifyPassword(password, salt, hashedPassword);

        // Assert
        assertTrue(result, "Should return true for correct password");
    }

    @Test
    @DisplayName("verifyPassword should return false for incorrect password")
    void verifyPasswordShouldReturnFalseForIncorrectPassword() {
        // Arrange
        String correctPassword = "securePassword123";
        String incorrectPassword = "wrongPassword456";
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(correctPassword, salt);

        // Act
        boolean result = PasswordUtil.verifyPassword(incorrectPassword, salt, hashedPassword);

        // Assert
        assertFalse(result, "Should return false for incorrect password");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "short", "password", "12345"})
    @DisplayName("hashPassword should work with various password inputs")
    void hashPasswordShouldWorkWithVariousInputs(String password) {
        // Arrange
        String salt = PasswordUtil.generateSalt();

        // Act & Assert
        assertDoesNotThrow(() -> {
            String hashedPassword = PasswordUtil.hashPassword(password, salt);
            assertNotNull(hashedPassword);
            assertFalse(hashedPassword.isEmpty());
        }, "Should handle various password inputs without throwing exceptions");
    }

    @Test
    @DisplayName("verifyPassword should handle null inputs properly")
    void verifyPasswordShouldHandleNullInputs() {
        // Arrange
        String password = "securePassword123";
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> PasswordUtil.verifyPassword(null, salt, hashedPassword),
                "Should throw NullPointerException for null password");

        assertThrows(RuntimeException.class,
                () -> PasswordUtil.verifyPassword(password, null, hashedPassword),
                "Should throw RuntimeException for null salt");

        assertFalse(PasswordUtil.verifyPassword(password, salt, null),
                "Should return false for null hashedPassword");
    }
}
