package fr.spam.backbookhub.features.notification.api.controllers;

import fr.spam.backbookhub.features.notification.api.dtos.NotificationResponse;
import fr.spam.backbookhub.features.notification.api.mappers.NotificationMapper;
import fr.spam.backbookhub.features.notification.models.Notification;
import fr.spam.backbookhub.features.notification.services.INotificationService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final INotificationService notificationService;
    private final NotificationMapper mapper;

    public NotificationController(NotificationMapper notificationMapper, INotificationService notificationService) {
        this.mapper = notificationMapper;
        this.notificationService = notificationService;
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<ServiceResponse<List<NotificationResponse>>> findByUserId(@Valid @PathVariable Long userId) {
//        return ResponseEntity
//                .status(200)
//                .body(
//                        notificationService
//                                .findByUserId(userId)
//                                .map(notifications -> notifications
//                                        .stream()
//                                        .map(mapper::toNotificationResponse)
//                                        .toList()
//                                )
//                );
//    }

    @PostMapping("")
    public ResponseEntity<ServiceResponse<NotificationResponse>> createNotification(@Valid @RequestBody Notification notification) {
        return ResponseEntity
                .status(201)
                .body(
                        notificationService
                                .create(notification)
                                .map(mapper::toNotificationResponse)
                );
    }
}