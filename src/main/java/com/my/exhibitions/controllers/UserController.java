package com.my.exhibitions.controllers;

import com.my.exhibitions.entities.User;
import com.my.exhibitions.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class UserController {

    private final static Logger LOGGER = Logger.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin() {
        LOGGER.info("Get -> /login");
        return "login";
    }

    @PostMapping("/login")
    public String logUserIn() {
        LOGGER.info("Post -> /login");
        return "redirect:/home";
    }

    @GetMapping("/registration")
    public String getRegistration(Model model) {
        LOGGER.info("Get -> /registration");
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerNewUser(@Valid @ModelAttribute("user")  User user, BindingResult bindingResult) {
        LOGGER.info("Post -> /registration");
        boolean alreadyExists = userService.existsByUsername(user.getUsername());
        if(alreadyExists) {
            bindingResult.rejectValue(
                    "username",
                    "",
                    "User with such username already exists"
            );
        }
        if(bindingResult.hasErrors()) {
            LOGGER.error("Error while registering new user");
            return "registration";
        }
        userService.save(user);
        return "redirect:/home";
    }
}