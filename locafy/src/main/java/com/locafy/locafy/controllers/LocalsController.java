package com.locafy.locafy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocalsController {
    @GetMapping("/locals-home")
    public String localsHomePage() {
        return "locals-home";
    }
}