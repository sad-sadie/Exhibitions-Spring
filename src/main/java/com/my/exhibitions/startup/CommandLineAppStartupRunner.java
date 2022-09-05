package com.my.exhibitions.startup;

import com.my.exhibitions.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {


    private final static Logger LOGGER = Logger.getLogger(CommandLineAppStartupRunner.class);
    private final UserService userService;

    @Autowired
    public CommandLineAppStartupRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        boolean adminExists = userService.existsByUsername("admin");
        if(!adminExists) {
            LOGGER.info("Registering admin");
            userService.createAdmin();
        }
    }
}