package com.my.exhibitions.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final static Logger LOGGER = Logger.getLogger(HomeController.class);
    @GetMapping("/home")
    public String getHome() {
        LOGGER.info("Get -> /home");
        return "home";
    }
}