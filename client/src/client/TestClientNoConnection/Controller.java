package TestClientNoConnection;

import com.geekcloud.common.messaging.CommandMessage;
import com.geekcloud.common.messaging.FileInfo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Controller {

    @FXML
    private ListView<String> localList;
    @FXML private TableView<FileInfo> cloudFilesTable;
    @FXML private TableColumn<FileInfo, String> colType;
    @FXML private TableColumn<FileInfo, String> colName;
    @FXML private TableColumn<FileInfo, String> colSize;

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

    private ClientConnection clientConnection;

    @FXML
    private void initialize() {
        this.clientConnection = new ClientConnection();
        clientConnection.init(this);

        colType.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("fileType"));
        colName.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("name"));
        colSize.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("sizeInKb"));
    }

    public TableView<FileInfo> getCloudFilesTable() { return cloudFilesTable; }

    @FXML
    public void auth() throws IOException {
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

    public void deleteFromLocalDisk() {

    }

    public void deleteFromServer() {

    }

    public void refreshListClient() {

    }

    public void refreshListServer() throws IOException {
        clientConnection.requestServerListRefresh();
    }

    @FXML private void clickOnFile(MouseEvent mouseEvent) {

    }
}
