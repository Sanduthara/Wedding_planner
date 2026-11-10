package com.sliit.weddingplanner.security;

import com.sliit.weddingplanner.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthTokenServiceTest {

    private final AuthTokenService tokenService =
            new AuthTokenService("local-test-secret-that-is-at-least-32-bytes");

    @Test
    void issuesAndVerifiesSignedUserClaims() {
        String token = tokenService.issueToken(42, "ROLE_VENDOR");

        assertEquals(new AuthenticatedUser(42, "ROLE_VENDOR"), tokenService.verify(token));
    }

    @Test
    void rejectsTamperedToken() {
        String token = tokenService.issueToken(42, "ROLE_VENDOR");
        String[] parts = token.split("\\.");
        parts[0] = parts[0].substring(0, parts[0].length() - 1) + "A";

        assertThrows(UnauthorizedException.class, () -> tokenService.verify(String.join(".", parts)));
    }
}
