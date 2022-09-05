package com.my.exhibitions.security;

import com.my.exhibitions.entities.User;
import com.my.exhibitions.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private final static Logger LOGGER = Logger.getLogger(MyUserDetailsService.class);

    private final UserService userService;

    @Autowired
    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        LOGGER.info("Trying to log " + username + " in");
        User user = userService.findByUsername(username);
        if (user == null) {
            LOGGER.error("Cannot find user " + username);
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }
}
