package com.locafy.locafy.security;

import com.locafy.locafy.domain.Local;
import com.locafy.locafy.repositories.LocalRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LocalUserDetailsService implements UserDetailsService {

    private final LocalRepository localRepository;

    public LocalUserDetailsService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Local local = localRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Local not found"));
        return new LocalUserDetails(local);
    }
}
