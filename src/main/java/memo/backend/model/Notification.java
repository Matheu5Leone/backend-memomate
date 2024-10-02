package memo.backend.model;

import lombok.Data;

@Data
public abstract class Notification {
    protected String recipient;
    protected String message;

    public Notification(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    public abstract void sendNotification();
}