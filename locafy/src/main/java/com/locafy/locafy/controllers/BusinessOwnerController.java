package com.locafy.locafy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BusinessOwnerController {

    @GetMapping("/business-owner-home")
    public String businessOwnerHome() {
        return "business-owner-home";
    }

    @GetMapping("/business-owner-profile")
    public String businessOwnerProfile() {
        return "business-owner-profile";
    }

    @GetMapping("/business-owner")
    public String businessOwner() {
        return "business-owner";
    }
}
