package scrNettyServer;

import com.geekcloud.auth.AuthService;
import com.geekcloud.auth.SqliteAuthService;
import com.geekcloud.auth.exceptions.AuthServiceException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import com.geekcloud.common.settings.ServerConst;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class CloudServer {

    private final Path ROOT = Paths.get("_cloud_repository");
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // 10 mb
    private AuthService authService;

    private void run() {

        // Для хранения файлов на сервере выделяем директорию.
        // В ней будет лежать файл базы данных пользователей для сервиса авторизации
        // и файлы пользователей, разложенные по папкам с именами логинов.
        // Шаг 1: проверяем наличие папки на сети, при отсутствии - создаем.
        if(Files.exists(ROOT, LinkOption.NOFOLLOW_LINKS)) {
            Logger.getGlobal().info("ROOT DIRECTORY " + ROOT);
        } else {
            try {
                Files.createDirectory(ROOT);
                Logger.getGlobal().warning("ATTENTION! --- NEW ROOT DIRECTORY CREATED " + ROOT);
            } catch (IOException e) {
                Logger.getGlobal().severe("FATAL ERROR! FAILED TO CREATE ROOT DIRECTORY. \nSERVER CLOSED");
                return;
            }
        }

        // Шаг 2: Запускаем службу авторизации
        try {
            authService = new SqliteAuthService(ROOT.resolve("users").toAbsolutePath().toString());
            authService.start();
            Logger.getGlobal().info("AUTHORISATION SERVICE STARTED");
        } catch (AuthServiceException e) {
            Logger.getGlobal().severe("FATAL ERROR! FAILED TO START AUTHORISATION SERVICE. \nSERVER CLOSED");
            return;
        }

        // Шаг 3: Запускаем нетти-сервер
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(mainGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new CloudServerHandler(ROOT.toAbsolutePath(), authService)
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(ServerConst.PORT).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            authService.stop();
        }
    }

    public static void main(String[] args) {
        new CloudServer().run();
    }
}
