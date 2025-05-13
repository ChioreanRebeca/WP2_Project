package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.Local;
import com.locafy.locafy.repositories.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LocalsController {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/locals-home")
    public String localsHomePage() {
        return "locals-home";
    }

    @GetMapping("/local-profile")
    public String localProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Local local = localRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("local", local);
        return "local-profile";
    }

    @PostMapping("/local-profile")
    public String updateLocalProfile(@ModelAttribute("local") Local formLocal,
                                     @RequestParam(required = false) String password,
                                     @RequestParam(required = false) String confirmPassword,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        Local currentLocal = localRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentLocal.setPhoneNumber(formLocal.getPhoneNumber());
        currentLocal.setAddress(formLocal.getAddress());

        if (password != null && !password.isEmpty() && password.equals(confirmPassword)) {
            currentLocal.setPassword(passwordEncoder.encode(password));
        }

        localRepository.save(currentLocal);
        return "redirect:/local-profile";
    }

    @GetMapping("/favorites")
    public String FavoritesPage() {
        return "favorites";
    }
}