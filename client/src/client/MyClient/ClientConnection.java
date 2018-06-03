package MyClient;


import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import com.geekcloud.common.settings.ServerConst;
import com.geekcloud.common.messaging.*;

public class ClientConnection implements ServerConst, Server_API {
    private Socket socket;
    private ObjectEncoderOutputStream oeos;
    private ObjectDecoderInputStream odis;
    private boolean isAuthrozied;
    private List<String> filesList;

    public boolean isAuthrozied() {
        return isAuthrozied;
    }

    public void setAuthrozied(boolean authrozied) {
        isAuthrozied = authrozied;
    }

    public ClientConnection() {
    }

    public void init(Controller controller) { //lazy init
        try {
            this.socket = new Socket(SERVER_URL, PORT);
            this.oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            this.odis = new ObjectDecoderInputStream(socket.getInputStream());
            this.isAuthrozied = false;

            new Thread(() -> {
                try {
                    while (true) {
                        Object message = odis.readObject();
                        if (message != null) {
                            System.out.println(message.toString());
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
                    while (true) {
                        Object message = odis.readObject();
                        if (message != null) {
                            if (message instanceof CommandMessage) {
                                CommandMessage commandMessage = (CommandMessage) message;
                               /* if (((CommandMessage) message).getCommand() == CommandMessage.Command.LIST_FILES) {
                                    //нужно как-то вернуть лист файлов текущей папки на сервере
                                } else if (((CommandMessage) message).getCommand() != CommandMessage.Command.LIST_FILES) {
                                    //здесь нужно вызвать метод, который будет обмениваться файлами с серваком
                                }*/
                            }
                            if (message instanceof FileListMessage) {
                                FileListMessage fileListMessage = (FileListMessage) message;

                            }
                            if (message instanceof DataTransferMessage) {
                                DataTransferMessage dataTransferMessage = (DataTransferMessage) message;
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

    public void saveFileToLocalDisk(DataTransferMessage dataMessage) {
        Path path = Paths.get("C:\\TestCloudKeeper" + "\\" + dataMessage.getFileName());
        try {
            if (Files.exists(path)) {
                Files.write(path, dataMessage.getData(), StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                Files.write(path, dataMessage.getData(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendFileToServer() {
        Path path = Paths.get(""); //нужно получить путь выбранного файла
        DataTransferMessage dataTransferMessage = new DataTransferMessage(path);
        try {
            oeos.writeObject(dataTransferMessage);
            oeos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
