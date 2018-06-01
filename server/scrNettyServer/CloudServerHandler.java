package scrNettyServer;


import com.geekcloud.auth.AuthService;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import com.geekcloud.common.messaging.*;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.logging.Logger;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {

    private String login;
    private final Path SERVER_DIRECTORY; // нужна, чтобы знать, куда пользователя уже не пускать
    private Path currentDirectory; // нужна, чтобы знать, в какой серверной папке работает пользователь
    private final AuthService AUTH_SERVICE;
    private boolean isAuth;

    public CloudServerHandler (Path serverDirectory, AuthService AUTH_SERVICE) {
        this.SERVER_DIRECTORY = serverDirectory;
        this.AUTH_SERVICE = AUTH_SERVICE;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Logger.getGlobal().info("CLIENT CONNECTED: " + InetAddress.getLocalHost().getHostName());
        // Send greeting for a new connection.
        // ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        // ctx.write("It is " + new Date() + " now.\r\n");
        // ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {

            if (msg == null) return;

            if (!isAuth) {
                if (msg instanceof AuthMessage) {
                    String login = ((AuthMessage) msg).getLogin();
                    String password = ((AuthMessage) msg).getPassword();
                    if (AUTH_SERVICE.isLoginAccepted(login, password)) {
                        // Проверяем, на месте ли папка с файлами пользователя
                        if(Files.isDirectory(SERVER_DIRECTORY.resolve(login), LinkOption.NOFOLLOW_LINKS)) {
                            currentDirectory = SERVER_DIRECTORY.resolve(login);
                        } else {
                            // если папки нет, пользователя пускать нельзя
                            Logger.getGlobal().severe("NO DIRECTORY FOUND FOR VALID USER " + login);
                            ctx.write(new ResultMessage(ResultMessage.Result.FAILED));
                            ctx.flush();
                            throw new Exception("User directory not found");
                        }

                        this.login = login;
                        isAuth = true;

                        ChannelFuture channelFuture = ctx.writeAndFlush(ResultMessage.Result.OK);

                        System.out.println("OK");
                        // TODO вписать сюда отсылку списка файлов пользователю
                        ctx.flush();
                    } else {
                        ctx.write(new ResultMessage(ResultMessage.Result.FAILED));
                        ctx.flush();
                    }
                }
            } else {
                if (msg instanceof CommandMessage) {
                    switch (((CommandMessage)msg).getCommand() ) {
                        case DELETE:
                        case RENAME:
                            // TODO прописать обработку команд пользователя
                    }
                } else {
                    Logger.getGlobal().warning("Server received wrong object from " + login);
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
        isAuth = false;
        ctx.flush();
        ctx.close();
    }
}
