package app.view;

import app.controller.RemoveRoomController;
import app.view.vbox.DefaultVBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.Objects;

public class RemoveRoomView extends Stage {

    private static volatile RemoveRoomView instance = new RemoveRoomView();

    private final DefaultVBox root = new DefaultVBox(Pos.CENTER);

    private final ComboBox<String> cbRooms = new ComboBox<>();
    private final Button btnRemove = new Button("Remove");

    private RemoveRoomView() {
        init();
    }

    public static RemoveRoomView getInstance(){
        if(instance == null) {
            synchronized (RemoveRoomView.class) {
                if (instance == null) {
                    instance = new RemoveRoomView();
                }
            }
        }
        return instance;
    }

    private void init() {
        root.getChildren().addAll(cbRooms, btnRemove);
        btnRemove.setOnAction(new RemoveRoomController(cbRooms));
        super.setTitle("Remove Room");
        super.setResizable(false);
        super.setScene(new Scene(root, 300,300));
        this.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-orange.css")).toExternalForm());
    }

    public ComboBox<String> getCbRooms() {
        return cbRooms;
    }
}

