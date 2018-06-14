package scrNettyServer;

import com.geekcloud.common.messaging.FileListMessage_SimpleVersion;
import io.netty.channel.Channel;

import java.io.IOException;

import java.nio.file.Paths;

public class ServerUtilits {
    public static void sendFileList(Channel channel, String login) {
            FileListMessage_SimpleVersion fm = new FileListMessage_SimpleVersion(Paths.get(getUserPath(login)));
            channel.writeAndFlush(fm);
    }
    public static String getUserPath(String login) {
        return "_cloud_repository/" + login + "/";
    }
}
