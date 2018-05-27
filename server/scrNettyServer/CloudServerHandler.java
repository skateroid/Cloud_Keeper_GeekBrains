package scrNettyServer;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import src.*;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {
    private boolean isAuth;
    private CloudServer cloudServer = new CloudServer();
    private ServiceMessage serviceMessage;
    private String nick;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected...");
        // Send greeting for a new connection.
        // ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        // ctx.write("It is " + new Date() + " now.\r\n");
        // ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            while (!isAuth) { //не заходит внутрь судя по всему, буду разбираться
                if (/*logpass.startsWith(Server_API.AUTH)*/msg instanceof MessageForAuth) {
                    String logpass = ((MessageForAuth) msg).getText();
                    String[] logpass_arr = logpass.split(" ");
                    String nick = cloudServer.getBaseAuthService().getNickByLoginPass(logpass_arr[0], logpass_arr[1]);
                    if (nick != null) {
                        if (!cloudServer.isNickBusy(nick)) {
                            ctx.write(Server_API.AUTH_SUCCESSFUl + " " + nick);
                            ctx.flush();
                            this.nick = nick;
                            System.out.println("auth_OK");
                            isAuth = true;
                            MessageForAuth auth_ok = new MessageForAuth(Server_API.AUTH_SUCCESSFUl);
                            return;
                        } else {
                            System.out.println("This account is already in use!");
                            return;
                            //ctx.flush();
                        }
                    } else {
                        System.out.println("Wrong login/password!");
                        return;
                        //ctx.flush();
                    }
                }
            }
            if (isAuth) {
                if (msg == null)
                    return;
                System.out.println(msg.getClass());
                if (msg instanceof MyMessage) {
                    System.out.println("Client text message: " + ((MyMessage) msg).getText());
                } else {
                    System.out.printf("Server received wrong object!");
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //ctx.flush();
        ctx.close();
    }
}
