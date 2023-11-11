package app.view;

import app.controller.ImportController;
import app.controller.RemoveController;
import app.util.Utils;
import com.raf.sk.specification.model.Appointment;
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

    private final Label lbTotalAppointments = new Label("Total appointments: ");
    private final Label lbTotalAppointmentsValue = new Label();
    private final Label lbTotalFreeAppointments = new Label("Total free appointments: ");
    private final Label lbTotalFreeAppointmentsValue = new Label();

    private final Button btnAdd = new Button("Add");
    private final Button btnRemove = new Button("Remove");
    private final Button btnChange = new Button("Change");
    private final Button btnShowFree = new Button("Show Free");
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
                new DefaultHBox(Pos.CENTER_LEFT, lbTotalAppointments, lbTotalAppointmentsValue),
                new DefaultHBox(Pos.CENTER_LEFT, lbTotalFreeAppointments, lbTotalFreeAppointmentsValue),
                tvAppointments,
                new DefaultHBox(Pos.CENTER, btnAdd, btnRemove, btnChange, btnShowFree)
        );

        Utils.getInstance().forceRefresh(tvAppointments, false);

        lbTotalAppointmentsValue.setText(Utils.getInstance().calculateAppointments());
        lbTotalFreeAppointmentsValue.setText(Utils.getInstance().calculateFreeAppointments());

        Utils.getInstance().generateColumns(tcDay, tcTime, tcRoom);

        tvAppointments.getColumns().addAll(tcDay, tcTime, tcRoom);

        btnAdd.setOnAction(event -> AddView.getInstance().show());
        btnRemove.setOnAction(new RemoveController(tvAppointments));
        btnImport.setOnAction(new ImportController(tvAppointments));
        btnExport.setOnAction(event -> ExportView.getInstance().show());
        btnShowFree.setOnAction(event -> FreeView.getInstance().show());
    }

    public TableView<Appointment> getTvAppointments() {
        return tvAppointments;
    }

    public Label getLbTotalAppointmentsValue() {
        return lbTotalAppointmentsValue;
    }

    public Label getLbTotalFreeAppointmentsValue() {
        return lbTotalFreeAppointmentsValue;
    }

    public TableColumn<Appointment, String> getTcDay() {
        return tcDay;
    }

    public TableColumn<Appointment, String> getTcRoom() {
        return tcRoom;
    }

    public TableColumn<Appointment, String> getTcTime() {
        return tcTime;
    }
}
