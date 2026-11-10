package com.sliit.weddingplanner.security;

import com.sliit.weddingplanner.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Component
public class AuthTokenService {

    public static final String USER_ATTRIBUTE = AuthTokenService.class.getName() + ".user";
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenService.class);
    private static final long TOKEN_LIFETIME_SECONDS = 12 * 60 * 60;

    private final byte[] signingKey;

    public AuthTokenService(
            @Value("${app.auth.token-secret:}") String configuredSecret) {
        if (configuredSecret == null || configuredSecret.isBlank()) {
            signingKey = new byte[32];
            new SecureRandom().nextBytes(signingKey);
            logger.warn("APP_AUTH_TOKEN_SECRET is not configured; issued tokens will be invalidated when the server restarts");
        } else {
            signingKey = configuredSecret.getBytes(StandardCharsets.UTF_8);
            if (signingKey.length < 32) {
                throw new IllegalArgumentException("APP_AUTH_TOKEN_SECRET must be at least 32 bytes");
            }
        }
    }

    public String issueToken(int userId, String role) {
        try {
            String payload = userId + ":" + role + ":" + (Instant.now().getEpochSecond() + TOKEN_LIFETIME_SECONDS);
            String encodedPayload = encode(payload.getBytes(StandardCharsets.UTF_8));
            return encodedPayload + "." + encode(sign(encodedPayload));
        } catch (Exception e) {
            throw new IllegalStateException("Could not issue authentication token", e);
        }
    }

    public AuthenticatedUser verify(String token) {
        try {
            String[] parts = token.split("\\.", -1);
            if (parts.length != 2) {
                throw new UnauthorizedException("Invalid authentication token");
            }

            byte[] expectedSignature = sign(parts[0]);
            byte[] providedSignature = Base64.getUrlDecoder().decode(parts[1]);
            if (!MessageDigest.isEqual(expectedSignature, providedSignature)) {
                throw new UnauthorizedException("Invalid authentication token");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
            String[] claims = payload.split(":", -1);
            if (claims.length != 3) {
                throw new UnauthorizedException("Invalid authentication token");
            }
            int userId = Integer.parseInt(claims[0]);
            String role = claims[1];
            long expiresAt = Long.parseLong(claims[2]);
            if (userId <= 0 || !role.matches("ROLE_(ADMIN|CUSTOMER|VENDOR)")
                    || expiresAt <= Instant.now().getEpochSecond()) {
                throw new UnauthorizedException("Authentication token has expired or is invalid");
            }
            return new AuthenticatedUser(userId, role);
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid authentication token");
        }
    }

    public AuthenticatedUser requireUser(HttpServletRequest request) {
        Object user = request.getAttribute(USER_ATTRIBUTE);
        if (user instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }
        throw new UnauthorizedException("Authentication is required");
    }

    private byte[] sign(String content) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(signingKey, "HmacSHA256"));
        return mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    private String encode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }
}
