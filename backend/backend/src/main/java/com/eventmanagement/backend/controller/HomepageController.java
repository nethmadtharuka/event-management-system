package com.eventmanagement.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventmanagement.backend.dto.HomepageDtos;
import com.eventmanagement.backend.model.Notification;
import com.eventmanagement.backend.service.HomepageService;

@RestController
@RequestMapping("/api/homepage")
public class HomepageController {

    private final HomepageService homepageService;
    private static final Logger log = LoggerFactory.getLogger(HomepageController.class);

    public HomepageController(HomepageService homepageService) {
        this.homepageService = homepageService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(Authentication authentication) {
        try {
            String username = authentication.getName();
            HomepageDtos.DashboardResponse dashboard = homepageService.getDashboardData(username);
            return ResponseEntity.ok(dashboard);
        } catch (Exception ex) {
            log.error("Failed to get dashboard data for user: {}", authentication.getName(), ex);
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to load dashboard data"));
        }
    }

    @PostMapping("/notifications/{notificationId}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long notificationId, Authentication authentication) {
        try {
            String username = authentication.getName();
            homepageService.markNotificationAsRead(notificationId, username);
            return ResponseEntity.ok(java.util.Map.of("message", "Notification marked as read"));
        } catch (Exception ex) {
            log.error("Failed to mark notification as read: {}", notificationId, ex);
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to mark notification as read"));
        }
    }

    @PostMapping("/notifications/read-all")
    public ResponseEntity<?> markAllNotificationsAsRead(Authentication authentication) {
        try {
            String username = authentication.getName();
            homepageService.markAllNotificationsAsRead(username);
            return ResponseEntity.ok(java.util.Map.of("message", "All notifications marked as read"));
        } catch (Exception ex) {
            log.error("Failed to mark all notifications as read for user: {}", authentication.getName(), ex);
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to mark all notifications as read"));
        }
    }

    @PostMapping("/notifications/create")
    public ResponseEntity<?> createNotification(@RequestParam String title, 
                                               @RequestParam String message,
                                               @RequestParam Notification.NotificationType type,
                                               Authentication authentication) {
        try {
            String username = authentication.getName();
            homepageService.createNotification(username, title, message, type);
            return ResponseEntity.ok(java.util.Map.of("message", "Notification created successfully"));
        } catch (Exception ex) {
            log.error("Failed to create notification for user: {}", authentication.getName(), ex);
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to create notification"));
        }
    }
}
