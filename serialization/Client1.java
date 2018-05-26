package com.flamexander.netty.servers.serialization;

import com.flamexander.netty.common.MyMessage;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client1 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8189);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        MyMessage mm = new MyMessage("Hello, Server!");
        oos.writeObject(mm);
        oos.close();
        socket.close();
    }
}
