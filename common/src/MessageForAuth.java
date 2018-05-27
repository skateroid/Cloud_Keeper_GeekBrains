package src;

import java.io.Serializable;

public class MessageForAuth implements Serializable {
    //Этим классом передавать сообщения для авторизации
    private String text;

    public MessageForAuth(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
