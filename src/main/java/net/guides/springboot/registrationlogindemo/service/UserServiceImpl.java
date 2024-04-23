package net.guides.springboot.registrationlogindemo.service;

import net.guides.springboot.registrationlogindemo.dto.UserDto;
import net.guides.springboot.registrationlogindemo.entity.Role;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.repository.RoleRepository;
import net.guides.springboot.registrationlogindemo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public void UserServiceImpl(UserRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;

    }
    @Override
    public void saveUSer(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);

    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        try {
            //attempt to save the role using the repository
            return roleRepository.save(role);

        } catch(Exception e) {
            // Handle any exceptions that occur during the save operation
            // Log the error or perform other  error-handling actions as needed
            e.printStackTrace(); // Print the Stack trace
            // You may choose to throw  a custom exception or return a default role object
            // for simplicity, we'll return null in case of an error

            return null;
        }

    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}