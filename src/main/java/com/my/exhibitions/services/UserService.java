package com.my.exhibitions.services;

import com.my.exhibitions.entities.User;
import com.my.exhibitions.repositories.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.my.exhibitions.entities.enums.Role.ADMIN;
import static com.my.exhibitions.entities.enums.Role.USER;

@Service
public class UserService {

    private final static Logger LOGGER = Logger.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void update(User user) {
        LOGGER.info("Updating user " + user.getUsername());
        userRepository.save(user);
    }

    public void save(User user) {
        LOGGER.info("Saving new user " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(USER);
        userRepository.save(user);
    }


    public User findByUsername(String username) {
        LOGGER.info("Getting user by username " + username);
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        LOGGER.info("Getting all users");
        return userRepository.findAll();
    }

    public boolean existsByUsername(String username) {
        LOGGER.info("Checking if user " + username + " exists");
        return userRepository.existsByUsername(username);
    }

    public void createAdmin() {
        LOGGER.info("Creating and saving admin");
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("123"));
        user.setRole(ADMIN);
        userRepository.save(user);
    }
}
