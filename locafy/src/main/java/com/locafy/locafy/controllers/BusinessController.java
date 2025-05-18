package com.locafy.locafy.controllers;

import com.locafy.locafy.domain.*;
import com.locafy.locafy.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/businesses") // it means it is on the class, and it is a prefix for all other links in this controller
public class BusinessController {

    private final BusinessRepository businessRepository; //this is another way to do it
    private final ImageRepository imageRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    @Autowired
    private FavoritesRepository favoritesRepository; //this is one way to do it
    @Autowired
    private ReviewsRepository reviewsRepository;
    @Autowired
    private LocalRepository localRepository;

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

        // Attach first image ID for each business
        owner.getBusinesses().forEach(business -> {
            List<Image> images = imageRepository.findByBusinessId(business.getId());
            if (!images.isEmpty()) {
                business.setImages(List.of(images.get(0))); // the first image in the list of images for each business
            }
        });

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

        // Save the business first (so it has an ID)
        businessRepository.save(business);

        // Handle uploaded images (if any)
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

    /*@ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error: upload images smaller than 20MB");
        // Redirect to the referring page (where the upload was attempted)
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    Doar daca mai am chef...daca imaginile depasesc de 50MB
    */

    @PostMapping("/images/{imageId}/delete")
    public String deleteImage(@PathVariable Long imageId, Principal principal, RedirectAttributes redirectAttributes) {
        Image image = imageRepository.findById(imageId).orElse(null);

        if (image == null) {
            redirectAttributes.addFlashAttribute("error", "Image not found.");
            return "redirect:/businesses";
        }

        Business business = image.getBusiness();

        // Check if logged-in user owns this business
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

        // Fetch all images for the business
        List<Image> images = imageRepository.findByBusinessId(id);
        business.setImages(images); // Attach images to the business object

        model.addAttribute("business", business);
        return "business-overview"; // This should be your Thymeleaf HTML file: business-overview.html
    }

}
