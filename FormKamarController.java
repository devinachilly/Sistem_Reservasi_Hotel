package com.sistemreservasihotel.sistemreservasihotel;

import com.sistemreservasihotel.sistemreservasihotel.dao.RoomDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormKamarController {

    @FXML private Label lblTitle;
    @FXML private TextField txtRoomNumber;
    @FXML private TextField txtType;
    @FXML private TextField txtPrice;
    @FXML private ComboBox<String> cbStatus;

    private RoomDAO roomDAO = new RoomDAO();
    private Room editingRoom = null;

    public void setRoom(Room room) {
        editingRoom = room;
        lblTitle.setText("Edit Kamar");

        txtRoomNumber.setText(room.getRoomNumber());
        txtType.setText(room.getType());
        txtPrice.setText(String.valueOf(room.getPricePerNight()));
        cbStatus.setValue(room.getStatus());
    }

    @FXML
    private void handleSave() {
        String number = txtRoomNumber.getText();
        String type = txtType.getText();
        double price = Double.parseDouble(txtPrice.getText());
        String status = cbStatus.getValue();

        if (editingRoom == null) {
            roomDAO.addRoom(number, type, price, status);
        } else {
            roomDAO.updateRoom(editingRoom.getId(), number, type, price, status);
        }

        close();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) txtRoomNumber.getScene().getWindow();
        stage.close();
    }
}
