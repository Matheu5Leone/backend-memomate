package memo.backend.model;

public class CalendarEventNotification extends Notification {

    public CalendarEventNotification(String recipient, String message) {
        super(recipient, message);
    }

    @Override
    public void sendNotification() {
        System.out.println("Enviando notificação de evento para " + recipient + ": " + message);
    }
}
