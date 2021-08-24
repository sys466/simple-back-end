package service;

import org.azm.docs.database.model.SystemUser;
import org.azm.docs.database.repository.SystemUserRepository;
import org.azm.docs.service.SystemUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class SystemUserDetailsServiceTest {

    private SystemUserDetailsService systemUserDetailsService;
    private final SystemUserRepository systemUserRepository = Mockito.mock(SystemUserRepository.class);
    private final String USERNAME = "test";
    private final String PASSWORD = "password";
    private SystemUser systemUser;
    private User user;

    @BeforeEach
    public void createSystemUserDetailsService() {
        systemUserDetailsService = new SystemUserDetailsService(systemUserRepository);
    }

    @BeforeEach
    public void createSystemUser() {
        systemUser = new SystemUser();
        systemUser.setUsername(USERNAME);
        systemUser.setPassword(PASSWORD);
    }

    @BeforeEach
    public void createUser() {
        user = new User(USERNAME, PASSWORD, Collections.emptyList());
    }

    @Test
    public void loadUserByUsernameSuccessTest() {
        Mockito.when(systemUserRepository.getByUsername(USERNAME)).thenReturn(systemUser);
        Assertions.assertEquals(user, systemUserDetailsService.loadUserByUsername(USERNAME));
    }

    @Test
    public void loadUserByUsernameFailedTest() {
        Mockito.when(systemUserRepository.getByUsername(USERNAME)).thenReturn(null);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> systemUserDetailsService.loadUserByUsername(USERNAME));
    }

}
