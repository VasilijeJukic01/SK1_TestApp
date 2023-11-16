package app.view;

import app.controller.ConfigurationController;
import app.core.Core;
import app.util.Utils;
import app.view.vbox.DefaultVBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.Properties;

public class ConfigurationView extends Stage {

    private static volatile ConfigurationView instance = new ConfigurationView();

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final Button btnUpload = new Button("Upload Configuration");
    private final Button btnClose = new Button("Save Changes");

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
        root.getChildren().addAll(btnUpload, btnClose);

        btnUpload.setOnAction(event -> {
            FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);
            String file = dialog.getFile();
            if (file != null) {
                Properties p = Utils.getInstance().loadPropertiesFromFile(new File(dialog.getDirectory() + file));
                Core.getInstance().setConfiguration(p);
            }
        });

        btnClose.setOnAction(new ConfigurationController(MainView.getInstance().getTvAppointments()));

        super.setTitle("Export");
        super.setResizable(false);
        super.setScene(new Scene(root, 300,300));
        this.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-orange.css")).toExternalForm());
    }

}
