package fxapp.runbrowser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
public class ControllerPanes {

    private static final String PANE_VIEW_RESOURCE = "values-view.fxml";
    private static final String REMOVE_ID = "pane_to_remove";
    @FXML
    private VBox panes;
    @FXML
    private Button addButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    public Button startButton;

    public void addPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PANE_VIEW_RESOURCE));
        Pane pane = loader.load();
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
}
