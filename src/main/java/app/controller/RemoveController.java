package app.controller;

import app.core.Core;
import app.util.Utils;
import app.view.MainView;
import com.raf.sk.specification.model.Appointment;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;

public class RemoveController implements EventHandler<ActionEvent> {

    private final TableView<Appointment> tvAppointments;

    public RemoveController(TableView<Appointment> tvAppointments) {
        this.tvAppointments = tvAppointments;
    }

    @Override
    public void handle(ActionEvent event) {
        if (tvAppointments.getSelectionModel().getSelectedItem() == null) return;
        Appointment appointment = tvAppointments.getSelectionModel().getSelectedItem();
        Core.getInstance().getSchedule().deleteAppointment(appointment);

        MainView.getInstance().getLbTotalAppointmentsValue().setText(Utils.getInstance().calculateAppointments());
        MainView.getInstance().getLbTotalFreeAppointmentsValue().setText(Utils.getInstance().calculateFreeAppointments());
        MainView.getInstance().getTvAppointments().setItems(FXCollections.observableArrayList(Core.getInstance().getAppointments()));
        MainView.getInstance().getTvAppointments().refresh();
    }
}
