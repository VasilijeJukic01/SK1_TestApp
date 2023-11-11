package app.util;

import app.core.Core;
import com.raf.sk.specification.model.Appointment;
import com.raf.sk.specification.model.Day;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.FileInputStream;
import java.io.IOException;
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

    public Properties loadProperties(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void forceRefresh(TableView<Appointment> tvAppointments, boolean isFree) {
        if (isFree) {
            tvAppointments.setItems(FXCollections.observableArrayList(Core.getInstance().getFreeAppointments()));
        }
        else {
            tvAppointments.setItems(FXCollections.observableArrayList(Core.getInstance().getAppointments()));
        }
        for (String columnName : Core.getInstance().getColumns()) {
            if (columnName.equalsIgnoreCase("START_DATE") || columnName.equalsIgnoreCase("END_DATE")) {
                continue;
            }
            TableColumn<Appointment, String> column = getAppointmentStringTableColumn(columnName);
            tvAppointments.getColumns().add(column);
        }
    }

    private static TableColumn<Appointment, String> getAppointmentStringTableColumn(String columnName) {
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

    public String calculateAppointments() {
        return String.valueOf(Core.getInstance().getAppointments().size());
    }

    public String calculateFreeAppointments() {
        return String.valueOf(Core.getInstance().getSchedule().getFreeAppointments().size());
    }

}
