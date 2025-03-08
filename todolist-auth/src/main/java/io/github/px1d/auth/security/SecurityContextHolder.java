package io.github.px1d.auth.security;

public class SecurityContextHolder {

    private static final ThreadLocal<AuthenticatedUser> CONTEXT = new ThreadLocal<>();

    public static void setCurrentUser(AuthenticatedUser user) {
        CONTEXT.set(user);
    }

    public static AuthenticatedUser getCurrentUser() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
