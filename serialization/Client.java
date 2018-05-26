package com.flamexander.netty.servers.serialization;

import com.flamexander.netty.common.MyMessage;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

public class Client {
    //Пусть пока тут будут висеть для образца
    public static void main(String[] args) {
        ObjectEncoderOutputStream oeos = null;
        try (Socket socket = new Socket("localhost", 8189)) {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            MyMessage textMessage = new MyMessage("Hello Object");
            oeos.writeObject(textMessage);
            oeos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oeos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
