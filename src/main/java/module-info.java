module fxapp.runbrowser {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    exports fxapp.runbrowser;
    opens fxapp.runbrowser to javafx.fxml;
}