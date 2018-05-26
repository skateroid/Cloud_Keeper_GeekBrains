package scrNettyServer;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import src.*;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {
    private boolean isAuth;
    private CloudServer cloudServer;
    private ServiceMessage serviceMessage;
    private String nick;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected...");
        cloudServer = new CloudServer();
        serviceMessage = new ServiceMessage();
        // Send greeting for a new connection.
        // ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        // ctx.write("It is " + new Date() + " now.\r\n");
        // ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            while (!isAuth) { //не заходит внутрь судя по всему, буду разбираться
                String logpass = (String) msg;
                if (logpass.startsWith(Server_API.AUTH)/*msg instanceof MessageForAuth*/) {
                    String[] logpass_arr = logpass.split(" ");
                    String nick = cloudServer.getBaseAuthService().getNickByLoginPass(logpass_arr[1], logpass_arr[2]);
                    if(nick != null){
                        if(!cloudServer.isNickBusy(nick)){
                            ctx.write(Server_API.AUTH_SUCCESSFUl + " " + nick);
                            ctx.flush();
                            this.nick = nick;
                            System.out.println("auth_OK");
                            break;
                        }else {
                            ctx.write("This account is already in use!");
                            ctx.flush();
                        }
                    }else {
                        ctx.write("Wrong login/password!");
                        ctx.flush();
                    }
                }
            }
            if (msg == null)
                return;
            System.out.println(msg.getClass());
            if (msg instanceof MyMessage) {
                System.out.println("Client text message: " + ((MyMessage) msg).getText());
            } else {
                System.out.printf("Server received wrong object!");
                return;
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
