package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.Business;
import com.locafy.locafy.domain.Local;
import com.locafy.locafy.repositories.BusinessRepository;
import com.locafy.locafy.repositories.LocalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LocalControllerTest {

    private LocalRepository localRepository;
    private BusinessRepository businessRepository;
    private LocalsController localsController;

    @BeforeEach
    void setUp() {
        localRepository = mock(LocalRepository.class);
        businessRepository = mock(BusinessRepository.class);
        localsController = new LocalsController();
        // Inject mocks manually because LocalsController uses @Autowired on fields
        localsController.localRepository = localRepository;
        localsController.businessRepository = businessRepository;
    }

    @Test
    void testLocalsHomePage() {
        Model model = mock(Model.class);
        Principal principal = () -> "user123";

        Local local = new Local();
        local.setUserName("user123");

        List<Business> businesses = List.of(new Business());

        when(localRepository.findByUserName("user123")).thenReturn(Optional.of(local));
        when(businessRepository.findAll()).thenReturn(businesses);

        String viewName = localsController.localsHomePage(model, principal);

        assertThat(viewName).isEqualTo("locals-home");
        verify(model).addAttribute("local", local);
        verify(model).addAttribute("businesses", businesses);
    }

    @Test
    void testLocalProfilePage() {
        Model model = mock(Model.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user123");

        Local local = new Local();
        local.setUserName("user123");

        when(localRepository.findByUserName("user123")).thenReturn(Optional.of(local));

        String viewName = localsController.localProfilePage(model, userDetails);

        assertThat(viewName).isEqualTo("local-profile");
        verify(model).addAttribute("local", local);
    }

    @Test
    void testUpdateLocalProfile() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user123");

        Local formLocal = new Local();
        formLocal.setPhoneNumber("123456789");
        formLocal.setAddress("New Address");
        formLocal.setPassword("newpass");

        Local currentLocal = new Local();
        currentLocal.setUserName("user123");
        currentLocal.setPhoneNumber("oldPhone");
        currentLocal.setAddress("oldAddress");
        currentLocal.setPassword("oldPass");

        when(localRepository.findByUserName("user123")).thenReturn(Optional.of(currentLocal));

        String redirect = localsController.updateLocalProfile(formLocal, null, null, userDetails);

        assertThat(redirect).isEqualTo("redirect:/local-profile");

        ArgumentCaptor<Local> localCaptor = ArgumentCaptor.forClass(Local.class);
        verify(localRepository).save(localCaptor.capture());
        Local savedLocal = localCaptor.getValue();

        assertThat(savedLocal.getPhoneNumber()).isEqualTo("123456789");
        assertThat(savedLocal.getAddress()).isEqualTo("New Address");
        assertThat(savedLocal.getPassword()).isEqualTo("newpass");
    }
}
