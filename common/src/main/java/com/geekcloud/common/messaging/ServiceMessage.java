package com.geekcloud.common.messaging;

public class ServiceMessage {
    //Класс для отправки служебных сообщений клиенту
    //Надо придумать куда их в клиентском интерфейсе выводить
    private String text;

    public ServiceMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
