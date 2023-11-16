package app.view;

import app.controller.FilterController;
import app.core.Core;
import app.view.hbox.DefaultHBox;
import app.view.vbox.DefaultVBox;
import com.raf.sk.specification.model.Day;
import com.raf.sk.specification.model.ScheduleRoom;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Objects;

public class FilterView extends Stage {

    private static volatile FilterView instance = new FilterView();

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final Label lbRoom = new Label("Room");
    private final ComboBox<String> cbRoom = new ComboBox<>();
    private final Label lbDay = new Label("Day");
    private final ComboBox<String> cbDay = new ComboBox<>();
    private final Label lbTime = new Label("Time");
    private final TextField tfTime = new TextField();
    private final Label lbStartDate = new Label("Start Date");
    private final TextField tfStartDate = new TextField();
    private final Label lbEndDate = new Label("End Date");
    private final TextField tfEndDate = new TextField();

    private final Label lbCriteria = new Label("Criteria");
    private final ComboBox<String> cbCriteria = new ComboBox<>();

    private final RadioButton rbFree = new RadioButton("Free");
    private final RadioButton rbReserved = new RadioButton("Reserved");
    private final ToggleGroup tgAppointments = new ToggleGroup();

    private final Button btnFilter = new Button("Filter");

    private FilterView() {
        init();
    }

    public static FilterView getInstance(){
        if(instance == null) {
            synchronized (AddView.class) {
                if (instance == null) {
                    instance = new FilterView();
                }
            }
        }
        return instance;
    }

    private void init() {
        root.getChildren().addAll(
                new DefaultHBox(Pos.CENTER, rbFree, rbReserved),
                new DefaultHBox(Pos.CENTER, lbRoom, cbRoom),
                new DefaultHBox(Pos.CENTER, lbDay, cbDay),
                new DefaultHBox(Pos.CENTER, lbTime, tfTime),
                new DefaultHBox(Pos.CENTER, lbStartDate, tfStartDate),
                new DefaultHBox(Pos.CENTER, lbEndDate, tfEndDate),
                new DefaultHBox(Pos.CENTER, lbCriteria, cbCriteria)
        );
        tgAppointments.getToggles().addAll(rbFree, rbReserved);

        cbCriteria.getItems().addAll("Room", "Date", "Day, Time and Date");

        cbDay.getItems().addAll(Arrays.stream(Day.values())
                .map(Day::toString)
                .toArray(String[]::new)
        );
        cbRoom.getItems().addAll(Core.getInstance().getSchedule().getRooms().stream()
                .map(ScheduleRoom::getName)
                .toArray(String[]::new)
        );

        root.getChildren().add(btnFilter);

        btnFilter.setOnAction(new FilterController(cbRoom, cbDay, tfTime, tfStartDate, tfEndDate, cbCriteria, rbFree));

        autofill();

        super.setTitle("Filter Appointments");
        super.setScene(new Scene(root, 500,500));
        this.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-orange.css")).toExternalForm());
    }

    private void autofill() {
        cbRoom.getSelectionModel().select(0);
        cbDay.getSelectionModel().select(0);
        tfTime.setText("10:00-11:00");
        tfStartDate.setText("2023-01-01");
        tfEndDate.setText("2023-12-31");
        cbCriteria.setValue("Day, Time and Date");
    }

}