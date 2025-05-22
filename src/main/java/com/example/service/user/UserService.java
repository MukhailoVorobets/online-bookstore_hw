package com.example.service.user;

import com.example.dto.user.UserRegistrationRequestDto;
import com.example.dto.user.UserResponseDto;
import com.example.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException;
}
