package MyClient;

import com.geekcloud.common.messaging.CommandMessage;
import com.geekcloud.common.messaging.DataTransferMessage;
import com.geekcloud.common.messaging.FileInfo;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {


    private final static String LOCAL_ROOT2 = "client\\local_storage_for_all_users";
    private final static String LOCAL_ROOT = "C:\\GitHub\\geek-cloud\\Cloud_Keeper_GeekBrains\\client\\local_storage_for_all_users";

    @FXML
    private TableView<File> localList;
    @FXML
    private ListView<File> cloudList;
    /*@FXML
    private TableView<FileInfo> cloudFilesTable;
    @FXML
    private TableColumn<FileInfo, String> colType;
    @FXML
    private TableColumn<FileInfo, String> colName;
    @FXML
    private TableColumn<FileInfo, String> colSize;*/

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

    private ObservableList<File> cloudListItems;
    private ObservableList<File> localListItems;

    //private FileListMessage fileListMessage;

    /*@FXML
    private void initialize() {
        this.clientConnection = new ClientConnection();
        // this.fileListMessage = new FileListMessage();
        cloudListItems = FXCollections.observableArrayList();
        localListItems = FXCollections.observableArrayList();
       cloudList.setItems(cloudListItems);

        clientConnection.init(this);

        *//*colType.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("fileType"));
        colName.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("name"));
        colSize.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("sizeInKb"));*//*
    }*/

    /*public TableView<FileInfo> getCloudFilesTable() {
        return cloudFilesTable;
    }*/

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
        File file = cloudList.getSelectionModel().getSelectedItem();
        clientConnection.sendMessage(new CommandMessage(CommandMessage.Command.DOWNLOAD, file));

    }

    public void uploadToServer() {
        File file = localList.getSelectionModel().getSelectedItem();
        clientConnection.sendMessage(new DataTransferMessage(Paths.get(file.getAbsolutePath())));
        System.out.println(Paths.get(file.getAbsolutePath()));
    }

    public void deleteFromLocalDisk() {
        try {
            Files.delete(Paths.get(localList.getSelectionModel().getSelectedItem().getAbsolutePath()));
            refreshListClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromServer() {
        clientConnection.sendMessage(new CommandMessage(CommandMessage.Command.DELETE, cloudList.getSelectionModel().getSelectedItem().getAbsolutePath()));
    }

    public void refreshListClient() {
        try {
            localListItems.clear();
            localListItems.addAll(Files.list(Paths.get(LOCAL_ROOT)).map(Path::toFile).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshListServer() {
        clientConnection.sendMessage(new CommandMessage(CommandMessage.Command.LIST_FILES));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //this.clientConnection = new ClientConnection();
        // this.fileListMessage = new FileListMessage();
        this.clientConnection = new ClientConnection();
        clientConnection.init(this);
        cloudListItems = FXCollections.observableArrayList();
        localListItems = FXCollections.observableArrayList();

        TableColumn<File, String> tcName = new TableColumn<>("Имя Файла");
        tcName.setCellValueFactory(new PropertyValueFactory<File,String>("name"));
        tcName.setPrefWidth(200);
        TableColumn<File,String> tcSize = new TableColumn<>("Размер");
        tcSize.setCellValueFactory(param -> {
            long size = param.getValue().length();
            return new ReadOnlyObjectWrapper<>(String.valueOf(size) + " bytes");
        });
        tcSize.setPrefWidth(200);
        localList.getColumns().addAll(tcName, tcSize);

        localList.setItems(localListItems);
        cloudList.setItems(cloudListItems);

        refreshListClient();
    }

    /*@FXML
    private void clickOnFile(MouseEvent mouseEvent) {

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
    }*/

    public ObservableList<File> getCloudListItems() {
        return cloudListItems;
    }

    public static String getLocalRoot() {
        return LOCAL_ROOT;
    }
}
