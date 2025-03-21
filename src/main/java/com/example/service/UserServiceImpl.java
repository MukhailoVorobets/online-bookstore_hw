package com.example.service;

import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;
import com.example.exception.EntityNotFoundException;
import com.example.exception.RegistrationException;
import com.example.mapper.UserMapper;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RegistrationException(
                    "Registration failed: User with email "
                            + request.getEmail() + " already exists.");
        }

        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByRole(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Role "
                        + Role.RoleName.USER + " not found"));
        user.setRoles(Set.of(userRole));
        return userMapper.toDto(repository.save(user));
    }
}
