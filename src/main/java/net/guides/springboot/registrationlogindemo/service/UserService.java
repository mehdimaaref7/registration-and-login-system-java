package net.guides.springboot.registrationlogindemo.service;

import net.guides.springboot.registrationlogindemo.dto.UserDto;
import net.guides.springboot.registrationlogindemo.entity.User;

import java.util.List;

public interface UserService {
    void saveUSer(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
