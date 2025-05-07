//package com.locafy.locafy.security;
//
//import com.locafy.locafy.domain.BusinessOwner;
//import com.locafy.locafy.repositories.BusinessOwnerRepository;
//import org.springframework.security.core.userdetails.*;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class BusinessOwnerUserDetailsService implements UserDetailsService {
//
//    private final BusinessOwnerRepository businessOwnerRepository;
//
//    public BusinessOwnerUserDetailsService(BusinessOwnerRepository businessOwnerRepository) {
//        this.businessOwnerRepository = businessOwnerRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        BusinessOwner owner = businessOwnerRepository.findByUserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Business owner not found"));
//
//        return new User(
//                owner.getUserName(),
//                owner.getPassword(),
//                List.of(new SimpleGrantedAuthority("ROLE_BUSINESS_OWNER"))
//        );
//    }
//}
