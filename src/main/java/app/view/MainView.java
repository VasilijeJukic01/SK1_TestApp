package app.view;

import app.controller.RemoveController;
import com.raf.sk.specification.model.Appointment;
import app.controller.Core;
import com.raf.sk.specification.model.Day;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import app.view.hbox.DefaultHBox;
import app.view.vbox.DefaultVBox;

import java.util.Objects;

public class MainView extends Stage {

    private static volatile MainView instance;

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final Button btnAdd = new Button("Add");
    private final Button btnRemove = new Button("Remove");
    private final Button btnChange = new Button("Change");
    private final Button btnImport = new Button("Import");
    private final Button btnExport = new Button("Export");
    private final TableView<Appointment> tvAppointments = new TableView<>();

    private final TableColumn<Appointment, String> tcDay = new TableColumn<>("DAY");
    private final TableColumn<Appointment, String> tcTime = new TableColumn<>("TIME");
    private final TableColumn<Appointment, String> tcRoom = new TableColumn<>("ROOM");

    private MainView() {
        init();
    }

    public static MainView getInstance(){
        if(instance == null) {
            synchronized (MainView.class) {
                if (instance == null) {
                    instance = new MainView();
                }
            }
        }
        return instance;
    }

    private void init() {
        initComponents();
        this.setTitle("Schedule App");
        this.setScene(new Scene(root, 800, 600));
        this.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-orange.css")).toExternalForm());
        this.show();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        this.root.getChildren().addAll(
                new DefaultHBox(Pos.CENTER, btnImport, btnExport),
                tvAppointments,
                new DefaultHBox(Pos.CENTER, btnAdd, btnRemove, btnChange)
        );

        tvAppointments.setItems(FXCollections.observableArrayList(Core.getInstance().getAppointments()));

        for (String columnName : Core.getInstance().getColumns()) {
            TableColumn<Appointment, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(cellData -> {
                Appointment appointment = cellData.getValue();
                String cellValue = appointment.getData(columnName);
                return new SimpleObjectProperty<>(cellValue).asString();
            });
            tvAppointments.getColumns().add(column);
        }

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
        tvAppointments.getColumns().addAll(tcDay, tcTime, tcRoom);

        btnRemove.setOnAction(new RemoveController(tvAppointments));
        btnExport.setOnAction(event -> ExportView.getInstance().show());
    }

    public TableView<Appointment> getTvAppointments() {
        return tvAppointments;
    }
}
