package com.my.exhibitions.services;

import com.my.exhibitions.entities.User;
import com.my.exhibitions.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static com.my.exhibitions.entities.enums.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    User user;

    @BeforeEach
    public void setUp() {
       user = new User();
       user.setId(10000L);
       user.setUsername("sasha");
       user.setPassword("123");
       user.setRole(USER);
    }

    @Test
    public void testSave() {
        userService.save(user);

        verify(userRepository).save(user);
    }

    @Test
    public void testUpdate() {
        userService.update(user);

        verify(userRepository).save(user);
    }

    @Test
    public void testFindByUsername() {
        when(userRepository.findByUsername("sasha")).thenReturn(user);

        User userFound = userService.findByUsername("sasha");

        verify(userRepository).findByUsername(anyString());
        assertThat(userFound).isEqualTo(user);
    }

    @Test
    public void testFindByUsernameNull() {
        when(userRepository.findByUsername("sasha")).thenReturn(null);

        User userFound = userService.findByUsername("sasha");

        verify(userRepository).findByUsername(anyString());
        assertThat(userFound).isNull();
    }

    @Test
    public void testFindAll() {
        User user1 = new User();
        when(userRepository.findAll()).thenReturn(List.of(user, user1));

        List<User> users = userService.findAll();

        verify(userRepository).findAll();
        assertThat(users).isEqualTo(List.of(user, user1));

    }

    @Test
    public void testFindAllNull() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.findAll();

        verify(userRepository).findAll();
        assertThat(users).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testExistsByUsername() {
        when(userRepository.existsByUsername("sasha")).thenReturn(true);

        boolean exists = userService.existsByUsername("sasha");

        assertThat(exists).isTrue();
    }

    @Test
    public void createAdmin() {
        userService.createAdmin();

        verify(userRepository, atLeast(1)).save(any());

    }

}
