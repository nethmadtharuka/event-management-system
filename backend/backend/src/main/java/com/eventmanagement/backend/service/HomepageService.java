package com.eventmanagement.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventmanagement.backend.dto.HomepageDtos;
import com.eventmanagement.backend.model.Notification;
import com.eventmanagement.backend.model.Order;
import com.eventmanagement.backend.model.User;
import com.eventmanagement.backend.repository.NotificationRepository;
import com.eventmanagement.backend.repository.OrderRepository;
import com.eventmanagement.backend.repository.UserRepository;

@Service
@Transactional
public class HomepageService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;

    public HomepageService(UserRepository userRepository, OrderRepository orderRepository, 
                          NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.notificationRepository = notificationRepository;
    }

    public HomepageDtos.DashboardResponse getDashboardData(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Get user summary
        HomepageDtos.UserSummary userSummary = new HomepageDtos.UserSummary(
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getProfileImage(),
            user.getRole().name()
        );

        // Get recent orders (last 5)
        List<Order> recentOrders = orderRepository.findByUserOrderByCreatedAtDesc(user)
            .stream()
            .limit(5)
            .collect(Collectors.toList());

        List<HomepageDtos.RecentOrder> recentOrderDtos = recentOrders.stream()
            .map(order -> new HomepageDtos.RecentOrder(
                order.getId(),
                order.getEvent().getTitle(),
                order.getVendor().getName(),
                order.getStatus(),
                order.getCreatedAt()
            ))
            .collect(Collectors.toList());

        // Get notifications (last 10)
        List<Notification> notifications = notificationRepository.findRecentNotificationsByUser(user)
            .stream()
            .limit(10)
            .collect(Collectors.toList());

        List<HomepageDtos.Notification> notificationDtos = notifications.stream()
            .map(notification -> new HomepageDtos.Notification(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType().name(),
                notification.getIsRead(),
                notification.getCreatedAt()
            ))
            .collect(Collectors.toList());

        // Get dashboard stats
        List<Order> allUserOrders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        int totalOrders = allUserOrders.size();
        int pendingOrders = (int) allUserOrders.stream().filter(o -> "PENDING".equals(o.getStatus())).count();
        int completedOrders = (int) allUserOrders.stream().filter(o -> "CONFIRMED".equals(o.getStatus())).count();

        // For now, we'll use placeholder values for events since we don't have event management fully implemented
        int totalEvents = 0;
        int upcomingEvents = 0;

        HomepageDtos.DashboardStats stats = new HomepageDtos.DashboardStats(
            totalOrders, pendingOrders, completedOrders, totalEvents, upcomingEvents
        );

        return new HomepageDtos.DashboardResponse(userSummary, recentOrderDtos, notificationDtos, stats);
    }

    public void markNotificationAsRead(Long notificationId, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to notification");
        }
        
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    public void markAllNotificationsAsRead(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Notification> unreadNotifications = notificationRepository.findByUserAndIsReadOrderByCreatedAtDesc(user, false);
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    public void createNotification(String username, String title, String message, Notification.NotificationType type) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        
        notificationRepository.save(notification);
    }
}
