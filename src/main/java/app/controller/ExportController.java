package app.controller;

import app.core.Core;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ExportController implements EventHandler<ActionEvent> {

    private final RadioButton rbCSV;
    private final RadioButton rbJSON;

    public ExportController(RadioButton rbCSV, RadioButton rbJSON) {
        this.rbCSV = rbCSV;
        this.rbJSON = rbJSON;
    }

    @Override
    public void handle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Schedule");

        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json");
        fileChooser.getExtensionFilters().addAll(csvFilter, jsonFilter);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try {
                if (rbCSV.isSelected()) {
                    Core.getInstance().getSchedule().saveScheduleToFile(selectedFile.getAbsolutePath(), "CSV");
                } else if (rbJSON.isSelected()) {
                    Core.getInstance().getSchedule().saveScheduleToFile(selectedFile.getAbsolutePath(), "JSON");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

