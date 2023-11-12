package app.controller;

import app.core.Core;
import app.util.Utils;
import com.raf.sk.specification.model.Appointment;
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
        Utils.getInstance().forceViewRefresh();
    }
}
