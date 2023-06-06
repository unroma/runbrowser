package fxapp.runbrowser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RunBrowserApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunBrowserApplication.class.getResource("panes-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Storage.getMain().add(fxmlLoader.getController());
        stage.setTitle("Run browser app!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> ChromeDriverManager.closeDriver());
    }

    public static void main(String[] args) {
        launch();
    }
}