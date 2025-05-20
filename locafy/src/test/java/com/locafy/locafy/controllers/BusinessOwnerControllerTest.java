package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.BusinessOwner;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import com.locafy.locafy.repositories.BusinessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.Model;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessOwnerControllerTest {

    private BusinessOwnerRepository businessOwnerRepository;
    private BusinessRepository businessRepository;
    private BusinessOwnerController controller;

    @BeforeEach
    void setUp() {
        businessOwnerRepository = mock(BusinessOwnerRepository.class);
        businessRepository = mock(BusinessRepository.class);
        controller = new BusinessOwnerController(businessOwnerRepository, businessRepository);
    }

    @Test
    void testBusinessOwnerHome() {
        // Mock data
        Principal principal = () -> "biz123";
        BusinessOwner owner = new BusinessOwner();
        owner.setUsername("biz123");

        when(businessOwnerRepository.findByUsername("biz123")).thenReturn(Optional.of(owner));
        when(businessRepository.findAll()).thenReturn(Collections.emptyList());

        Model model = mock(Model.class);
        String viewName = controller.businessOwnerHome(model, principal);

        assertEquals("business-owner-home", viewName);
        verify(model).addAttribute("owner", owner);
        verify(model).addAttribute("businesses", Collections.emptyList());
    }

    @Test
    void testShowBusinessOwnerProfile() {
        BusinessOwner owner = new BusinessOwner();
        owner.setUsername("biz123");

        when(businessOwnerRepository.findByUsername("biz123")).thenReturn(Optional.of(owner));
        Model model = mock(Model.class);

        User userDetails = new User("biz123", "password", Collections.emptyList());
        String viewName = controller.showBusinessOwnerProfile(userDetails, model);

        assertEquals("business-owner-profile", viewName);
        verify(model).addAttribute("owner", owner);
    }

    @Test
    void testUpdateBusinessOwnerProfile() {
        BusinessOwner storedOwner = new BusinessOwner();
        storedOwner.setUsername("biz123");

        when(businessOwnerRepository.findByUsername("biz123")).thenReturn(Optional.of(storedOwner));

        BusinessOwner formOwner = new BusinessOwner();
        formOwner.setPhoneNumber("12345");
        formOwner.setAddress("New Address");
        formOwner.setPassword("{noop}newpass");

        User userDetails = new User("biz123", "password", Collections.emptyList());

        String result = controller.updateBusinessOwnerProfile(formOwner, null, null, userDetails);

        assertEquals("redirect:/business-owner-profile", result);

        ArgumentCaptor<BusinessOwner> captor = ArgumentCaptor.forClass(BusinessOwner.class);
        verify(businessOwnerRepository).save(captor.capture());

        BusinessOwner saved = captor.getValue();
        assertEquals("12345", saved.getPhoneNumber());
        assertEquals("New Address", saved.getAddress());
        assertEquals("{noop}newpass", saved.getPassword());
    }
}
