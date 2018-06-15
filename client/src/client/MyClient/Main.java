package MyClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml")); //вот тут пришлось добавить getClassLoader(), иначе не получилось грузить fxml
        primaryStage.setTitle("GeekCloud Client");
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        }); //скорее всего так не совсем правильно, не догадался как реализовать правильно
    }

    public static void main(String[] args) {
        launch(args);

    }
}
