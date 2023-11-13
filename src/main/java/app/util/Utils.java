package app.util;

import app.core.Core;
import app.view.ChangeView;
import app.view.FreeView;
import app.view.MainView;
import app.view.RemoveRoomView;
import com.raf.sk.specification.model.Appointment;
import com.raf.sk.specification.model.Day;
import com.raf.sk.specification.model.ScheduleRoom;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Utils {

    private static volatile Utils instance;

    private Utils() {
    }

    public static Utils getInstance(){
        if(instance == null) {
            synchronized (Utils.class) {
                if (instance == null) {
                    instance = new Utils();
                }
            }
        }
        return instance;
    }

    public Properties loadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream(getClass().getResource("/configuration.config").getFile())) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void forceTableRefresh(TableView<Appointment> tvAppointments, boolean isFree) {
        if (isFree) tvAppointments.setItems(FXCollections.observableArrayList(Core.getInstance().getFreeAppointments()));
        else tvAppointments.setItems(FXCollections.observableArrayList(Core.getInstance().getAppointments()));

        for (String columnName : Core.getInstance().getColumns()) {
            if (columnName.equalsIgnoreCase("START_DATE") || columnName.equalsIgnoreCase("END_DATE")) {
                continue;
            }
            TableColumn<Appointment, String> column = getAppointmentStringTableColumn(columnName);
            tvAppointments.getColumns().add(column);
        }
    }

    public void forceViewRefresh() {
        MainView.getInstance().getLbTotalAppointmentsValue().setText(Utils.getInstance().calculateAppointments());
        MainView.getInstance().getLbTotalFreeAppointmentsValue().setText(Utils.getInstance().calculateFreeAppointments());
        MainView.getInstance().getTvAppointments().setItems(FXCollections.observableArrayList(Core.getInstance().getAppointments()));
        FreeView.getInstance().getTvAppointments().setItems(FXCollections.observableArrayList(Core.getInstance().getFreeAppointments()));
        MainView.getInstance().getTvAppointments().refresh();
        FreeView.getInstance().getTvAppointments().refresh();
    }

    private TableColumn<Appointment, String> getAppointmentStringTableColumn(String columnName) {
        TableColumn<Appointment, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (columnName.equalsIgnoreCase("START_DATE")) {
                return new SimpleObjectProperty<>(appointment.getTime().getStartDate()).asString();
            }
            if (columnName.equalsIgnoreCase("END_DATE")) {
                return new SimpleObjectProperty<>(appointment.getTime().getEndDate()).asString();
            }
            String cellValue = appointment.getData(columnName);
            return new SimpleObjectProperty<>(cellValue).asString();
        });
        return column;
    }

    public void generateColumns(TableColumn<Appointment, String> tcDay, TableColumn<Appointment, String> tcTime, TableColumn<Appointment, String> tcRoom) {
        tcDay.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            Day day = appointment.getTime().getDay();
            return new SimpleObjectProperty<>(day).asString();
        });
        tcTime.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            String timeStart = appointment.getTime().getStartTime();
            String timeEnd = appointment.getTime().getEndTime();
            return new SimpleObjectProperty<>(timeStart+"-"+timeEnd).asString();
        });
        tcRoom.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            return new SimpleObjectProperty<>(appointment.getScheduleRoom().getName()).asString();
        });
    }

    public void autoFillForChange(Appointment appointment) {
        int dayIndex = appointment.getTime().getDay().ordinal();
        int roomIndex = Core.getInstance().getSchedule().getRooms().indexOf(appointment.getScheduleRoom());
        String time = appointment.getTime().getStartTime()+"-"+appointment.getTime().getEndTime();
        ChangeView.getInstance().getDay().getSelectionModel().select(dayIndex);
        ChangeView.getInstance().getCbRoom().getSelectionModel().select(roomIndex);
        ChangeView.getInstance().getTfTime().setText(time);

        Map<String, TextField> textFields = ChangeView.getInstance().getTextFields();
        for (String s : textFields.keySet()) {
            if (s.equalsIgnoreCase("START_DATE")) {
                textFields.get(s).setText(String.valueOf(appointment.getTime().getStartDate()));
            }
            else if (s.equalsIgnoreCase("END_DATE")) {
                textFields.get(s).setText(String.valueOf(appointment.getTime().getEndDate()));
            }
            else {
                String dataValue = appointment.getData(s);
                textFields.get(s).setText(dataValue);
            }
        }
    }

    public void fillRooms() {
        RemoveRoomView.getInstance().getCbRooms().getItems().clear();
        RemoveRoomView.getInstance().getCbRooms().getItems().addAll(Core.getInstance().getSchedule().getRooms().stream()
                .map(ScheduleRoom::getName)
                .toArray(String[]::new)
        );
    }

    public String calculateAppointments() {
        return String.valueOf(Core.getInstance().getAppointments().size());
    }

    public String calculateFreeAppointments() {
        return String.valueOf(Core.getInstance().getSchedule().getFreeAppointments().size());
    }

}
