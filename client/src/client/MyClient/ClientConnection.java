package MyClient;


import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import com.geekcloud.common.settings.ServerConst;
import com.geekcloud.common.messaging.*;
import javafx.application.Platform;

public class ClientConnection implements ServerConst {
    private Socket socket;
    private ObjectEncoderOutputStream oeos;
    private ObjectDecoderInputStream odis;
    private boolean isAuthrozied;
    private String username;
    private boolean isConnected;

    public boolean isAuthrozied() {
        return isAuthrozied;
    }

    public void setAuthrozied(boolean authrozied) {
        isAuthrozied = authrozied;
    }

    public ClientConnection() {
    }

    public void init(Controller controller) {
        try {
            this.socket = new Socket(SERVER_URL, PORT);
            this.oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            this.odis = new ObjectDecoderInputStream(socket.getInputStream());
            this.isAuthrozied = false;
            this.isConnected = true;

            new Thread(() -> {
                try {
                    while (true) {
                        Object message = odis.readObject();
                        if (message != null) {
                            if (message instanceof ResultMessage.Result) {
                                if (message == ResultMessage.Result.OK) {
                                    setAuthrozied(true);
                                    controller.switchWindows();
                                    controller.showMessage(null, "You're welcome");
                                    break;
                                }
                            }
                        }
                    }
                    while (isConnected) {
                        Object message = odis.readObject();
                        if (message != null) {
                            if (message instanceof FileListMessage) {
                                FileListMessage fm = (FileListMessage) message;
                                Platform.runLater(() -> {
                                    controller.getCloudListItems().clear();
                                    controller.getCloudListItems().addAll(fm.getFiles());
                                });
                            }
                            if (message instanceof DataTransferMessage) {
                                DataTransferMessage dataTransferMessage = (DataTransferMessage) message;
                                Path path = Paths.get(controller.getROOT() + "\\" + dataTransferMessage.getFileName());
                                try {
                                    if (Files.exists(path)) {
                                        Files.write(path, dataTransferMessage.getData(), StandardOpenOption.TRUNCATE_EXISTING);
                                    } else {
                                        Files.write(path, dataTransferMessage.getData(), StandardOpenOption.CREATE);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                controller.refreshListClient();
                            }
                        }
                    }
                } catch (IOException e) {
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try {
            oeos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void auth(String login, String password) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            AuthMessage authMessage = new AuthMessage(login, new String(digest.digest()));
            this.username = login;
            oeos.writeObject(authMessage);
            oeos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            odis.close();
            oeos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
