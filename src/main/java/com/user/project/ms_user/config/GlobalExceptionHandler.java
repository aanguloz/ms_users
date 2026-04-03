package com.user.project.ms_user.config;

import com.user.project.ms_user.exception.InvalidCredentialsException;
import com.user.project.ms_user.service.impl.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountLockedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final RedisService redisService;

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String, Object>> handleAccountLocked(AccountLockedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("status", 423);

        Long lockTimeRemaining = redisService.getLockTimeRemaining(ex.getMessage().split(" ")[1]); // extraer username
        body.put("lockTimeRemaining", lockTimeRemaining);
        body.put("message", "Demasiados intentos fallidos. Intente más tarde.");

        return ResponseEntity.status(423).body(body);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("status", 401);

        String username = extractUsernameFromRequest(request);
        int failedAttempts = redisService.getFailedAttempts(username);
        int maxAttempts = 5; // o el valor que tengas configurado

        body.put("failedAttempts", failedAttempts);
        body.put("remainingAttempts", Math.max(0, maxAttempts - failedAttempts));
        body.put("message", "Usuario o contraseña incorrectos");

        return ResponseEntity.status(401).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Error interno del servidor");
        body.put("status", 500);
        body.put("message", ex.getMessage());

        return ResponseEntity.status(500).body(body);
    }

    private String extractUsernameFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        // 1. Validar que exista el header y tenga el prefijo "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "unknown";
        }

        try {
            // 2. Extraer el token (quitar "Bearer ")
            String token = authHeader.substring(7);

            // 3. OPCIÓN A: Si tienes una utilidad JWT propia (RECOMENDADO)
            // return jwtUtil.extractUsername(token);
            // o: return jwtUtil.extractClaim(token, Claims::getSubject);

            // 4. OPCIÓN B: Parseo manual sin validación de firma (solo para logging/error handling)
            // ⚠️ No usar para lógica de seguridad, solo para obtener datos de auditoría
            String[] parts = token.split("\\.");
            if (parts.length != 3) return "unknown";

            // Decodificar el payload (parte central del JWT)
            String payload = new String(
                    java.util.Base64.getUrlDecoder().decode(parts[1]),
                    java.nio.charset.StandardCharsets.UTF_8
            );

            // Extraer claim "sub" o "username" con expresión regular simple
            // Busca: "sub":"valor" o "username":"valor"
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"(?:sub|username)\"\\s*:\\s*\"([^\"]+)\"");
            java.util.regex.Matcher matcher = pattern.matcher(payload);

            if (matcher.find()) {
                return matcher.group(1);
            }

            return "unknown";

        } catch (Exception e) {
            // Token malformado, decodificación fallida, etc.
            return "unknown";
        }
    }

}
