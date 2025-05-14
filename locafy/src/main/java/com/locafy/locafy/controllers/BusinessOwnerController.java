package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.Local;
import org.springframework.ui.Model;
import com.locafy.locafy.domain.BusinessOwner;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class BusinessOwnerController {

    private final BusinessOwnerRepository businessOwnerRepository;

    public BusinessOwnerController(BusinessOwnerRepository businessOwnerRepository) {
        this.businessOwnerRepository = businessOwnerRepository;
    }
    
    @GetMapping("/business-owner-home")
    public String businessOwnerHome(Model model, Principal principal) {
        BusinessOwner currentUser = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("owner", currentUser);
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
        currentOwner.setPassword(formOwner.getPassword());

        /*if (password != null && !password.isEmpty() && password.equals(confirmPassword)) {
            currentLocal.setPassword(passwordEncoder.encode(password));
        }*/

        businessOwnerRepository.save(currentOwner);
        return "redirect:/business-owner-profile";
    }

    @GetMapping("/business-owner")
    public String businessOwner() {
        return "businesses";
    }
}
