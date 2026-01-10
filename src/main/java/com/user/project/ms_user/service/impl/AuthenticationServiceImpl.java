package com.user.project.ms_user.service.impl;

import com.user.project.ms_user.model.dto.LoginPolicy;
import com.user.project.ms_user.model.dto.response.AuthRespDTO;
import com.user.project.ms_user.model.entity.User;
import com.user.project.ms_user.repository.UserRepository;
import com.user.project.ms_user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthRespDTO login(String userName, String password) {
        // 1. Verificar si cuenta está bloqueada
        if (redisService.isAccountLocked(userName)) {
            throw new RuntimeException("Cuenta bloqueada temporalmente");
        }

        // 2. Buscar usuario
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Validar contraseña
        if (!passwordEncoder.matches(password, user.getPassword())) {
            redisService.incrementFailedAttempts(userName);
            throw new RuntimeException("Credenciales inválidas");
        }

        // 4. Resetear intentos fallidos
        redisService.resetFailedAttempts(userName);

        // 5. Generar tokens usando política de Redis
        LoginPolicy policy = redisService.getLoginPolicy();
        String accessToken = jwtService.generateAccessToken(user, policy.getAccessTokenExpiryMinutes());
        String refreshToken = jwtService.generateRefreshToken(user);

        // 6. Almacenar refresh token en Redis
        redisService.storeRefreshToken(userName, refreshToken);

        return new AuthRespDTO(accessToken, refreshToken);
    }

    @Override
    public AuthRespDTO refresh(String userName, String providedRefreshToken) {
        // 1. Obtener refresh token almacenado
        String storedRefreshToken = redisService.getRefreshToken(userName);
        if (storedRefreshToken == null || !storedRefreshToken.equals(providedRefreshToken)) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        // 2. Generar nuevo access token
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        LoginPolicy policy = redisService.getLoginPolicy();
        String newAccessToken = jwtService.generateAccessToken(user, policy.getAccessTokenExpiryMinutes());

        // 3. Opcional: generar nuevo refresh token y actualizar en Redis
        String newRefreshToken = jwtService.generateRefreshToken(user);
        redisService.storeRefreshToken(userName, newRefreshToken);

        return new AuthRespDTO(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String username) {
        redisService.invalidateRefreshToken(username);
    }
}