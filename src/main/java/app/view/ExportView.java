package app.view;

import app.controller.ExportController;
import app.view.hbox.DefaultHBox;
import app.view.vbox.DefaultVBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.util.Objects;

public class ExportView extends Stage {

    private static volatile ExportView instance = new ExportView();

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final Label lbCSV = new Label("CSV");
    private final Label lbJSON = new Label("JSON");
    private final RadioButton rbCSV = new RadioButton();
    private final RadioButton rbJSON = new RadioButton();
    private final ToggleGroup tgExport = new ToggleGroup();

    private final Button btnSave = new Button("Save");

    private ExportView() {
        init();
    }

    public static ExportView getInstance(){
        if(instance == null) {
            synchronized (ExportView.class) {
                if (instance == null) {
                    instance = new ExportView();
                }
            }
        }
        return instance;
    }

    private void init() {
        root.getChildren().addAll(new DefaultHBox(Pos.CENTER, new DefaultHBox(Pos.CENTER, lbCSV, rbCSV), new DefaultHBox(Pos.CENTER, lbJSON, rbJSON)), btnSave);
        rbCSV.setToggleGroup(tgExport);
        rbJSON.setToggleGroup(tgExport);

        btnSave.setOnAction(new ExportController(rbCSV, rbJSON));

        super.setTitle("Export");
        super.setResizable(false);
        super.setScene(new Scene(root, 300,300));
        this.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-orange.css")).toExternalForm());
    }

}
