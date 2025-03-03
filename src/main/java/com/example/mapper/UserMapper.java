package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;
import com.example.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
