package app.controller;

import com.raf.sk.specification.Schedule;
import com.raf.sk.specification.model.Appointment;
import app.util.Utils;
import component.MySchedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Core {

    private static volatile Core instance;

    private Schedule schedule;
    private List<String> columns = new ArrayList<>();

    private Core() {
        // Privremeno
        loadSchedule("src/main/resources/configuration.config", "src/main/resources/sraf.csv");
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

    public void loadSchedule(String configPath, String schedulePath) {
        Properties properties = Utils.getInstance().loadProperties(configPath);
        this.schedule = new MySchedule(properties);
        try {
            schedule.loadScheduleFromFile(schedulePath, properties);
            String[] col = properties.getProperty("columns").replaceAll("\"", "").split(",");
            columns.addAll(List.of(col));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Schedule getSchedule() {
        return schedule;
    }

    public List<Appointment> getAppointments() {
        return schedule.getReservedAppointments();
    }

    public List<String> getColumns() {
        return columns;
    }
}
