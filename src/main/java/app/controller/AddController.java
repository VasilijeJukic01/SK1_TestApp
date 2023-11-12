package app.controller;

import app.core.Core;
import app.util.Utils;
import com.raf.sk.specification.model.Appointment;
import com.raf.sk.specification.model.Day;
import com.raf.sk.specification.model.ScheduleRoom;
import com.raf.sk.specification.model.time.ReservedTime;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddController implements EventHandler<ActionEvent> {

    private final ComboBox<String> cbRoom;
    private final ComboBox<String> cbDay;
    private final TextField tfTime;

    private final Map<String, TextField> textFields;

    public AddController(ComboBox<String> cbRoom, ComboBox<String> cbDay, TextField tfTime, Map<String, TextField> textFields) {
        this.cbRoom = cbRoom;
        this.cbDay = cbDay;
        this.tfTime = tfTime;
        this.textFields = new LinkedHashMap<>(textFields);
    }

    @Override
    public void handle(ActionEvent event) {
        String day = cbDay.getValue();

        Map<String, Object> values = new LinkedHashMap<>();
        LocalDate startDate = null, endDate = null;

        for (String key : textFields.keySet()) {
            if (key.equalsIgnoreCase("START_DATE")) {
                startDate = LocalDate.parse(textFields.get("START_DATE").getText());
                continue;
            }
            if (key.equalsIgnoreCase("END_DATE")) {
                endDate = LocalDate.parse(textFields.get("END_DATE").getText());
                continue;
            }
            values.put(key, textFields.get(key).getText());
        }

        ReservedTime reservedTime = createTime(startDate, endDate, day);
        ScheduleRoom scheduleRoom = Core.getInstance().getSchedule().getRoomByName(cbRoom.getValue());
        Appointment a = new Appointment(reservedTime, scheduleRoom, values);
        Core.getInstance().getSchedule().addAppointment(a);

        Utils.getInstance().forceViewRefresh();
    }

    private ReservedTime createTime(LocalDate startDate, LocalDate endDate, String day) {
        ReservedTime reservedTime;
        String[] timeData = tfTime.getText().split("-");

        if (endDate != null && startDate != null) {
            reservedTime = new ReservedTime(Day.valueOf(day), timeData[0], timeData[1], startDate, endDate);
        }
        else if (endDate == null && startDate != null) {
            reservedTime = new ReservedTime(Day.valueOf(day), timeData[0], timeData[1], startDate, startDate);
        }
        else {
            startDate = LocalDate.parse(Core.getInstance().getProperties().getProperty("startDate").replaceAll("\"", ""));
            endDate = LocalDate.parse(Core.getInstance().getProperties().getProperty("endDate").replaceAll("\"", ""));
            reservedTime = new ReservedTime(Day.valueOf(day), timeData[0], timeData[1], startDate, endDate);
        }
        return reservedTime;
    }

}
