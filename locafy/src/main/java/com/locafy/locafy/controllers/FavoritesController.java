package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.Business;
import com.locafy.locafy.domain.Favorites;
import com.locafy.locafy.domain.Image;
import com.locafy.locafy.domain.Local;
import com.locafy.locafy.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class FavoritesController {

    private final BusinessRepository businessRepository; // better than autowired
    private final ImageRepository imageRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    private final FavoritesRepository favoritesRepository;
    private final LocalRepository localRepository;


    public FavoritesController(BusinessRepository businessRepository,
                              ImageRepository imageRepository,
                              BusinessOwnerRepository businessOwnerRepository,
                              FavoritesRepository favoritesRepository,
                              LocalRepository localRepository) {
        this.businessRepository = businessRepository;
        this.imageRepository = imageRepository;
        this.businessOwnerRepository = businessOwnerRepository;
        this.favoritesRepository = favoritesRepository;
        this.localRepository = localRepository;
    }

    @GetMapping("/favorites")
    public String showFavorites(Model model, Principal principal) {
        Local localUser = localRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Favorites> favorites = favoritesRepository.findByLocalUser(localUser);

        for (Favorites favorite : favorites) {
            List<Image> images = imageRepository.findByBusinessId(favorite.getBusiness().getId());
            favorite.getBusiness().setImages(images);
        }
        model.addAttribute("local", localUser);
        model.addAttribute("favorites", favorites);

        return "favorites";
    }

    @PostMapping("/favorites/delete/{id}")
    public String deleteFavorite(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Favorites favorite = favoritesRepository.findById(id).orElse(null);

        if (favorite == null) {
            redirectAttributes.addFlashAttribute("error", "Favorite not found.");
            return "redirect:/favorites";
        }

        if (!favorite.getLocalUser().getUserName().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/favorites";
        }

        favoritesRepository.delete(favorite);
        redirectAttributes.addFlashAttribute("success", "Favorite removed successfully.");
        return "redirect:/favorites";
    }

    @PostMapping("/favorites/add/{id}")
    public String addFavorite(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Local localUser = localRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // Check if favorite already exists
        boolean alreadyExists = favoritesRepository.findByLocalUserAndBusiness(localUser, business).isPresent();
        if (alreadyExists) {
            redirectAttributes.addFlashAttribute("error", "Business is already in your favorites.");
            return "redirect:/locals-home";
        }

        Favorites favorite = new Favorites();
        favorite.setLocalUser(localUser);
        favorite.setBusiness(business);
        favoritesRepository.save(favorite);

        redirectAttributes.addFlashAttribute("success", "Business added to favorites!");
        return "redirect:/locals-home";
    }


}
