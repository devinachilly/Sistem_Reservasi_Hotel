package com.sistemreservasihotel.sistemreservasihotel;

import com.sistemreservasihotel.sistemreservasihotel.dao.RoomDAO;
import com.sistemreservasihotel.sistemreservasihotel.dao.ReservationDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

public class PengunjungDashboardController {

    @FXML private Label lblWelcome;

    @FXML private TableView<Room> tableRooms;
    @FXML private TableColumn<Room, String> colNumber;
    @FXML private TableColumn<Room, String> colType;
    @FXML private TableColumn<Room, Double> colPrice;

    @FXML private DatePicker dpCheckIn;
    @FXML private DatePicker dpCheckOut;

    @FXML private Label lblTotal;

    private User currentUser;
    private RoomDAO roomDAO = new RoomDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();

    private Room selectedRoom;
    private double totalPrice = 0;

    @FXML
    private void initialize() {

        currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            lblWelcome.setText("Halo, " + currentUser.getFullName());
        }

        colNumber.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRoomNumber()));
        colType.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPricePerNight()));

        loadRooms();

        tableRooms.setOnMouseClicked(e -> {
            selectedRoom = tableRooms.getSelectionModel().getSelectedItem();
        });
    }

    // LOAD KAMAR
    private void loadRooms() {
        ObservableList<Room> list = roomDAO.getAvailableRooms();
        tableRooms.setItems(list);
    }

    // HITUNG TOTAL
    @FXML
    private void handleCalculate() {

        if (selectedRoom == null) {
            showAlert("Pilih kamar terlebih dahulu!");
            return;
        }

        LocalDate in = dpCheckIn.getValue();
        LocalDate out = dpCheckOut.getValue();

        if (in == null || out == null) {
            showAlert("Tanggal check-in dan check-out tidak boleh kosong!");
            return;
        }

        if (!out.isAfter(in)) {
            showAlert("Tanggal check-out harus setelah check-in!");
            return;
        }

        long days = ChronoUnit.DAYS.between(in, out);
        totalPrice = days * selectedRoom.getPricePerNight();

        lblTotal.setText("Rp " + totalPrice);
    }

    // PESAN KAMAR
    @FXML
    private void handleOrder() {

        if (selectedRoom == null) {
            showAlert("Pilih kamar terlebih dahulu!");
            return;
        }

        LocalDate in = dpCheckIn.getValue();
        LocalDate out = dpCheckOut.getValue();

        if (in == null || out == null || totalPrice == 0) {
            showAlert("Isi tanggal dan hitung total terlebih dahulu!");
            return;
        }

        boolean ok = reservationDAO.createReservation(
                currentUser.getId(),
                selectedRoom.getId(),
                in,
                out,
                totalPrice
        );

        if (ok) {
            roomDAO.updateRoomStatus(selectedRoom.getId(), "DIPESAN");
            showAlert("Kamar berhasil dipesan!");
            loadRooms();
        } else {
            showAlert("Gagal memesan!");
        }
    }

    // LOGOUT
    @FXML
    private void handleLogout() {
        Session.clear();
        try {
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ALERT HELPER
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
