package MyClient;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialogs;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;

public class Controller {

    private ClientConnection clientConnection;

    @FXML
    private Pane authPanel;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passField;
    @FXML
    private Pane workArea;
    /*@FXML
    private Panel actionPanel1;*/


    @FXML
    private void initialize() {
        this.clientConnection = new ClientConnection();
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
    }

    public void showMessage(Component parent, String message) {
        // метод будет заменен при переработке функционала с чата на облачное хранилище
        JOptionPane jOptionPane = new JOptionPane();
        jOptionPane.showMessageDialog(parent, message);
    }

    public void showUsersList(String[] users) {
        // метод будет заменен при переработке функционала с чата на облачное хранилище
    }
}
