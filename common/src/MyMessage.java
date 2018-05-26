package src;

import java.io.Serializable;

public class MyMessage implements Serializable {
    //Этот класс для пересылки файлов
    private static final long serialVersionUID = 5193392663743561680L;

    private String text;

    public String getText() {
        return text;
    }

    public MyMessage(String text) {
        this.text = text;
    }
}
