package MyClient;

import com.geekcloud.common.fileprocessing.FileHelper;
import com.geekcloud.common.messaging.FileInfo;
import com.geekcloud.common.messaging.FileListMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Controller {

    @FXML
    private ListView<String> localList;
    @FXML
    private ListView<String> cloudList;

    @FXML
    private Pane authPanel;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passField;
    @FXML
    private Pane workArea;
    @FXML
    private HBox actionPanel1;
    @FXML
    private HBox actionPanel2;
    @FXML
    private ClientConnection clientConnection;
    private ObservableList<FileInfo> cloudListItems;
    private FileListMessage fileListMessage;

    @FXML
    private void initialize() {
        this.clientConnection = new ClientConnection();
       // this.fileListMessage = new FileListMessage();
        clientConnection.init(this);
    }

    @FXML
    public void auth() {
        clientConnection.auth(loginField.getText(), passField.getText());
        loginField.clear();
        passField.clear();
    }

    public void switchWindows() {
        authPanel.setVisible(!clientConnection.isAuthrozied());
        workArea.setVisible(clientConnection.isAuthrozied());

        actionPanel1.setVisible(clientConnection.isAuthrozied());
        actionPanel1.setManaged(clientConnection.isAuthrozied());

        actionPanel2.setVisible(clientConnection.isAuthrozied());
        actionPanel2.setManaged(clientConnection.isAuthrozied());
    }

    public void showMessage(Component parent, String message) {
        JOptionPane jOptionPane = new JOptionPane();
        jOptionPane.showMessageDialog(parent, message);
    }

    public void downloadFileToDisk() {

    }

    public void uploadToServer() {

    }

    public void deleateFromLocalDisk() {

    }

    public void deleateFromServer() {

    }

    public void refreshListClient() {

    }

    public void refreshListServer() {
        cloudListItems = FXCollections.observableArrayList(fileListMessage.getFileList());
        //cloudList.setItems(cloudListItems);
    }
}
