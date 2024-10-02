package memo.backend.util;

import memo.backend.model.*;

public class NotificationFactory {

    public Notification createNotification(String type, String recipient, String message) {
        switch (type) {
            case "friendship":
                return new FriendshipNotification(recipient, message);
            case "note":
                return new NoteNotification(recipient, message);
            case "calendar":
                return new CalendarEventNotification(recipient, message);
            default:
                throw new IllegalArgumentException("Tipo de notificação desconhecido");
        }
    }
}