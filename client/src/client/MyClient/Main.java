package MyClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml")); //вот тут пришлось добавить getClassLoader(), иначе не получилось грузить fxml
        primaryStage.setTitle("GeekCloud Client");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
        stage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);

    }

    public Stage getStage() {
        return stage;
    }
}
