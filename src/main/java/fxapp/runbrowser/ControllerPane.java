package fxapp.runbrowser;

import fxapp.runbrowser.enums.SavedDefaults;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerPane implements Initializable {

    @FXML
    public Pane pane;
    @FXML
    public TextField url;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public CheckBox encryptSave;
    @FXML
    public ChoiceBox<String> savedDefault;
    @FXML
    public TextField relativeUrl;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        savedDefault.getItems().addAll(Arrays.stream(SavedDefaults.values()).map(SavedDefaults::getName).toList());
        savedDefault.setOnAction(this::initSavedDefaultsIntoFields);
    }

    public void initSavedDefaultsIntoFields(ActionEvent event) {
        String savedDefaultValue = savedDefault.getValue();
        SavedDefaults savedDefault = SavedDefaults.getDefaultByName(savedDefaultValue);
        url.setText(savedDefault.getUrl());
        if (url.isDisable() && savedDefault.equals(SavedDefaults.EMPTY)) {
            disableCredentials();
        } else  {
            enableCredentials();
        }
    }

    private void disableCredentials() {
        url.setDisable(false);
        username.setDisable(true);
        password.setDisable(true);
        encryptSave.setDisable(true);
        username.setText(null);
        password.setText(null);
        encryptSave.setSelected(false);
    }

    private void enableCredentials() {
        url.setDisable(true);
        username.setDisable(false);
        password.setDisable(false);
        encryptSave.setDisable(false);
    }
}
