package com.eventmanagement.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventmanagement.backend.model.Vendor;
import com.eventmanagement.backend.repository.VendorRepository;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final VendorRepository vendorRepository;

    public ChatController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // Simple AI-like response endpoint
    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody Map<String, String> body) {
        String question = body.getOrDefault("question", "").toLowerCase();
        if (question.contains("wedding") && (question.contains("vendor") || question.contains("vendors"))) {
            List<Vendor> vendors = vendorRepository.findAll();
            // Filter basic set for demo
            List<Vendor> weddingVendors = vendors.stream()
                .filter(v -> {
                    String cat = v.getCategory() == null ? "" : v.getCategory().toLowerCase();
                    return cat.contains("cater") || cat.contains("photo") || cat.contains("decor") || cat.contains("planner");
                }).toList();

            return ResponseEntity.ok(Map.of(
                "answer", "We have the following vendors for weddings: Caterers, Photographers, Planners, and Decorators.",
                "vendors", weddingVendors,
                "suggestions", List.of(
                    Map.of("label", "See More Vendors", "action", "see_more", "params", Map.of("eventType", "wedding")),
                    Map.of("label", "View Wedding Planners", "action", "filter", "params", Map.of("category", "planner")),
                    Map.of("label", "Contact a Caterer", "action", "filter", "params", Map.of("category", "cater"))
                )
            ));
        }

        // Generic fallback
        return ResponseEntity.ok(Map.of(
            "answer", "How can I help with your event? You can ask for vendors by type or event.",
            "suggestions", List.of(
                Map.of("label", "Wedding Vendors", "action", "see_more", "params", Map.of("eventType", "wedding")),
                Map.of("label", "Corporate Planners", "action", "see_more", "params", Map.of("eventType", "corporate"))
            )
        ));
    }
}


