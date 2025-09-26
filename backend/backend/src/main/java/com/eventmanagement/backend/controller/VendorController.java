package com.eventmanagement.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventmanagement.backend.model.Vendor;
import com.eventmanagement.backend.repository.VendorRepository;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public ResponseEntity<?> listVendors(
        @RequestParam(value = "eventType", required = false) String eventType,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "q", required = false) String keyword
    ) {
        List<Vendor> vendors;

        if (category != null && !category.isBlank()) {
            vendors = vendorRepository.findByCategoryIgnoreCase(category.trim());
        } else if (keyword != null && !keyword.isBlank()) {
            vendors = vendorRepository.searchByKeyword(keyword.trim());
        } else {
            vendors = vendorRepository.findAll();
        }

        // eventType can map to categories heuristically for now (e.g., wedding -> common categories)
        if (eventType != null && !eventType.isBlank()) {
            String type = eventType.trim().toLowerCase();
            vendors = vendors.stream().filter(v -> {
                String cat = v.getCategory() != null ? v.getCategory().toLowerCase() : "";
                return switch (type) {
                    case "wedding" -> cat.contains("cater") || cat.contains("photo") || cat.contains("decor") || cat.contains("planner") || cat.contains("music");
                    case "corporate" -> cat.contains("av") || cat.contains("cater") || cat.contains("venue") || cat.contains("planner");
                    case "birthday" -> cat.contains("cater") || cat.contains("decor") || cat.contains("entertain") || cat.contains("cake");
                    default -> true;
                };
            }).collect(Collectors.toList());
        }

        return ResponseEntity.ok(vendors);
    }
}


