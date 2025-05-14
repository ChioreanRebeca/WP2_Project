package com.locafy.locafy.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Principal principal) {
        System.out.println("Principal after logout: " + principal); // should be null
        return "home";
    }

    @GetMapping("/map")
    public String MapPage() {
        return "map";
    }

    @GetMapping("/contact")
    public String ContactPage() {
        return "contact";
    }
}
