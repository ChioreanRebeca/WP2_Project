package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.Favorites;
import com.locafy.locafy.domain.Local;
import com.locafy.locafy.repositories.FavoritesRepository;
import com.locafy.locafy.repositories.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/favorites")
public class FavoritesController {

    private final FavoritesRepository favoritesRepository;
    private final LocalRepository localRepository;

    @Autowired
    public FavoritesController(FavoritesRepository favoritesRepository, LocalRepository localRepository) {
        this.favoritesRepository = favoritesRepository;
        this.localRepository = localRepository;
    }

    @GetMapping
    public String showFavorites(Model model, Principal principal) {
        // Assuming the principal name is the email or username of the Local user
        Local localUser = localRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Local user not found"));

        List<Favorites> favorites = favoritesRepository.findByLocalUser(localUser);

        model.addAttribute("local", localUser);
        model.addAttribute("favorites", favorites);

        return "favorites";
    }
}

