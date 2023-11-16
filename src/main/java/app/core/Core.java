package app.core;

import com.raf.sk.specification.Manager;
import com.raf.sk.specification.Schedule;
import com.raf.sk.specification.model.Appointment;
import app.util.Utils;

import java.util.List;
import java.util.Properties;

public class Core {

    private static volatile Core instance;

    private Properties properties;
    private final Schedule schedule;
    private List<String> columns;

    private Core() {
        this.properties = Utils.getInstance().loadProperties();
        this.columns = List.of(properties.getProperty("columns").replaceAll("\"", "").split(","));
        try {
            Class.forName("component.MySchedule");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.schedule = Manager.getSchedule();
        this.schedule.initSchedule(properties);
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
        schedule.setConfig(properties);
        this.columns = List.of(properties.getProperty("columns").replaceAll("\"", "").split(","));
    }

    public void setConfiguration() {
        this.properties = Utils.getInstance().loadProperties();
        this.schedule.setConfig(properties);
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
