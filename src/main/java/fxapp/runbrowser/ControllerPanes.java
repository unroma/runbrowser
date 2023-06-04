package fxapp.runbrowser;

import fxapp.runbrowser.enums.SavedDefaults;
import fxapp.runbrowser.model.OpenTabsState;
import fxapp.runbrowser.model.TabValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

public class ControllerPanes implements Initializable {

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
    public CheckBox defaultProfile;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;
    @FXML
    private CheckBox loadWhenStart;
    @FXML
    public Button startButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFromFile();
    }

    public void addPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PANE_VIEW_RESOURCE));
        Pane pane = loader.load();
        ControllerPane controllerPane = loader.getController();
        pane.setId(UUID.randomUUID().toString());
        createdPanes.put(pane.getId(), controllerPane);
        pane.getChildren().add(initCloseButton());
        addPaneNode(pane);
    }

    public void setDefaultProfilePath() {
        if (defaultProfile.isSelected()) {
            if (pathToProfile == null) {
                pathToProfile = System.getProperty("user.home").concat(PATH_TO_PROFILE);
            }
            path.setText(pathToProfile);
            path.setDisable(true);
        } else {
            path.setDisable(false);
        }
    }

    public void saveToFile() {
        setToStorage();
        OpenTabsState state = OpenTabsState.builder()
                        .defaultProfile(defaultProfile.isSelected())
                        .loadWhenStart(loadWhenStart.isSelected())
                        .path(path.getText())
                        .tabs(ChromeDriverManager.getTabs()).build();
        Parser.writeObjectToJsonFile(state);
    }

    public void loadFromFile() {
        OpenTabsState state = Parser.readObjectFromJsonFile();
        if (state != null) {
            createdPanes.clear();
            clearScrollPane();
            setAppValues(state);
        }
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

    private void addPane(TabValue tabValue) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PANE_VIEW_RESOURCE));
        Pane pane;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ControllerPane controllerPane = loader.getController();
        pane.setId(UUID.randomUUID().toString());
        createdPanes.put(pane.getId(), controllerPane);
        pane.getChildren().add(initCloseButton());

        if (tabValue.getSavedDefault() != SavedDefaults.EMPTY) {
            controllerPane.url.setText(tabValue.getSavedDefault().getUrl());
            controllerPane.savedDefault.setValue(tabValue.getSavedDefault().name());
            controllerPane.username.setText(tabValue.getUsername());
            controllerPane.password.setText(tabValue.getPassword());
            controllerPane.encryptSave.setSelected(tabValue.getEncryptAndSave());
        } else {
            controllerPane.url.setText(tabValue.getUrl());
        }
        controllerPane.relativeUrl.setText(tabValue.getRelative());
        addPaneNode(pane);
    }

    private void addPaneNode(Pane pane) {
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

    private void clearScrollPane() {
        VBox vBox = (VBox) scrollPane.getContent();
        if (vBox != null) {
            vBox.getChildren().clear();
        }
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

    private void setToStorage() {
        ChromeDriverManager.getTabs().clear();
        createdPanes.values().forEach(controllerPane -> {
            SavedDefaults savedDefault = SavedDefaults.getDefaultByName(controllerPane.savedDefault.getValue());
            String urlValue = controllerPane.url.getText();
            if (savedDefault != null && savedDefault != SavedDefaults.EMPTY) {
                urlValue = savedDefault.getUrl();
            }
            TabValue tab = TabValue.builder()
                    .url(urlValue)
                    .savedDefault(savedDefault)
                    .username(controllerPane.username.getText())
                    .password(controllerPane.password.getText())
                    .relative(controllerPane.relativeUrl.getText())
                    .encryptAndSave(controllerPane.encryptSave.isSelected())
                    .build();
            ChromeDriverManager.getTabs().add(tab);
        });
    }

    private void initFromFile() {
        OpenTabsState state = Parser.readObjectFromJsonFile();
        if (state != null) {
            if (state.getLoadWhenStart()) {
                setAppValues(state);
            }
        }
    }

    private void setAppValues(OpenTabsState state) {
        loadWhenStart.setSelected(state.getLoadWhenStart());
        path.setText(state.getPath());
        defaultProfile.setSelected(state.getDefaultProfile());
        setDefaultProfilePath();
        state.getTabs().forEach(this::addPane);
        if (state.getTabs().isEmpty()) {
            startButton.setDisable(true);
        }
    }
}
