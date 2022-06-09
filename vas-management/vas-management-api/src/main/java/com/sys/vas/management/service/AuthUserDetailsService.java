package com.sys.vas.management.service;

import com.sys.vas.management.dto.entity.UserEntity;
import com.sys.vas.management.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AuthUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public AuthUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found")
                );
        log.info("Found user: {}", userEntity.toString());
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(userEntity.getUserRole().getRole()));

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEnabled(),
                true,
                true,
                userEntity.getEnabled(),
                authorityList
        );
    }
}
