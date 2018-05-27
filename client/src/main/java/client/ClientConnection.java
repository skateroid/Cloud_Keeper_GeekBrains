package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import src.MessageForAuth;
import src.MyMessage;
import src.ServerConst;
import src.Server_API;

//import static common.Server_API.AUTH_SUCCESSFUl;


public class ClientConnection implements ServerConst, Server_API {
    Socket socket;
    DataOutputStream out;
    DataInputStream in;
    ObjectEncoderOutputStream oeos;
    private boolean isAuthrozied = false;

    public boolean isAuthrozied() {
        return isAuthrozied;
    }

    public void setAuthrozied(boolean authrozied) {
        isAuthrozied = authrozied;
    }

    public ClientConnection() {
    }

    public void init(Controller view) { //lazy init
        try {
            this.socket = new Socket(SERVER_URL, PORT);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            this.oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        System.out.println("here");
                        if (message.startsWith(AUTH_SUCCESSFUl)) {
                            setAuthrozied(true);
                            view.switchWindows();
                            break;
                        }
                        view.showMessage(message);
                    }
                    while (true) {
                        String message = in.readUTF();
                        String[] elements = message.split(" ");
                        if (message.startsWith(SYSTEM_SYMBOL)) {
                            if (elements[0].equals(CLOSE_CONNECTION)) {
                                setAuthrozied(false);
                                view.showMessage(message.substring(CLOSE_CONNECTION.length() + 1));
                                view.switchWindows();
                            } else if (message.startsWith(USERS_LIST)) {
                                String[] users = message.split(" ");
                                Arrays.sort(users);
                                System.out.println(Arrays.toString(users));
                                view.showUsersList(users);
                            }

                        } else {
                            view.showMessage(message);
                        }
                    }
                } catch (IOException e) {
                } finally {
                    disconnect();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void auth(String login, String password) {
        try {
            MessageForAuth messageForAuth = new MessageForAuth(login + " " + password);
            oeos.writeObject(messageForAuth);
            oeos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            out.writeUTF(CLOSE_CONNECTION);
            oeos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
