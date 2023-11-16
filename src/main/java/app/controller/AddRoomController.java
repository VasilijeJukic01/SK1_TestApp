package app.controller;

import app.core.Core;
import com.raf.sk.specification.model.Equipment;
import com.raf.sk.specification.model.ScheduleRoom;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class AddRoomController implements EventHandler<ActionEvent> {

    private final TextField tfRoom;
    private final TextField tfCapacity;
    private final TextArea tfData;

    public AddRoomController(TextField tfRoom, TextField tfCapacity, TextArea tfData) {
        this.tfRoom = tfRoom;
        this.tfCapacity = tfCapacity;
        this.tfData = tfData;
    }

    @Override
    public void handle(ActionEvent event) {
        String room = tfRoom.getText();
        String capacity = tfCapacity.getText();
        String data = tfData.getText();

        if(room.isEmpty() || capacity.isEmpty() || data.isEmpty()) return;

        List<Equipment> equipment = new ArrayList<>();
        String[] split = data.split(",");
        for (String s : split) {
            String[] split1 = s.split("-");
            if(split1.length != 2) return;
            equipment.add(new Equipment(split1[0], Integer.parseInt(split1[1])));
        }

        ScheduleRoom scheduleRoom = new ScheduleRoom(room, Integer.parseInt(capacity), equipment);
        Core.getInstance().getSchedule().addRoom(scheduleRoom);

    }

}
