package app.controller;

import app.core.Core;
import app.util.Utils;
import app.view.FreeView;
import app.view.MainView;
import com.raf.sk.specification.model.Appointment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ImportController implements EventHandler<ActionEvent> {

    private final TableView<Appointment> tvAppointments;

    public ImportController(TableView<Appointment> tvAppointments) {
        this.tvAppointments = tvAppointments;
    }

    @Override
    public void handle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) loadFile(selectedFile);

        Utils.getInstance().forceViewRefresh();
    }

    private void loadFile(File selectedFile) {
        try {
            Core.getInstance().newSchedule();
            Core.getInstance().getSchedule().loadScheduleFromFile(selectedFile.getAbsolutePath());

            tvAppointments.getItems().clear();

            TableColumn<Appointment, String> tcDayMain = MainView.getInstance().getTcDay();
            TableColumn<Appointment, String> tcTimeMain = MainView.getInstance().getTcTime();
            TableColumn<Appointment, String> tcRoomMain = MainView.getInstance().getTcRoom();

            TableColumn<Appointment, String> tcDay = FreeView.getInstance().getTcDay();
            TableColumn<Appointment, String> tcTime = FreeView.getInstance().getTcTime();
            TableColumn<Appointment, String> tcRoom = FreeView.getInstance().getTcRoom();
            TableColumn<Appointment, String> tcDate = FreeView.getInstance().getTcDate();

            tvAppointments.getColumns().removeIf(column -> !column.equals(tcDayMain)
                    && !column.equals(tcTimeMain)
                    && !column.equals(tcRoomMain)
            );
            FreeView.getInstance().getTvAppointments().getColumns().removeIf(column -> !column.equals(tcDay)
                    && !column.equals(tcTime)
                    && !column.equals(tcRoom)
                    && !column.equals(tcDate)
            );

            Utils.getInstance().forceTableRefresh(tvAppointments, false);
            Utils.getInstance().forceTableRefresh(FreeView.getInstance().getTvAppointments(), true);
        }
        catch (IOException e) {
            showAlert();
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Error reading the file.");
        alert.showAndWait();
    }

}
