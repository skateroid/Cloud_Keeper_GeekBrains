package com.geekcloud.common.messaging;

import java.io.Serializable;

//Этим классом передавать сообщения для авторизации
public class AuthMessage extends Message /*implements Serializable*/ {

   // private static final long serialVersionUID = 5193392663743561680L;

    private String login;
    private String password;

    public AuthMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
