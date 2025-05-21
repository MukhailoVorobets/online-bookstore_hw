package com.example.service.user;

import com.example.dto.user.UserRegistrationRequestDto;
import com.example.dto.user.UserResponseDto;
import com.example.exception.EntityNotFoundException;
import com.example.exception.RegistrationException;
import com.example.mapper.UserMapper;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.role.RoleRepository;
import com.example.repository.user.UserRepository;
import com.example.service.shoppingcart.ShoppingCartService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

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
        repository.save(user);
        shoppingCartService.createShoppingCart(user);
        return userMapper.toDto(user);
    }
}
