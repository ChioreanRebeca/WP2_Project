package com.locafy.locafy.security;

import com.locafy.locafy.domain.Local;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class LocalUserDetails implements UserDetails {

    private final Local local;

    public LocalUserDetails(Local local) {
        this.local = local;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_LOCAL"));
    }

    @Override
    public String getPassword() {
        return local.getPassword();
    }

    @Override
    public String getUsername() {
        return local.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Local getLocal() {
        return local;
    }
}

