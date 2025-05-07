//package com.locafy.locafy.security;
//
//import com.locafy.locafy.domain.BusinessOwner;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//
//public class BusinessOwnerUserDetails implements UserDetails {
//
//    private final BusinessOwner businessOwner;
//
//    public BusinessOwnerUserDetails(BusinessOwner businessOwner) {
//        this.businessOwner = businessOwner;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(new SimpleGrantedAuthority("ROLE_BUSINESS_OWNER"));
//    }
//
//    @Override
//    public String getPassword() {
//        return businessOwner.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return businessOwner.getUserName();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    public BusinessOwner getBusinessOwner() {
//        return businessOwner;
//    }
//}
