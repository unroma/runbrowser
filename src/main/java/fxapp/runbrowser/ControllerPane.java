package fxapp.runbrowser;

import fxapp.runbrowser.enums.SavedDefaults;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public ChoiceBox<String> savedDefault;
    @FXML
    public TextField relativeUrl;


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
