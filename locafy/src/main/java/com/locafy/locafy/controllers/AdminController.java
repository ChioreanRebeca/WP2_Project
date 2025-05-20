package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.Business;
import com.locafy.locafy.domain.BusinessOwner;
import com.locafy.locafy.domain.Local;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import com.locafy.locafy.repositories.FavoritesRepository;
import com.locafy.locafy.repositories.LocalRepository;
import org.springframework.transaction.annotation.Transactional;
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
    private final FavoritesRepository favoritesRepository;

    public AdminController(LocalRepository localRepository, BusinessOwnerRepository businessOwnerRepository, FavoritesRepository favoritesRepository) {
        this.localRepository = localRepository;
        this.businessOwnerRepository = businessOwnerRepository;
        this.favoritesRepository = favoritesRepository;
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
    @Transactional
    public String deleteOwner(@PathVariable Long id) {
        BusinessOwner owner = businessOwnerRepository.findById(id).orElse(null);
        if (owner != null) {
            List<Long> businessIds = owner.getBusinesses().stream()
                    .map(Business::getId)
                    .collect(Collectors.toList());

            favoritesRepository.deleteAllByBusinessIds(businessIds);
            businessOwnerRepository.deleteById(id);
        }

        return "redirect:/admin-page";
    }

    @PostMapping("/admin-page/delete-local/{id}")
    public String deleteLocal(@PathVariable Long id) {
        favoritesRepository.deleteAllByLocalId(id);
        localRepository.deleteById(id);
        return "redirect:/admin-page";
    }
}
