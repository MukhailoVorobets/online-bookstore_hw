package com.example.service;

import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;
import com.example.exception.RegistrationException;

public interface UserService {
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException;
}
