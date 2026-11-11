package com.sliit.weddingplanner.security;

import com.sliit.weddingplanner.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Pattern PACKAGE_ID_PATH = Pattern.compile("^/api/packages/(\\d+)(?:/availability)?$");
    private static final Pattern PACKAGE_COLLECTION_PATH = Pattern.compile("^/api/packages$");
    private static final Pattern VENDOR_ID_PATH = Pattern.compile("^/api/vendors/(\\d+)(?:/(approve|reject|availability))?$");
    private static final Pattern VENDOR_COLLECTION_PATH = Pattern.compile("^/api/vendors$");
    private static final Pattern ADMIN_RESOURCE_PATH = Pattern.compile("^/api/admins(?:/\\d+)?$");
    private static final Pattern CATEGORY_RESOURCE_PATH = Pattern.compile("^/api/categories(?:/\\d+)?$");
    private static final Pattern BOOKING_COLLECTION_PATH = Pattern.compile("^/api/bookings$");
    private static final Pattern REVIEW_COLLECTION_PATH = Pattern.compile("^/api/reviews$");
    private static final Pattern BOOKING_PACKAGE_PATH = Pattern.compile("^/api/bookings/package/(\\d+)/status$");
    private static final Pattern VENDOR_BOOKINGS_PATH = Pattern.compile("^/api/bookings/vendor/(\\d+)$");
    private static final Pattern VENDOR_PACKAGES_PATH = Pattern.compile("^/api/packages/vendor/(\\d+)$");
    private static final Pattern VENDOR_REVIEWS_PATH = Pattern.compile("^/api/reviews/vendor/(\\d+)$");

    private final AuthTokenService tokenService;

    public AuthTokenFilter(AuthTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthenticatedUser user = null;
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            if (!authorization.startsWith("Bearer ") || authorization.length() <= 7) {
                writeUnauthorized(response, "Invalid authentication token");
                return;
            }
            try {
                user = tokenService.verify(authorization.substring(7));
            } catch (UnauthorizedException e) {
                writeUnauthorized(response, e.getMessage());
                return;
            }
        }

        String requiredRole = requiredRole(request);
        if (requiredRole != null && user == null) {
            writeUnauthorized(response, "Authentication is required");
            return;
        }
        if ("ROLE_VENDOR_OR_ADMIN".equals(requiredRole)
                && user != null
                && !user.role().equals("ROLE_VENDOR")
                && !user.role().equals("ROLE_ADMIN")) {
            writeForbidden(response);
            return;
        }
        if (requiredRole != null && !"ROLE_VENDOR_OR_ADMIN".equals(requiredRole)
                && user != null && !requiredRole.equals(user.role())) {
            writeForbidden(response);
            return;
        }

        if (user != null) {
            request.setAttribute(AuthTokenService.USER_ATTRIBUTE, user);
        }
        filterChain.doFilter(request, response);
    }

    private String requiredRole(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.equals("/api/packages") && HttpMethod.POST.matches(method)) {
            return "ROLE_VENDOR";
        }
        if (PACKAGE_COLLECTION_PATH.matcher(path).matches() && HttpMethod.GET.matches(method)) {
            return "ROLE_VENDOR_OR_ADMIN";
        }
        if (BOOKING_COLLECTION_PATH.matcher(path).matches() && HttpMethod.GET.matches(method)) {
            return "ROLE_ADMIN";
        }
        if (REVIEW_COLLECTION_PATH.matcher(path).matches() && HttpMethod.GET.matches(method)) {
            return "ROLE_ADMIN";
        }
        if (VENDOR_COLLECTION_PATH.matcher(path).matches() && HttpMethod.GET.matches(method)) {
            return "ROLE_ADMIN";
        }
        if (ADMIN_RESOURCE_PATH.matcher(path).matches()) {
            return "ROLE_ADMIN";
        }
        if (CATEGORY_RESOURCE_PATH.matcher(path).matches()
                && (HttpMethod.POST.matches(method) || HttpMethod.PUT.matches(method)
                || HttpMethod.DELETE.matches(method))) {
            return "ROLE_ADMIN";
        }
        Matcher packagePath = PACKAGE_ID_PATH.matcher(path);
        if (packagePath.matches() && (HttpMethod.PUT.matches(method) || HttpMethod.DELETE.matches(method)
                || (path.endsWith("/availability") && HttpMethod.PATCH.matches(method)))) {
            return "ROLE_VENDOR";
        }
        if (BOOKING_PACKAGE_PATH.matcher(path).matches() && HttpMethod.PUT.matches(method)) {
            return "ROLE_VENDOR";
        }
        Matcher vendorBookings = VENDOR_BOOKINGS_PATH.matcher(path);
        if (vendorBookings.matches() && HttpMethod.GET.matches(method)) {
            return "ROLE_VENDOR";
        }
        Matcher vendorPackages = VENDOR_PACKAGES_PATH.matcher(path);
        if (vendorPackages.matches() && HttpMethod.GET.matches(method)) {
            return "ROLE_VENDOR";
        }
        Matcher vendorReviews = VENDOR_REVIEWS_PATH.matcher(path);
        if (vendorReviews.matches() && HttpMethod.GET.matches(method)) {
            return "ROLE_VENDOR";
        }
        Matcher vendorPath = VENDOR_ID_PATH.matcher(path);
        if (vendorPath.matches()) {
            String action = vendorPath.group(2);
            if (action == null && HttpMethod.GET.matches(method)) {
                return "ROLE_VENDOR_OR_ADMIN";
            }
            if ("availability".equals(action) && HttpMethod.PATCH.matches(method)) {
                return "ROLE_VENDOR_OR_ADMIN";
            }
            if (("approve".equals(action) || "reject".equals(action))
                    && HttpMethod.PUT.matches(method)) {
                return "ROLE_ADMIN";
            }
            if (action == null && (HttpMethod.PUT.matches(method) || HttpMethod.DELETE.matches(method))) {
                return "ROLE_ADMIN";
            }
        }
        return null;
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":401,\"message\":\"" + message + "\"}");
    }

    private void writeForbidden(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":403,\"message\":\"You are not allowed to perform this action\"}");
    }
}
