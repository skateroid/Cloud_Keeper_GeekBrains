package client;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class Controller {

    private client.ClientConnection clientConnection;

    @FXML private Pane authPanel;
    @FXML private TextField loginField;
    @FXML private PasswordField passField;
    @FXML private Pane workArea;

    @FXML private void initialize() {
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

    public void showMessage(String message) {
        // метод будет заменен при переработке функционала с чата на облачное хранилище
    }

    public void showUsersList(String[] users) {
        // метод будет заменен при переработке функционала с чата на облачное хранилище
    }
}
