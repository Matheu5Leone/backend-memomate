package memo.backend.model;

public class FriendshipNotification extends Notification {

    public FriendshipNotification(String recipient, String message) {
        super(recipient, message);
    }

    @Override
    public void sendNotification() {
        System.out.println("Enviando notificação de amizade para " + recipient + ": " + message);
    }
}
