package memo.backend.service;

import memo.backend.model.Notification;
import memo.backend.util.NotificationFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationFactory notificationFactory;

    public NotificationService() {
        this.notificationFactory = new NotificationFactory();
    }

    public void sendNotification(String type, String recipient, String message) {
        Notification notification = notificationFactory.createNotification(type, recipient, message);
        notification.sendNotification();
    }
}