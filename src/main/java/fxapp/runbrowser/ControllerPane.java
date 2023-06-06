package fxapp.runbrowser;

import fxapp.runbrowser.enums.SavedDefaults;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerPane implements Initializable {

    private static final String REMOVE_ID = "pane_to_remove";
    @FXML
    private Pane pane;
    @Getter
    @FXML
    private TextField url;
    @Getter
    @FXML
    private TextField username;
    @Getter
    @FXML
    private PasswordField password;
    @Getter
    @FXML
    private ChoiceBox<String> savedDefault;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> options = new ArrayList<>(Arrays.stream(SavedDefaults.values()).map(SavedDefaults::name).toList());
        options.replaceAll(name -> name.equals(SavedDefaults.EMPTY.name()) ? "": name);
        savedDefault.getItems().addAll(options);
        savedDefault.setOnAction(this::initSavedDefaultsIntoFields);
    }

    public void initSavedDefaultsIntoFields(ActionEvent event) {
        String savedDefaultValue = savedDefault.getValue();
        SavedDefaults savedDefault = SavedDefaults.getDefaultByName(savedDefaultValue);
        url.setText(savedDefault.getUrl());
        if (savedDefault.equals(SavedDefaults.EMPTY)) {
            disableCredentials();
        } else  {
            enableCredentials();
        }
    }

    public void closePane(ActionEvent event) {
        Node currentButton = (Node) event.getSource();
        Pane currentPane = (Pane) currentButton.getParent();
        Storage.getCreatedPanes().remove(currentPane.getId());
        currentPane.setId(REMOVE_ID);
        ControllerPanes panes = Storage.getMain().stream().findFirst().orElseThrow();
        VBox vBox = (VBox) panes.getScrollPane().getContent();
        vBox.getChildren().stream()
                .filter(pane -> pane.getId().equals(REMOVE_ID))
                .findFirst().ifPresent(vBox.getChildren()::remove);
        if (panes.getAddButton().isDisable() && vBox.getChildren().size() < 10) {
            panes.getAddButton().setDisable(false);
        }
        if (vBox.getChildren().isEmpty()) {
            panes.getStartButton().setDisable(true);
        }
    }

    private void disableCredentials() {
        url.setDisable(false);
        username.setDisable(true);
        password.setDisable(true);
        username.setText(null);
        password.setText(null);
    }

    private void enableCredentials() {
        url.setDisable(true);
        username.setDisable(false);
        password.setDisable(false);
    }
}
