package com.geekcloud.auth;

import com.geekcloud.auth.exceptions.UserAlreadyExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAuthService implements AuthService{

    private Map <String, User> users;

    private class User {
        private String login;
        private String password;
        private String nick;

        User(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
        String getPassword() { return password; }
        String getNick() { return nick; }
    }

    public BaseAuthService(){
        this.users = new HashMap<>();
    }

    public void start(){
        testPrefill(); // TODO убрать по завершении разработки
    }

    public void stop(){}

    private void testPrefill () {
        users.put("login1", new User ("login1", "pass1", "Rick"));
        users.put("login2", new User ("login2", "pass2", "Morty"));
        users.put("login3", new User ("login3", "pass3", "Bet"));
    }

    public String getNickByLoginPass(String login, String pass){
        return users.containsKey(login) ? users.get(login).getNick() : null;
    }

    public boolean isLoginAccepted(String username, String password) {
        if (!users.containsKey(username)) return false;
        return users.get(username).getPassword().equals(password);
    }

    public boolean isUserNameVacant(String username) {
        return !users.containsKey(username);
    }

    public synchronized void registerNewUser(String username, String password, String nick) throws UserAlreadyExistsException {
        if (users.containsKey(username)) throw new UserAlreadyExistsException();
        users.put(username, new User(username, password, nick));
    }

    public void deleteUser(String username, String password) {
        if(isLoginAccepted(username, password)) {
            users.remove(username);
        }
    }

    public List<String> listRegisteredUsers() {
        return new ArrayList<>(users.keySet());
    }
}
