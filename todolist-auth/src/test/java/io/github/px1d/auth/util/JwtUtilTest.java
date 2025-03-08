package io.github.px1d.auth.util;

import io.github.px1d.auth.security.AuthenticatedUser;
import io.github.px1d.exception.UnauthorizedException;
import io.github.px1d.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilTest {

    private static final String SECRET = "test-secret-key";
    private static final Long EXPIRATION = 3600000L; // 1 hour in milliseconds
    private JwtUtil jwtUtil;
    private User mockUser;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRATION);

        // Initialize mock user
        mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getEmail()).thenReturn("test@example.com");
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // Act
        String token = jwtUtil.generateToken(mockUser);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtUtil.validateToken(token, mockUser));

        // Verify claims
        assertEquals("1", jwtUtil.extractId(token));

        // Verify expiration is in the future
        Date expiration = jwtUtil.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Arrange
        String token = jwtUtil.generateToken(mockUser);

        // Act
        boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Arrange - manipulate token to make it invalid
        String validToken = jwtUtil.generateToken(mockUser);
        String invalidToken = validToken + "invalid";

        // Act
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithExpiredToken_ShouldReturnFalse() {
        // Arrange - create token with past expiration
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", mockUser.getId());
        claims.put("email", mockUser.getEmail());

        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(mockUser.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis() - 2 * EXPIRATION))
                .setExpiration(new Date(System.currentTimeMillis() - EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        // Act
        boolean isValid = jwtUtil.validateToken(expiredToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void getUserFromToken_WithValidToken_ShouldReturnAuthenticatedUser() {
        // Arrange
        String token = jwtUtil.generateToken(mockUser);

        // Act
        AuthenticatedUser authenticatedUser = jwtUtil.getUserFromToken(token);

        // Assert
        assertNotNull(authenticatedUser);
        assertEquals(mockUser.getId(), authenticatedUser.getId());
        assertEquals(mockUser.getEmail(), authenticatedUser.getEmail());
    }

    @Test
    void getUserFromToken_WithInvalidToken_ShouldThrowUnauthorizedException() {
        // Arrange
        String invalidToken = "invalid.token.string";

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> jwtUtil.getUserFromToken(invalidToken));
    }

    @Test
    void extractId_ShouldReturnUserIdFromToken() {
        // Arrange
        String token = jwtUtil.generateToken(mockUser);

        // Act
        String id = jwtUtil.extractId(token);

        // Assert
        assertEquals(mockUser.getId().toString(), id);
    }

    @Test
    void extractExpiration_ShouldReturnExpirationDateFromToken() {
        // Arrange
        String token = jwtUtil.generateToken(mockUser);

        // Act
        Date expiration = jwtUtil.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
        long expectedTimeApprox = System.currentTimeMillis() + EXPIRATION;
        long allowedDelta = 10000; // Allow for small processing time differences (10 seconds)
        assertTrue(Math.abs(expectedTimeApprox - expiration.getTime()) < allowedDelta);
    }
}
