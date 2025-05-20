package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.BusinessOwner;
import com.locafy.locafy.domain.Local;
import com.locafy.locafy.dto.SignupFormDTO;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import com.locafy.locafy.repositories.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class SignupController {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private BusinessOwnerRepository businessOwnerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupFormDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("signupForm") SignupFormDTO form) {
       String encodedPassword = passwordEncoder.encode(form.getPassword());

        if ("LOCAL".equalsIgnoreCase(form.getRole())) {
            Local local = new Local(
                    form.getUsername(), form.getEmail(), encodedPassword,
                    form.getFirstName(), form.getLastName(), form.getPhoneNumber(), form.getAddress()
            );
            localRepository.save(local);

        } else if ("BUSINESS_OWNER".equalsIgnoreCase(form.getRole())) {
            BusinessOwner bOwner = new BusinessOwner(
                    form.getUsername(), encodedPassword, form.getFirstName(), form.getLastName(),
                    form.getEmail(), form.getPhoneNumber(), form.getAddress()
            );
            businessOwnerRepository.save(bOwner);
        }

        return "redirect:/login";
    }
}

