package com.eventmanagement.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class HomepageDtos {

    public static class DashboardResponse {
        public UserSummary user;
        public List<RecentOrder> recentOrders;
        public List<Notification> notifications;
        public DashboardStats stats;

        public DashboardResponse(UserSummary user, List<RecentOrder> recentOrders, 
                               List<Notification> notifications, DashboardStats stats) {
            this.user = user;
            this.recentOrders = recentOrders;
            this.notifications = notifications;
            this.stats = stats;
        }
    }

    public static class UserSummary {
        public Long id;
        public String username;
        public String firstName;
        public String lastName;
        public String email;
        public String profileImage;
        public String role;

        public UserSummary(Long id, String username, String firstName, String lastName, 
                          String email, String profileImage, String role) {
            this.id = id;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.profileImage = profileImage;
            this.role = role;
        }
    }

    public static class RecentOrder {
        public Long id;
        public String eventName;
        public String vendorName;
        public String status;
        public LocalDateTime createdAt;

        public RecentOrder(Long id, String eventName, String vendorName, String status, LocalDateTime createdAt) {
            this.id = id;
            this.eventName = eventName;
            this.vendorName = vendorName;
            this.status = status;
            this.createdAt = createdAt;
        }
    }

    public static class Notification {
        public Long id;
        public String title;
        public String message;
        public String type; // ORDER_UPDATE, EVENT_REMINDER, SYSTEM
        public boolean isRead;
        public LocalDateTime createdAt;

        public Notification(Long id, String title, String message, String type, boolean isRead, LocalDateTime createdAt) {
            this.id = id;
            this.title = title;
            this.message = message;
            this.type = type;
            this.isRead = isRead;
            this.createdAt = createdAt;
        }
    }

    public static class DashboardStats {
        public int totalOrders;
        public int pendingOrders;
        public int completedOrders;
        public int totalEvents;
        public int upcomingEvents;

        public DashboardStats(int totalOrders, int pendingOrders, int completedOrders, 
                            int totalEvents, int upcomingEvents) {
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.completedOrders = completedOrders;
            this.totalEvents = totalEvents;
            this.upcomingEvents = upcomingEvents;
        }
    }
}
