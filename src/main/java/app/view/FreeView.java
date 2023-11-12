package app.view;

import app.util.Utils;
import com.raf.sk.specification.model.Appointment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import app.view.vbox.DefaultVBox;

import java.util.Objects;

public class FreeView  extends Stage {

    private static volatile FreeView instance = new FreeView();

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final TableView<Appointment> tvAppointments = new TableView<>();
    private final TableColumn<Appointment, String> tcDay = new TableColumn<>("DAY");
    private final TableColumn<Appointment, String> tcTime = new TableColumn<>("TIME");
    private final TableColumn<Appointment, String> tcDate = new TableColumn<>("DATE");
    private final TableColumn<Appointment, String> tcRoom = new TableColumn<>("ROOM");

    private final Button btnClose = new Button("Close");

    private FreeView() {
        init();
    }

    public static FreeView getInstance(){
        if(instance == null) {
            synchronized (FreeView.class) {
                if (instance == null) {
                    instance = new FreeView();
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private void init() {
        root.getChildren().addAll(tvAppointments, btnClose);

        Utils.getInstance().forceTableRefresh(tvAppointments, true);
        Utils.getInstance().generateColumns(tcDay, tcTime, tcRoom);

        tcDate.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            return new SimpleObjectProperty<>(appointment.getTime().getDate()).asString();
        });

        tvAppointments.getColumns().addAll(tcDay, tcTime, tcDate, tcRoom);
        btnClose.setOnAction(e -> this.close());

        super.setTitle("Free Appointments");
        super.setScene(new Scene(root, 500,500));
        this.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-orange.css")).toExternalForm());
    }

    public TableView<Appointment> getTvAppointments() {
        return tvAppointments;
    }

    public TableColumn<Appointment, String> getTcRoom() {
        return tcRoom;
    }

    public TableColumn<Appointment, String> getTcTime() {
        return tcTime;
    }

    public TableColumn<Appointment, String> getTcDay() {
        return tcDay;
    }

    public TableColumn<Appointment, String> getTcDate() {
        return tcDate;
    }
}
