package io.github.px1d.auth.aspect;

import io.github.px1d.auth.security.AuthenticatedUser;
import io.github.px1d.auth.security.SecurityContextHolder;
import io.github.px1d.auth.util.JwtUtil;
import io.github.px1d.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationAspect {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    @Around("@annotation(io.github.px1d.auth.annotation.RequiresAuth) || @within(io.github.px1d.auth.annotation.RequiresAuth)")
    public Object validateToken(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String token = extractTokenFromHeader(request);
            if (token == null || !jwtUtil.validateToken(token)) {
                throw new UnauthorizedException("Invalid or missing JWT token");
            }
            AuthenticatedUser user = jwtUtil.getUserFromToken(token);
            SecurityContextHolder.setCurrentUser(user);
            return joinPoint.proceed();
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Authentication failed", e);
            throw new UnauthorizedException("Authentication failed", e);
        } finally {
            SecurityContextHolder.clear();
        }
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
