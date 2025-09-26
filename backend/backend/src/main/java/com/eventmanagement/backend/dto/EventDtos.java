package com.eventmanagement.backend.dto;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EventDtos {
    public static class CreateEventRequest {
        @NotBlank
        public String title;
        public String description;
        @NotNull
        public LocalDateTime startAt;
        @NotNull
        public LocalDateTime endAt;
        public String location;
        public Set<Long> vendorIds; // optional
    }

    public static class EventResponse {
        public Long id;
        public String title;
        public String description;
        public LocalDateTime startAt;
        public LocalDateTime endAt;
        public String location;
        public String createdBy;
        public java.util.List<VendorSummary> vendors;
    }

    public static class VendorSummary {
        public Long id;
        public String name;
        public String category;
    }
}


