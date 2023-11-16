package app.view;

import app.controller.AddRoomController;
import app.view.hbox.DefaultHBox;
import app.view.vbox.DefaultVBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class AddRoomView extends Stage {

    private static volatile AddRoomView instance = new AddRoomView();

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final Label lbRoom = new Label("Room Name");
    private final TextField tfRoom = new TextField();
    private final Label lbCapacity = new Label("Capacity");
    private final TextField tfCapacity = new TextField();
    private final Label lbData = new Label("Data(Name-Capacity) - split by ','");
    private final TextArea tfData = new TextArea();

    private final Button btnAdd = new Button("Add");

    private AddRoomView() {
        init();
    }

    public static AddRoomView getInstance(){
        if(instance == null) {
            synchronized (AddRoomView.class) {
                if (instance == null) {
                    instance = new AddRoomView();
                }
            }
        }
        return instance;
    }

    private void init() {
        root.getChildren().addAll(
                new DefaultHBox(Pos.CENTER, lbRoom, tfRoom),
                new DefaultHBox(Pos.CENTER, lbCapacity, tfCapacity),
                lbData, tfData
        );

        root.getChildren().add(btnAdd);

        btnAdd.setOnAction(new AddRoomController(tfRoom, tfCapacity, tfData));

        super.setTitle("Add Room");
        super.setScene(new Scene(root, 500,500));
        this.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-orange.css")).toExternalForm());
    }
}

