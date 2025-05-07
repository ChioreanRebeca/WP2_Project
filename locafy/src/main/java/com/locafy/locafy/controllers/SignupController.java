package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.Local;
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
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("local", new Local());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute Local local) {
        local.setPassword(passwordEncoder.encode(local.getPassword()));
        localRepository.save(local);
        return "redirect:/login";
    }
}

