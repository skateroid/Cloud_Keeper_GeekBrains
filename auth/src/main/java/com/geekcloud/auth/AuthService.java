package com.geekcloud.auth;

import com.geekcloud.auth.exceptions.AuthServiceException;
import com.geekcloud.auth.exceptions.DatabaseConnectionException;
import com.geekcloud.auth.exceptions.UserAlreadyExistsException;

import java.util.List;

public interface AuthService {
    void start() throws AuthServiceException;
    void stop();
    String getNickByLoginPass(String username, String password);
    boolean isLoginAccepted(String username, String password);
    boolean isUserNameVacant(String username);
    void registerNewUser (String username, String password, String nick) throws UserAlreadyExistsException, DatabaseConnectionException;
    void deleteUser (String username, String password) throws DatabaseConnectionException;
    List<String> listRegisteredUsers();
}