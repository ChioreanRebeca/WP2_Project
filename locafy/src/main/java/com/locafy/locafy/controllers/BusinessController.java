package com.locafy.locafy.controllers;

import org.springframework.ui.Model;
import com.locafy.locafy.domain.Business;
import com.locafy.locafy.domain.Image;
import com.locafy.locafy.repositories.BusinessRepository;
import com.locafy.locafy.repositories.ImageRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/businesses")
public class BusinessController {
    private final BusinessRepository businessRepository;
    private final ImageRepository imageRepository;

    public BusinessController(BusinessRepository businessRepository, ImageRepository imageRepository) {
        this.businessRepository = businessRepository;
        this.imageRepository = imageRepository;
    }

    /*@GetMapping("/business-details")
    public String showBusinessDetails(Model model, Principal principal) {
        Business current = businessRepository.findByBusinessName(principal.getName()).orElseThrow();
        model.addAttribute("business", current);
        return "business-details";
    }*/


    @GetMapping("/business-details")
    public String viewBusinessProfile(@PathVariable Long id, Model model) {
        Business business = businessRepository.findById(id).orElseThrow();
        List<Image> images = imageRepository.findByBusinessId(id);
        model.addAttribute("business", business);
        model.addAttribute("images", images);
        return "business-details";
    }

    @PostMapping("/{id}/upload-image")
    public String uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        Business business = businessRepository.findById(id).orElseThrow();
        Image image = new Image();
        image.setBusiness(business);
        image.setData(file.getBytes());
        imageRepository.save(image);
        return "redirect:/businesses/" + id;
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image.getData());
    }
}
