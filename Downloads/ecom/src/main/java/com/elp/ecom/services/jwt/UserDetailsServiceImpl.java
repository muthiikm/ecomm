package com.elp.ecom.services.jwt;

import com.elp.ecom.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> OptionalUser = userRepository.findFirstByEmail(username);
       if(OptionalUser.isEmpty()) throw new UsernameNotFoundException("Username not found ");
    }
}
