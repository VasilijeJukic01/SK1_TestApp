package app.view;

import app.controller.ChangeController;
import app.core.Core;
import app.view.hbox.DefaultHBox;
import app.view.vbox.DefaultVBox;
import com.raf.sk.specification.model.Day;
import com.raf.sk.specification.model.ScheduleRoom;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ChangeView extends Stage {

    private static volatile ChangeView instance = new ChangeView();

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final Label lbRoom = new Label("Room");
    private final ComboBox<String> cbRoom = new ComboBox<>();
    private final Label lbDay = new Label("Day");
    private final ComboBox<String> cbDay = new ComboBox<>();
    private final Label lbTime = new Label("Time");
    private final TextField tfTime = new TextField();

    private final Button btnChange = new Button("Change");

    private final Map<String, TextField> textFields = new LinkedHashMap<>();

    private ChangeView() {
        init();
    }

    public static ChangeView getInstance(){
        if(instance == null) {
            synchronized (ChangeView.class) {
                if (instance == null) {
                    instance = new ChangeView();
                }
            }
        }
        return instance;
    }

    private void init() {
        root.getChildren().addAll(
                new DefaultHBox(Pos.CENTER, lbRoom, cbRoom),
                new DefaultHBox(Pos.CENTER, lbDay, cbDay),
                new DefaultHBox(Pos.CENTER, lbTime, tfTime)
        );

        cbDay.getItems().addAll(Arrays.stream(Day.values())
                .map(Day::toString)
                .toArray(String[]::new)
        );
        cbRoom.getItems().addAll(Core.getInstance().getSchedule().getRooms().stream()
                .map(ScheduleRoom::getName)
                .toArray(String[]::new)
        );

        for (String column : Core.getInstance().getColumns()) {
            Label label = new Label(column);
            TextField textField = new TextField();
            textFields.put(column, textField);
            root.getChildren().addAll(new DefaultHBox(Pos.CENTER, label, textField));
        }

        root.getChildren().add(btnChange);

        btnChange.setOnAction(new ChangeController(cbRoom, cbDay, tfTime, textFields));

        super.setTitle("Change Appointment");
        super.setScene(new Scene(root, 500,500));
        this.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-orange.css")).toExternalForm());
    }

    public ComboBox<String> getCbRoom() {
        return cbRoom;
    }

    public ComboBox<String> getDay() {
        return cbDay;
    }

    public TextField getTfTime() {
        return tfTime;
    }

    public Map<String, TextField> getTextFields() {
        return textFields;
    }

}

