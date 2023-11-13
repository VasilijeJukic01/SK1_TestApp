package app.controller;

import app.core.Core;
import app.util.Utils;
import app.view.FreeView;
import app.view.MainView;
import com.raf.sk.specification.model.Appointment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ConfigurationController  implements EventHandler<ActionEvent> {

    private final TableView<Appointment> tvAppointments;

    public ConfigurationController(TableView<Appointment> tvAppointments) {
        this.tvAppointments = tvAppointments;
    }

    @Override
    public void handle(ActionEvent event) {
        Core.getInstance().setConfiguration();
        Utils.getInstance().forceTableRefresh(MainView.getInstance().getTvAppointments(), false);

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

        Utils.getInstance().forceViewRefresh();
    }

}
