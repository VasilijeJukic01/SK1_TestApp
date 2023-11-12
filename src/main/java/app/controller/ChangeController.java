package app.controller;

import app.core.Core;
import app.util.Utils;
import app.view.MainView;
import com.raf.sk.specification.model.Appointment;
import com.raf.sk.specification.model.Day;
import com.raf.sk.specification.model.ScheduleRoom;
import com.raf.sk.specification.model.time.ReservedTime;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ChangeController implements EventHandler<ActionEvent> {

    private final ComboBox<String> cbRoom;
    private final ComboBox<String> cbDay;
    private final TextField tfTime;
    private final Map<String, TextField> textFields;

    public ChangeController(ComboBox<String> cbRoom, ComboBox<String> cbDay, TextField tfTime, Map<String, TextField> textFields) {
        this.cbRoom = cbRoom;
        this.cbDay = cbDay;
        this.tfTime = tfTime;
        this.textFields = textFields;
    }

    @Override
    public void handle(ActionEvent event) {
        Appointment oldAppointment = MainView.getInstance().getTvAppointments().getSelectionModel().getSelectedItem();
        if (cbRoom.getSelectionModel().getSelectedItem() == null || cbDay.getSelectionModel().getSelectedItem() == null || tfTime.getText().isEmpty()) return;
        if (oldAppointment == null) return;

        ScheduleRoom room = Core.getInstance().getSchedule().getRoomByName(cbRoom.getSelectionModel().getSelectedItem());
        String day = cbDay.getSelectionModel().getSelectedItem();
        String[] time = tfTime.getText().split("-");
        Map<String, Object> data = new HashMap<>();

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        for (String s : textFields.keySet()) {
            if (s.equalsIgnoreCase("START_DATE")) {
                startDate = LocalDate.parse(textFields.get(s).getText());
                continue;
            }
            if (s.equalsIgnoreCase("END_DATE")) {
                endDate = LocalDate.parse(textFields.get(s).getText());
                continue;
            }
            data.put(s, textFields.get(s).getText());
        }

        Appointment a = new Appointment(new ReservedTime(Day.valueOf(day), time[0], time[1], startDate, endDate), room, data);
        Core.getInstance().getSchedule().changeAppointment(oldAppointment, a);

        Utils.getInstance().forceViewRefresh();
    }

}
