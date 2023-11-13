package app.view;

import app.controller.ConfigurationController;
import app.view.vbox.DefaultVBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.Objects;

public class ConfigurationView extends Stage {

    private static volatile ConfigurationView instance = new ConfigurationView();

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final Button btnLoad = new Button("Load Configuration");
    private final Button btnSave = new Button("Save Changes");

    private ConfigurationView() {
        init();
    }

    public static ConfigurationView getInstance(){
        if(instance == null) {
            synchronized (ConfigurationView.class) {
                if (instance == null) {
                    instance = new ConfigurationView();
                }
            }
        }
        return instance;
    }

    private void init() {
        root.getChildren().addAll(btnLoad, btnSave);

        btnLoad.setOnAction(event -> {
            try {
                File file = new File(getClass().getResource("/configuration.config").getFile());
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (file.exists()) {
                        desktop.open(file);
                    }
                }
            } catch (Exception ignored) {}
        });

        btnSave.setOnAction(new ConfigurationController(MainView.getInstance().getTvAppointments()));

        super.setTitle("Export");
        super.setResizable(false);
        super.setScene(new Scene(root, 300,300));
        this.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-orange.css")).toExternalForm());
    }

}
