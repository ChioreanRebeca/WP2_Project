package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import com.locafy.locafy.domain.BusinessOwner;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import com.locafy.locafy.repositories.BusinessRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class BusinessOwnerController {

    @Autowired
    PasswordEncoder passwordEncoder;

    private final BusinessOwnerRepository businessOwnerRepository;
    private final BusinessRepository businessRepository;

    public BusinessOwnerController(BusinessOwnerRepository businessOwnerRepository, BusinessRepository businessRepository) {
        this.businessOwnerRepository = businessOwnerRepository;
        this.businessRepository = businessRepository;
    }

    @GetMapping("/business-owner-home")
    public String businessOwnerHome(Model model, Principal principal) {
        BusinessOwner currentUser = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();
        List<Business> businesses = businessRepository.findAll();
        model.addAttribute("owner", currentUser);
        model.addAttribute("businesses", businesses);
        return "business-owner-home";
    }

    @GetMapping("/business-owner-profile")
    public String showBusinessOwnerProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        BusinessOwner currentOwner = businessOwnerRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("owner", currentOwner);
        return "business-owner-profile";
    }


    @PostMapping("/business-owner-profile")
    public String updateBusinessOwnerProfile(@ModelAttribute("owner") BusinessOwner formOwner,
                                     @RequestParam(required = false) String password,
                                     @RequestParam(required = false) String confirmPassword,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        BusinessOwner currentOwner = businessOwnerRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentOwner.setPhoneNumber(formOwner.getPhoneNumber());
        currentOwner.setAddress(formOwner.getAddress());

        if (password != null && !password.isEmpty() && password.equals(confirmPassword)) {
            currentOwner.setPassword(passwordEncoder.encode(password));
        }

        businessOwnerRepository.save(currentOwner);
        return "redirect:/business-owner-profile";
    }
}
