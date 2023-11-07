package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import view.hbox.DefaultHBox;
import view.vbox.DefaultVBox;

public class MainView extends Stage {

    private static volatile MainView instance;

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final Button btnAdd = new Button("Add");
    private final Button btnRemove = new Button("Remove");
    private final Button btnChange = new Button("Change");
    private final Button btnImport = new Button("Import");
    private final Button btnExport = new Button("Export");
    private final TableView<String> tvAppointments = new TableView<>();

    private final TableColumn<String, String> tcDay = new TableColumn<>("Day");
    private final TableColumn<String, String> tcStartTime = new TableColumn<>("Start Time");
    private final TableColumn<String, String> tcEndTime = new TableColumn<>("End Time");

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
        this.show();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        this.root.getChildren().addAll(
                new DefaultHBox(Pos.CENTER, btnImport, btnExport),
                tvAppointments,
                new DefaultHBox(Pos.CENTER, btnAdd, btnRemove, btnChange)
        );
        tvAppointments.getColumns().addAll(tcDay, tcStartTime, tcEndTime);
    }

    public TableView<String> getTvAppointments() {
        return tvAppointments;
    }
}
