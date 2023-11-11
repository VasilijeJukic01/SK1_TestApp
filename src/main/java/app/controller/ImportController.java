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
import java.util.List;
import java.util.Properties;

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

        MainView.getInstance().getLbTotalAppointmentsValue().setText(Utils.getInstance().calculateAppointments());
        MainView.getInstance().getLbTotalFreeAppointmentsValue().setText(Utils.getInstance().calculateFreeAppointments());
    }

    private void loadFile(File selectedFile) {
        try {
            Properties p = Utils.getInstance().loadProperties("src/main/resources/import.config");
            Core.getInstance().newSchedule(p);
            Core.getInstance().getSchedule().loadScheduleFromFile(selectedFile.getAbsolutePath(), p);
            String[] col = p.getProperty("columns").replaceAll("\"", "").split(",");
            Core.getInstance().getColumns().clear();
            Core.getInstance().getColumns().addAll(List.of(col));

            tvAppointments.getItems().clear();

            TableColumn<Appointment, String> tcDay = MainView.getInstance().getTcDay();
            TableColumn<Appointment, String> tcTime = MainView.getInstance().getTcTime();
            TableColumn<Appointment, String> tcRoom = MainView.getInstance().getTcRoom();

            tvAppointments.getColumns().removeIf(column -> !column.equals(tcDay) && !column.equals(tcTime) && !column.equals(tcRoom));

            Utils.getInstance().forceRefresh(tvAppointments, false);
            Utils.getInstance().forceRefresh(FreeView.getInstance().getTvAppointments(), true);

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
