package com.geekcloud.common.messaging;

//Этим классом передавать сообщения для авторизации
public class AuthMessage extends Message {

    private String login;
    private String password;

    public AuthMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
