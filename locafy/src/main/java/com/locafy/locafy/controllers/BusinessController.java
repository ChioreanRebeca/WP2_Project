package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.*;
import com.locafy.locafy.repositories.*;
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
@RequestMapping("/businesses") // it means it is on the class, and it is a prefix for all other links in this controller
public class BusinessController {

    private final BusinessRepository businessRepository; // better than autowired
    private final ImageRepository imageRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    private final FavoritesRepository favoritesRepository;
    private final LocalRepository localRepository;

    public BusinessController(BusinessRepository businessRepository,
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

    @GetMapping
    public String showBusinesses(Model model, Principal principal) {
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();

        owner.getBusinesses().forEach(business -> {
            List<Image> images = imageRepository.findByBusinessId(business.getId());
            if (!images.isEmpty()) {
                business.setImages(List.of(images.get(0))); // the first image in the list of images for each business
            }
        });

        model.addAttribute("owner", owner);
        return "businesses";
    }

    @GetMapping("/business-details")
    public String newBusinessForm(Model model, Principal principal) {
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();
        Business business = new Business();
        business.setOwner(owner);
        model.addAttribute("business", business);
        model.addAttribute("images", List.of());
        return "business-details";
    }

    @GetMapping("/business-details/{id}")
    public String editBusinessForm(@PathVariable Long id, Model model, Principal principal) {
        Business business = businessRepository.findById(id).orElseThrow();

        if (!business.getOwner().getUsername().equals(principal.getName())) {
            return "redirect:/businesses";
        }

        List<Image> images = imageRepository.findByBusinessId(id);
        model.addAttribute("business", business);
        model.addAttribute("images", images);
        return "business-details";
    }

    @PostMapping("/business-details")
    public String saveBusiness(@ModelAttribute Business business, Principal principal) {
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();
        business.setOwner(owner);
        businessRepository.save(business);

        return "redirect:/businesses/business-details/" + business.getId();
    }

    @PostMapping("/{id}/upload-image")
    public String uploadImages(@PathVariable Long id, @RequestParam("files") MultipartFile[] files, Principal principal) throws IOException {
        Business business = businessRepository.findById(id).orElseThrow();

        if (!business.getOwner().getUsername().equals(principal.getName())) {
            return "redirect:/businesses";
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Image image = new Image();
                image.setBusiness(business);
                image.setData(file.getBytes());
                imageRepository.save(image);
            }
        }

        return "redirect:/businesses/business-details/" + id;
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image.getData());
    }

    @PostMapping("/remove/{id}")
    public String removeBusiness(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();

        Business business = businessRepository.findById(id).orElseThrow();
        
        if (business.getOwner().getId() != owner.getId()) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/businesses";
        }

        businessRepository.delete(business);
        redirectAttributes.addFlashAttribute("success", "Business removed successfully.");
        return "redirect:/businesses";
    }

    @PostMapping({"/save"})
    public String saveOrUpdateBusiness(@PathVariable(required = false) Long id,
                                       @ModelAttribute("business") Business formBusiness,
                                       @RequestParam(value = "files", required = false) MultipartFile[] files,
                                       Principal principal,
                                       RedirectAttributes redirectAttributes,
                                       Model model) throws IOException {
        BusinessOwner owner = businessOwnerRepository.findByUsername(principal.getName()).orElseThrow();
        Business business;

        if (formBusiness.getId() != null) {
            business = businessRepository.findById(formBusiness.getId()).orElseThrow();
            if (business.getOwner().getId() != owner.getId()) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
                return "redirect:/businesses";
            }
            business.setBusinessName(formBusiness.getBusinessName());
            business.setPhoneNumber(formBusiness.getPhoneNumber());
            business.setEmail(formBusiness.getEmail());
            business.setAddress(formBusiness.getAddress());
            business.setWebsite(formBusiness.getWebsite());
            business.setDescription(formBusiness.getDescription());
        } else {
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

        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    Image image = new Image();
                    image.setBusiness(business);
                    image.setData(file.getBytes());
                    imageRepository.save(image);
                }
            }
        }

        redirectAttributes.addFlashAttribute("success", "Business updated successfully.");

        return "redirect:/businesses/business-details/" + business.getId();
    }

    @PostMapping("/images/{imageId}/delete")
    public String deleteImage(@PathVariable Long imageId, Principal principal, RedirectAttributes redirectAttributes) {
        Image image = imageRepository.findById(imageId).orElse(null);

        if (image == null) {
            redirectAttributes.addFlashAttribute("error", "Image not found.");
            return "redirect:/businesses";
        }

        Business business = image.getBusiness();

        if (!business.getOwner().getUsername().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/businesses";
        }

        Long businessId = business.getId();
        imageRepository.delete(image);

        redirectAttributes.addFlashAttribute("success", "Image deleted successfully.");
        return "redirect:/businesses/business-details/" + businessId;
    }

    @GetMapping("/business-overview/{id}")
    public String getBusinessOverview(@PathVariable Long id, Model model) {
        Business business = businessRepository.findById(id).orElseThrow();

        List<Image> images = imageRepository.findByBusinessId(id);
        business.setImages(images);

        model.addAttribute("business", business);
        return "business-overview";
    }
}
