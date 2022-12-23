package hminepos.helper;

/**
 * Created by SaiMao on 5/22/2017.
 */
public class MessageBoxEvent {

    private String message;

    public MessageBoxEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
