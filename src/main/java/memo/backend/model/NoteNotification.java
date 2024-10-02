package memo.backend.model;

public class NoteNotification extends Notification {
    public NoteNotification(String recipient, String message) {
        super(recipient, message);
    }

    @Override
    public void sendNotification() {
        System.out.println("Enviando notificação de nota para " + recipient + ": " + message);
    }
}
