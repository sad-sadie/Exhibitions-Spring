package com.my.exhibitions.startup;

import com.my.exhibitions.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public CommandLineAppStartupRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        boolean adminExists = userService.existsByUsername("admin");
        if(!adminExists) {
            userService.createAdmin();
        }
    }
}