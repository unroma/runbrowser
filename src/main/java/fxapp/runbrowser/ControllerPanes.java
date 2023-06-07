package fxapp.runbrowser;

import fxapp.runbrowser.enums.SavedDefaults;
import fxapp.runbrowser.model.OpenTabsState;
import fxapp.runbrowser.model.TabValue;
import fxapp.runbrowser.utils.EncryptUtils;
import fxapp.runbrowser.utils.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class ControllerPanes implements Initializable {

    private static final String PANE_VIEW_RESOURCE = "values-view.fxml";
    private static final String PATH_TO_PROFILE = "\\AppData\\Local\\Google\\Chrome\\User Data";
    @FXML
    private String pathToProfile;
    @FXML
    private VBox panes;
    @Getter
    @FXML
    private Button addButton;
    @FXML
    private TextField path;
    @FXML
    private CheckBox defaultProfile;
    @FXML
    @Getter
    private ScrollPane scrollPane;
    @FXML
    private Button clearButton;
    @FXML
    private TextArea consoleOutput;
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;
    @FXML
    private CheckBox loadWhenStart;
    @Getter
    @FXML
    private Button startButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        redirectConsoleOutput();
        initFromFile();
    }

    public void addPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PANE_VIEW_RESOURCE));
        Pane pane = loader.load();
        ControllerPane controllerPane = loader.getController();
        pane.setId(UUID.randomUUID().toString());
        Storage.getCreatedPanes().put(pane.getId(), controllerPane);
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
        Storage.setToStorage();
        OpenTabsState state = OpenTabsState.builder()
                        .defaultProfile(defaultProfile.isSelected())
                        .loadWhenStart(loadWhenStart.isSelected())
                        .path(path.getText())
                        .tabs(Storage.getTabs()).build();
        JsonParser.writeObjectToJsonFile(state);
    }

    public void loadFromFile() {
        OpenTabsState state = JsonParser.readObjectFromJsonFile();
        if (state != null) {
            Storage.getCreatedPanes().clear();
            clearScrollPane();
            setAppValues(state);
        }
    }

    public void start() {
        panes.setDisable(true);
        Storage.setToStorage();
        runWebDriverTask();
    }

    public void clearConsole() {
        consoleOutput.setText(null);
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
        Storage.getCreatedPanes().put(pane.getId(), controllerPane);

        if (tabValue.getSavedDefault() != SavedDefaults.EMPTY) {
            controllerPane.getUrl().setText(tabValue.getSavedDefault().getUrl());
            controllerPane.getSavedDefault().setValue(tabValue.getSavedDefault().name());
            controllerPane.getUsername().setText(EncryptUtils.decryptText(tabValue.getUsername()));
            controllerPane.getPassword().setText(EncryptUtils.decryptText(tabValue.getPassword()));
        } else {
            controllerPane.getUrl().setText(tabValue.getUrl());
        }
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

    private void initFromFile() {
        OpenTabsState state = JsonParser.readObjectFromJsonFile();
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

    private void redirectConsoleOutput() {
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) {
                Platform.runLater(() -> consoleOutput.appendText(String.valueOf((char) b)));
            }
        };
        PrintStream printStream = new PrintStream(outputStream, true);
        System.setOut(printStream);
    }

    private void runWebDriverTask() {
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            ChromeDriverManager.initDriver(path.getText());
            ChromeDriverManager.closeDriver();
            return null;
        }, executor);
        future.thenAccept(result -> {
            executor.shutdown();
            System.out.println("Done!");
            panes.setDisable(false);
        }).exceptionally(e -> {
            System.out.printf("Exception occurred during webdriver usage: %s", e.getMessage());
            executor.shutdown();
            panes.setDisable(false);
            return null;
        });
    }
}
