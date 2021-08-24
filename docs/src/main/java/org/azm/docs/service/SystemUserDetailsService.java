package org.azm.docs.service;

import lombok.RequiredArgsConstructor;
import org.azm.docs.database.model.SystemUser;
import org.azm.docs.database.repository.SystemUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SystemUserDetailsService implements UserDetailsService {

    private final SystemUserRepository systemUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser user = systemUserRepository.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with username %s is not found", username));
        } else {
            return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
        }
    }
}
