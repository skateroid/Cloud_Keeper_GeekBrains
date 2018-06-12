package TestClientNoConnection;


import com.geekcloud.common.messaging.*;
import com.geekcloud.common.settings.ServerConst;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.nio.file.Paths;


public class ClientConnection implements ServerConst, Server_API {
    private boolean isAuthrozied;
    private Controller controller;

    public boolean isAuthrozied() {
        return isAuthrozied;
    }

    public void setAuthrozied(boolean authrozied) {
        isAuthrozied = authrozied;
    }

    public ClientConnection() {
    }

    public void init(Controller controller) {
        this.controller = controller;
        this.isAuthrozied = false;
    }

    public void auth(String login, String password) throws IOException {
        if (login.equals("login1") && password.equals("pass1")) {
            setAuthrozied(true);
            controller.switchWindows();
            controller.showMessage(null, "You're welcome");
            requestServerListRefresh();
        }
    }

    public void requestServerListRefresh() throws IOException {
        Message message = new FileListMessage(Paths.get("_cloud_repository/login1").toAbsolutePath(), Paths.get("_cloud_repository").toAbsolutePath());
        controller.getCloudFilesTable().setItems(FXCollections.observableArrayList(((FileListMessage)message).getFileList()));
    }
}
