package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.Business;
import com.locafy.locafy.domain.BusinessOwner;
import com.locafy.locafy.domain.Image;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import com.locafy.locafy.repositories.BusinessRepository;
import com.locafy.locafy.repositories.FavoritesRepository;
import com.locafy.locafy.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/businesses")
public class BusinessController {

    private final BusinessRepository businessRepository;
    private final ImageRepository imageRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;

    public BusinessController(BusinessRepository businessRepository,
                              ImageRepository imageRepository,
                              BusinessOwnerRepository businessOwnerRepository) {
        this.businessRepository = businessRepository;
        this.imageRepository = imageRepository;
        this.businessOwnerRepository = businessOwnerRepository;
    }

    // Show list of businesses for logged-in owner
    @GetMapping
    public String showBusinesses(Model model, Principal principal) {
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("owner", owner);
        return "businesses";
    }

    // Show empty form for creating new business
    @GetMapping("/business-details")
    public String newBusinessForm(Model model, Principal principal) {
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();
        Business business = new Business();
        business.setOwner(owner);  // pre-set owner
        model.addAttribute("business", business);
        model.addAttribute("images", List.of());  // no images yet
        return "business-details";
    }

    // Show form pre-filled with existing business data for editing
    @GetMapping("/business-details/{id}")
    public String editBusinessForm(@PathVariable Long id, Model model, Principal principal) {
        Business business = businessRepository.findById(id).orElseThrow();

        // Optional: check if logged-in user owns this business for security
        if (!business.getOwner().getUsername().equals(principal.getName())) {
            return "redirect:/businesses";  // or throw 403 Forbidden
        }

        List<Image> images = imageRepository.findByBusinessId(id);
        model.addAttribute("business", business);
        model.addAttribute("images", images);
        return "business-details";
    }

    // Handle form submit for both new and edited businesses
    @PostMapping("/business-details")
    public String saveBusiness(@ModelAttribute Business business, Principal principal) {
        // Fetch owner from principal (username)
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();
        business.setOwner(owner);

        // Save the business (new or updated)
        businessRepository.save(business);

        return "redirect:/businesses/business-details/" + business.getId();
    }

    // Upload image for a specific business
    @PostMapping("/{id}/upload-image")
    public String uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        Business business = businessRepository.findById(id).orElseThrow();

        // Optional: verify ownership before upload
        if (!business.getOwner().getUsername().equals(principal.getName())) {
            return "redirect:/businesses"; // or 403
        }

        Image image = new Image();
        image.setBusiness(business);
        image.setData(file.getBytes());
        imageRepository.save(image);

        return "redirect:/businesses/business-details/" + id;
    }

    // Serve business images
    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image.getData());
    }

    // Remove a business
    @PostMapping("/remove/{id}")
    public String removeBusiness(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();

        Business business = businessRepository.findById(id).orElseThrow();

        // Check if business belongs to current owner before deleting (optional safety)
        if (business.getOwner().getId() != owner.getId()) {
            // unauthorized delete attempt - handle accordingly (e.g., throw exception or ignore)
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/businesses";
        }

        businessRepository.delete(business);
        redirectAttributes.addFlashAttribute("success", "Business removed successfully.");
        return "redirect:/businesses";
    }

    // Save or update business
    @PostMapping({"/save"})
    public String saveOrUpdateBusiness(@PathVariable(required = false) Long id,
                                       @ModelAttribute("business") Business formBusiness,
                                       Principal principal,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();
        Business business;

        if (formBusiness.getId() != null) {
            // Update existing business
            business = businessRepository.findById(formBusiness.getId()).orElseThrow();
            if (business.getOwner().getId() != owner.getId()) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
                return "redirect:/businesses";
            }
            // Update fields
            business.setBusinessName(formBusiness.getBusinessName());
            business.setPhoneNumber(formBusiness.getPhoneNumber());
            business.setEmail(formBusiness.getEmail());
            business.setAddress(formBusiness.getAddress());
            business.setWebsite(formBusiness.getWebsite());
            business.setDescription(formBusiness.getDescription());
        } else {
            // Create new business
            business = new Business();
            business.setOwner(owner);
            business.setBusinessName(formBusiness.getBusinessName());
            business.setEmail(formBusiness.getEmail());
            business.setPhoneNumber(formBusiness.getPhoneNumber());
            business.setAddress(formBusiness.getAddress());
            business.setWebsite(formBusiness.getWebsite());
            business.setDescription(formBusiness.getDescription());
        }

        businessRepository.save(business);

        redirectAttributes.addFlashAttribute("success", "Business updated successfully.");

        // Redirect to business-details page of this business
        return "redirect:/businesses/business-details/" + business.getId();
    }

}
