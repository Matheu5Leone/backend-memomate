package memo.backend.controller;

import memo.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public void sendNotification(
            @RequestParam String type,
            @RequestParam String recipient,
            @RequestParam String message) {
        notificationService.sendNotification(type, recipient, message);
    }
}
