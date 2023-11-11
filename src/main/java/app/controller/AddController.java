package app.controller;

import app.core.Core;
import app.util.Utils;
import app.view.MainView;
import com.raf.sk.specification.model.Appointment;
import com.raf.sk.specification.model.Day;
import com.raf.sk.specification.model.ScheduleRoom;
import com.raf.sk.specification.model.time.ReservedTime;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

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
        Properties properties = Utils.getInstance().loadProperties("src/main/resources/configuration.config");
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

        ReservedTime reservedTime = createTime(startDate, endDate, day, properties);

        ScheduleRoom scheduleRoom = Core.getInstance().getSchedule().getRoomByName(cbRoom.getValue());
        Appointment a = new Appointment(reservedTime, scheduleRoom, values);
        Core.getInstance().getSchedule().addAppointment(a);

        MainView.getInstance().getLbTotalAppointmentsValue().setText(Utils.getInstance().calculateAppointments());
        MainView.getInstance().getLbTotalFreeAppointmentsValue().setText(Utils.getInstance().calculateFreeAppointments());
        MainView.getInstance().getTvAppointments().setItems(FXCollections.observableArrayList(Core.getInstance().getAppointments()));
        MainView.getInstance().getTvAppointments().refresh();
    }

    private ReservedTime createTime(LocalDate startDate, LocalDate endDate, String day, Properties properties) {
        ReservedTime reservedTime;
        String[] timeData = tfTime.getText().split("-");

        if (endDate != null && startDate != null) {
            reservedTime = new ReservedTime(Day.valueOf(day), timeData[0], timeData[1], startDate, endDate);
        }
        else if (endDate == null && startDate != null) {
            reservedTime = new ReservedTime(Day.valueOf(day), timeData[0], timeData[1], startDate, startDate);
        }
        else {
            startDate = LocalDate.parse(properties.getProperty("startDate").replaceAll("\"", ""));
            endDate = LocalDate.parse(properties.getProperty("endDate").replaceAll("\"", ""));
            reservedTime = new ReservedTime(Day.valueOf(day), timeData[0], timeData[1], startDate, endDate);
        }

        return reservedTime;
    }

}
