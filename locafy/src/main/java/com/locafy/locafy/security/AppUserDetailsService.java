package com.locafy.locafy.security;

import com.locafy.locafy.repositories.AdminRepository;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import com.locafy.locafy.repositories.LocalRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final BusinessOwnerRepository businessOwnerRepository;
    private final LocalRepository localRepository;
    private final AdminRepository adminRepository;

    public AppUserDetailsService(BusinessOwnerRepository businessOwnerRepository, LocalRepository localRepository, AdminRepository adminRepository) {
        this.businessOwnerRepository = businessOwnerRepository;
        this.localRepository = localRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try loading Business Owner
        var businessOwner = businessOwnerRepository.findByUsername(username);
        if (businessOwner.isPresent()) {
            return new User(
                    businessOwner.get().getUsername(),
                    businessOwner.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_BUSINESS_OWNER"))
            );
        }

        // Try loading Local
        var local = localRepository.findByUserName(username);
        if (local.isPresent()) {
            return new User(
                    local.get().getUserName(),
                    local.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_LOCAL"))
            );
        }

        // Try loading Admin
        var admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) {
            return new User(
                    admin.get().getUsername(),
                    admin.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }


        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
