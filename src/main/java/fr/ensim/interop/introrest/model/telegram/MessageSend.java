package fr.ensim.interop.introrest.model.telegram;

public class MessageSend {

    public String chat_id = "5556461794";
    public String text;

    public MessageSend(String message) {
        this.text = message;
    }

    public String getMessage() {
        return text;
    }

    public void setMessage(String message) {
        this.text = message;
    }
}
