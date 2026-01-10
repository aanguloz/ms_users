package com.user.project.ms_user.service.impl;

import com.user.project.ms_user.model.dto.request.UserReqDTO;
import com.user.project.ms_user.model.dto.request.UserRoleReqDTO;
import com.user.project.ms_user.model.dto.response.UserRespDTO;
import com.user.project.ms_user.model.entity.User;
import com.user.project.ms_user.repository.UserRepository;
import com.user.project.ms_user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRespDTO addUser(UserReqDTO user) {
        // 1. Verifica que el usuario o email no existan (opcional, pero recomendado)
        if (userRepository.existsByUsername(user.getUserName())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // 2. Crea la entidad
        User userEntity = new User();
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUserName());

        // 3. ✅ Encripta la contraseña antes de guardar
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userEntity.setPassword(encodedPassword); // Solo guardamos la versión encriptada

        // 4. Guarda en base de datos
        userRepository.save(userEntity);

        // 5. Retorna DTO sin contraseña (¡nunca la expongas!)
        return new UserRespDTO(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getUsername()
        );
    }

    @Override
    public UserRespDTO updateUser(UserRoleReqDTO user) {

        return null;
    }

}
