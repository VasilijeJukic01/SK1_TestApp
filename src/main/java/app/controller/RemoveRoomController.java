package app.controller;

import app.core.Core;
import app.util.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

public class RemoveRoomController implements EventHandler<ActionEvent> {

    private final ComboBox<String> cbRooms;

    public RemoveRoomController(ComboBox<String> cbRooms) {
        this.cbRooms = cbRooms;
    }

    @Override
    public void handle(ActionEvent event) {
        if (cbRooms.getSelectionModel().getSelectedItem() == null) return;
        Core.getInstance().getSchedule().deleteRoom(Core.getInstance().getSchedule().getRoomByName(cbRooms.getSelectionModel().getSelectedItem()));
        Utils.getInstance().fillRooms();
        Utils.getInstance().forceViewRefresh();
    }
}
