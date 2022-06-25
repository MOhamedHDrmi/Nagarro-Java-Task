package com.nagarro.javatest.security.authentication;

import com.nagarro.javatest.model.Role;
import com.nagarro.javatest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {

    Map<String, User> users = new HashMap<>();
    private static final String ADMIN = "admin";
    private static final String USER = "user";

    @Autowired
    PasswordEncoder encoder;

    @PostConstruct
    void loadUsers() {
        users = new HashMap<>();

        Set<Role> admin = new HashSet<>();
        admin.add(new Role(1L, "ROLE_ADMIN"));
        Set<Role> user = new HashSet<>();
        user.add(new Role(2L, "ROLE_USER"));

        users.put(ADMIN, new User(ADMIN, encoder.encode(ADMIN), admin));
        users.put(USER, new User(USER, encoder.encode(USER), user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = users.values().stream().filter(u -> username.equals(u.getUsername()))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Could not find user : " + username));

        return MyUserDetails.build(user);
    }
}