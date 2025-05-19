package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.BusinessOwner;
import com.locafy.locafy.domain.Local;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import com.locafy.locafy.repositories.LocalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class AdminController {

    private final LocalRepository localRepository;
    private final BusinessOwnerRepository businessOwnerRepository;

    public AdminController(LocalRepository localRepository, BusinessOwnerRepository businessOwnerRepository) {
        this.localRepository = localRepository;
        this.businessOwnerRepository = businessOwnerRepository;
    }

    @GetMapping("/admin-page")
    public String showAdminDashboard(Model model) {
        List<Local> locals = StreamSupport.stream(localRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        List<BusinessOwner> businessOwners = StreamSupport.stream(businessOwnerRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        model.addAttribute("locals", locals);
        model.addAttribute("owners", businessOwners);
        return "admin-page";
    }

    @PostMapping("/admin-page/delete-owner/{id}")
    public String deleteOwner(@PathVariable Long id) {
        businessOwnerRepository.deleteById(id);
        return "redirect:/admin-page";
    }

    @PostMapping("/admin-page/delete-local/{id}")
    public String deleteLocal(@PathVariable Long id) {
        localRepository.deleteById(id);
        return "redirect:/admin-page";
    }

}
