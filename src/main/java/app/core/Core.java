package app.core;

import com.raf.sk.specification.Schedule;
import com.raf.sk.specification.model.Appointment;
import app.util.Utils;
import component.MySchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Core {

    private static volatile Core instance;

    private final Properties properties;
    private Schedule schedule;
    private List<String> columns = new ArrayList<>();

    private Core() {
        this.properties = Utils.getInstance().loadProperties("src/main/resources/configuration.config");
        this.schedule = new MySchedule(properties);
    }

    public static Core getInstance(){
        if(instance == null) {
            synchronized (Core.class) {
                if (instance == null) {
                    instance = new Core();
                }
            }
        }
        return instance;
    }

    public void newSchedule() {
        this.schedule = new MySchedule(properties);
        this.columns = List.of(properties.getProperty("columns").replaceAll("\"", "").split(","));
    }

    public Properties getProperties() {
        return properties;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public List<Appointment> getAppointments() {
        return schedule.getReservedAppointments();
    }

    public List<Appointment> getFreeAppointments() {
        return schedule.getFreeAppointments();
    }

    public List<String> getColumns() {
        return columns;
    }
}
