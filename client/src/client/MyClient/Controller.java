package MyClient;

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

public class Controller {

    @FXML
    private ListView<String> localList;
//    @FXML
//    private ListView<String> cloudList;
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
    @FXML
    private ClientConnection clientConnection;
//    private ObservableList<FileInfo> cloudListItems;
//    private FileListMessage fileListMessage;

    @FXML
    private void initialize() {
        this.clientConnection = new ClientConnection();
       // this.fileListMessage = new FileListMessage();
        clientConnection.init(this);

        colType.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("fileType"));
        colName.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("name"));
        colSize.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("sizeInKb"));
    }

    public TableView<FileInfo> getCloudFilesTable() { return cloudFilesTable; }

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

    public void deleteFromLocalDisk() {

    }

    public void deleteFromServer() {

    }

    public void refreshListClient() {

    }

    public void refreshListServer() {
        // думаю, здесь по нажатию кнопки - скорее запрос на сервер
//        cloudListItems = FXCollections.observableArrayList(fileListMessage.getFileList());
        //cloudList.setItems(cloudListItems);
        clientConnection.sendMessage(new CommandMessage().setCommand(CommandMessage.Command.LIST_FILES));
    }

    @FXML private void clickOnFile(MouseEvent mouseEvent) {

        // двойной клик - для папок: переход по каталогу, для файлов: загрузка после подтверждения
        if (mouseEvent.getClickCount() == 2) {
            FileInfo selectedFile = cloudFilesTable.getSelectionModel().getSelectedItem();

            if (selectedFile == null) return;

            if (selectedFile.getFileType() == FileInfo.FileType.DIR) {
                //TODO прописать команду на сервер о переходе в выбранную папку
            } else {
                //TODO прописать диалог о подтверждении намерения пользователя загрузить файл
                //TODO и, в случае подтверждения, команду на сервер о загрузке файла
            }
        }
    }
}
