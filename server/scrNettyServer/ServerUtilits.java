package scrNettyServer;

import com.geekcloud.common.messaging.FileListMessage;
import io.netty.channel.Channel;
import java.nio.file.Paths;

public class ServerUtilits {
    public static void sendFileList(Channel channel, String login) {
            FileListMessage fm = new FileListMessage(Paths.get(getUserPath(login)));
            channel.writeAndFlush(fm);
    }
    public static String getUserPath(String login) {
        return "_cloud_repository/" + login + "/";
    }
}
