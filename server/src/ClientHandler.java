package src;

import com.geekcloud.common.messaging.Server_API;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Server_API {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nick;
    public ClientHandler(Server server, Socket socket){
        try{
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            this.nick = "undefined";
        }catch(IOException e){
            e.printStackTrace();
        }
        new Thread(()-> {
            try{
                //Auth
                while(true){
                    String message = in.readUTF();
                    if(message.startsWith(Server_API.AUTH)){
                        String[] elements = message.split(" ");
                        String nick = server.getAuthService().getNickByLoginPass(elements[1], elements[2]);
                        if(nick != null){
                            if(!server.isNickBusy(nick)){
                                sendMessage(Server_API.AUTH_SUCCESSFUl + " " + nick);
                                this.nick = nick;
                                server.broadcastUsersList();
                                server.broadcast(this.nick + " has entered the chat room");
                                break;
                            }else sendMessage("This account is already in use!");
                        }else sendMessage("Wrong login/password!");
                    }else sendMessage("You should authorize first!");
                }
                while(true){
                    String message = in.readUTF();
                    if(message.startsWith(Server_API.SYSTEM_SYMBOL)){
                        if(message.equalsIgnoreCase(Server_API.CLOSE_CONNECTION)) break;
                        else if(message.startsWith(Server_API.PRIVATE_MESSAGE)){ // /w nick messaging
                            String nameTo = message.split(" ")[1];
                            String messageText = message.substring(Server_API.PRIVATE_MESSAGE.length() + nameTo.length() + 2);
                            server.sendPrivateMessage(this, nameTo, messageText);
                        }else sendMessage("Command doesn't exist!");
                    }else {
                        System.out.println("client " + message);
                        server.broadcast(this.nick + " " + message);
                    }
                }
            }catch(IOException e){
            }finally{
                disconnect();
            }
        }).start();
    }
    public void sendMessage(String msg){
        try{
            out.writeUTF(msg);
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void disconnect(){
        sendMessage(Server_API.CLOSE_CONNECTION + " You have been disconnected!");
        server.unsubscribeMe(this);
        try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public String getNick(){
        return nick;
    }
}
