package app.controller;

import app.core.Core;
import app.util.Utils;
import app.view.FreeView;
import app.view.MainView;
import com.raf.sk.specification.model.Appointment;
import com.raf.sk.specification.model.Day;
import com.raf.sk.specification.model.ScheduleRoom;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.List;

public class FilterController implements EventHandler<ActionEvent> {

    private final ComboBox<String> cbRoom;
    private final ComboBox<String> cbDay;
    private final TextField tfTime;
    private final TextField tfStartDate;
    private final TextField tfEndDate;
    private final ComboBox<String> cbCriteria;

    private final RadioButton rbFree;

    public FilterController(ComboBox<String> cbRoom, ComboBox<String> cbDay, TextField tfTime, TextField tfStartDate, TextField tfEndDate, ComboBox<String> cbCriteria, RadioButton rbFree) {
        this.cbRoom = cbRoom;
        this.cbDay = cbDay;
        this.tfTime = tfTime;
        this.tfStartDate = tfStartDate;
        this.tfEndDate = tfEndDate;
        this.cbCriteria = cbCriteria;
        this.rbFree = rbFree;
    }

    @Override
    public void handle(ActionEvent event) {
        String day = cbDay.getValue();
        String room = cbRoom.getValue();
        String time = tfTime.getText();
        String startDate = tfStartDate.getText();
        String endDate = tfEndDate.getText();
        Day d = day != null ? Day.valueOf(day) : null;
        ScheduleRoom r = room != null ? Core.getInstance().getSchedule().getRoomByName(room) : null;
        List<Appointment> filtered = (rbFree.isSelected()) ? getFreeAppointments(d, r, time, LocalDate.parse(startDate), LocalDate.parse(endDate)) :
                getReservedAppointments(d, r, time, LocalDate.parse(startDate), LocalDate.parse(endDate));

        if (filtered == null) return;

        if (rbFree.isSelected()) {
            FreeView.getInstance().getTvAppointments().getItems().clear();
            FreeView.getInstance().getTvAppointments().getItems().addAll(filtered);
            FreeView.getInstance().getTvAppointments().refresh();
        }
        else {
            MainView.getInstance().getTvAppointments().getItems().clear();
            MainView.getInstance().getTvAppointments().getItems().addAll(filtered);
            MainView.getInstance().getTvAppointments().refresh();
        }

        MainView.getInstance().getLbTotalAppointmentsValue().setText(Utils.getInstance().calculateAppointments());
        MainView.getInstance().getLbTotalFreeAppointmentsValue().setText(Utils.getInstance().calculateFreeAppointments());
    }

    private List<Appointment> getFreeAppointments(Day day, ScheduleRoom room, String time, LocalDate startDate, LocalDate endDate) {
        String[] timeSplit = time.split("-");
        if (cbCriteria.getValue().equals("Room")) {
            return Core.getInstance().getSchedule().findFreeAppointmentsByRoom(room);
        }
        else if (cbCriteria.getValue().equals("Day, Time and Date")) {
            return Core.getInstance().getSchedule().findFreeAppointmentsByDayAndPeriod(day, startDate, endDate, timeSplit[0], timeSplit[1]);
        }
        else if (cbCriteria.getValue().equals("Date")) {
            return Core.getInstance().getSchedule().findFreeAppointmentsByDate(startDate);
        }
        return null;
    }

    private List<Appointment> getReservedAppointments(Day day, ScheduleRoom room, String time, LocalDate startDate, LocalDate endDate) {
        String[] timeSplit = time.split("-");
        if (cbCriteria.getValue().equals("Room")) {
            return Core.getInstance().getSchedule().findReservedAppointmentsByRoom(room);
        }
        else if (cbCriteria.getValue().equals("Day, Time and Date")) {
            return Core.getInstance().getSchedule().findReservedAppointmentsByDayAndPeriod(day, startDate, endDate, timeSplit[0], timeSplit[1]);
        }
        else if (cbCriteria.getValue().equals("Date")) {
            return Core.getInstance().getSchedule().findReservedAppointmentsByDate(startDate);
        }
        return null;
    }

}
