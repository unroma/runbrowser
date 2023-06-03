package fxapp.runbrowser;

import fxapp.runbrowser.enums.SavedDefaults;
import fxapp.runbrowser.model.TabValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ControllerPanes {

    private static final Map<String, ControllerPane> createdPanes = new HashMap<>();
    private static final String PANE_VIEW_RESOURCE = "values-view.fxml";
    private static final String REMOVE_ID = "pane_to_remove";
    private static final String PATH_TO_PROFILE = "\\AppData\\Local\\Google\\Chrome\\User Data";
    private String pathToProfile;
    @FXML
    private VBox panes;
    @FXML
    private Button addButton;
    @FXML
    public TextField path;
    @FXML
    public CheckBox defaultPath;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    public Button startButton;

    public void addPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PANE_VIEW_RESOURCE));
        Pane pane = loader.load();
        ControllerPane controllerPane = loader.getController();
        pane.setId(UUID.randomUUID().toString());
        createdPanes.put(pane.getId(), controllerPane);
        pane.getChildren().add(initCloseButton());
        VBox vBox = (VBox) scrollPane.getContent();
        if (vBox == null) {
            vBox = new VBox();
            scrollPane.setContent(vBox);
            vBox.getChildren().add(pane);
        } else {
            if (vBox.getChildren().size() == 9) {
                addButton.setDisable(true);
            }
            vBox.getChildren().add(pane);
        }
        startButton.setDisable(false);
    }

    private Button initCloseButton() {
        Button closeButton = new Button("X");
        closeButton.setLayoutY(4.0);
        closeButton.setLayoutX(660.0);
        closeButton.setPrefHeight(16.0);
        closeButton.setPrefWidth(16.0);
        closeButton.setFont(Font.font("System Bold", 8.0));
        closeButton.setOnAction(event -> {
            Node currentButton = (Node) event.getSource();
            Pane currentPane = (Pane) currentButton.getParent();
            createdPanes.remove(currentPane.getId());
            currentPane.setId(REMOVE_ID);
            VBox vBox = (VBox) scrollPane.getContent();
            vBox.getChildren().stream()
                    .filter(pane -> pane.getId().equals(REMOVE_ID))
                    .findFirst().ifPresent(vBox.getChildren()::remove);
            if (addButton.isDisable() && vBox.getChildren().size() < 10) {
                addButton.setDisable(false);
            }
            if (vBox.getChildren().isEmpty()) {
                startButton.setDisable(true);
            }
        });
        return closeButton;
    }

    public void start() {
        panes.setDisable(true);
        setToStorage();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                ChromeDriverManager.initDriver(path.getText());
                ChromeDriverManager.closeDriver();
                panes.setDisable(false);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void setDefaultProfilePath() {
        if (defaultPath.isSelected()) {
            if (pathToProfile == null) {
                pathToProfile = System.getProperty("user.home").concat(PATH_TO_PROFILE);
            }
            path.setText(pathToProfile);
            path.setDisable(true);
        } else {
            path.setDisable(false);
        }
    }

    private void setToStorage() {
        createdPanes.values().forEach(controllerPane -> {
            String urlValue = controllerPane.url.getText();
            if (controllerPane.savedDefault.getValue() != null) {
                urlValue = SavedDefaults.getDefaultByName(controllerPane.savedDefault.getValue()).getUrl();
            }
            TabValue tab = TabValue.builder()
                    .url(urlValue)
                    .user(controllerPane.username.getText())
                    .password(controllerPane.password.getText())
                    .relative(controllerPane.relativeUrl.getText())
                    .savedDefault(controllerPane.savedDefault.getValue())
                    .build();
            ChromeDriverManager.getTabs().add(tab);
        });
    }
}
