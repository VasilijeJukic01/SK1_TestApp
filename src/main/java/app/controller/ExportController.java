package app.controller;

import app.core.Core;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;

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
        if (rbCSV.isSelected()) {
            try {
                Core.getInstance().getSchedule().saveScheduleToFile("src/main/resources/output.csv", "CSV");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (rbJSON.isSelected()) {
            try {
                Core.getInstance().getSchedule().saveScheduleToFile("src/main/resources/output.json", "JSON");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

