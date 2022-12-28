package com.kasperserzysko.web.services;

import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final DataRepository db;

    public UserService(DataRepository db) {
        this.db = db;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return db.getUsers().findUserByEmail(username).map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
