package com.sistemreservasihotel.sistemreservasihotel;

import com.sistemreservasihotel.sistemreservasihotel.dao.RoomDAO;
import com.sistemreservasihotel.sistemreservasihotel.dao.ReservationDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;

public class ResepsionisDashboardController {

    @FXML private Label lblWelcome;

    @FXML private TableView<Room> tableRooms;
    @FXML private TableColumn<Room, String> colRoomNumber;
    @FXML private TableColumn<Room, String> colType;
    @FXML private TableColumn<Room, Double> colPrice;
    @FXML private TableColumn<Room, String> colStatus;

    @FXML private TableView<Reservation> tableReservations;
    @FXML private TableColumn<Reservation, String> colResGuest;
    @FXML private TableColumn<Reservation, String> colResRoom;
    @FXML private TableColumn<Reservation, String> colResIn;
    @FXML private TableColumn<Reservation, String> colResOut;
    @FXML private TableColumn<Reservation, Double> colResTotal;
    @FXML private TableColumn<Reservation, String> colResStatus;

    private final RoomDAO roomDAO = new RoomDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    private void initialize() {

        User u = Session.getCurrentUser();
        if (u != null) {
            lblWelcome.setText("Halo, " + u.getFullName() + " (Resepsionis)");
        } else {
            lblWelcome.setText("Halo, Resepsionis");
        }

        //tabel kamar
        colRoomNumber.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getRoomNumber()));
        colType.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        colPrice.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPricePerNight()));
        colStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        //tabel reservasi
        colResGuest.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getGuestName()));
        colResRoom.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getRoomNumber()));
        colResIn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCheckInDate().format(dateFmt)));
        colResOut.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCheckOutDate().format(dateFmt)));
        colResTotal.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalPrice()));
        colResStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        loadRooms();
        loadReservations();
    }

    //LOAD DATA

    private void loadRooms() {
        ObservableList<Room> rooms = roomDAO.getAllRooms();
        tableRooms.setItems(rooms);
    }

    private void loadReservations() {
        ObservableList<Reservation> list = reservationDAO.getAllReservations();
        tableReservations.setItems(list);
    }

    //KAMAR

    @FXML
    private void handleRefreshRooms() {
        loadRooms();
    }

    @FXML
    private void handleAddRoom() {
        TextInputDialog dNumber = new TextInputDialog();
        dNumber.setHeaderText("Tambah Kamar Baru");
        dNumber.setContentText("Nomor kamar:");
        String number = dNumber.showAndWait().orElse(null);
        if (number == null || number.trim().isEmpty()) return;

        TextInputDialog dType = new TextInputDialog();
        dType.setHeaderText("Tambah Kamar Baru");
        dType.setContentText("Tipe kamar (Standard/Deluxe/Suite):");
        String type = dType.showAndWait().orElse(null);
        if (type == null || type.trim().isEmpty()) return;

        TextInputDialog dPrice = new TextInputDialog();
        dPrice.setHeaderText("Tambah Kamar Baru");
        dPrice.setContentText("Harga per malam:");
        String priceStr = dPrice.showAndWait().orElse(null);
        if (priceStr == null || priceStr.trim().isEmpty()) return;

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            showInfo("Harga harus berupa angka.");
            return;
        }

        boolean ok = roomDAO.addRoom(number.trim(), type.trim(), price, "TERSEDIA");
        if (ok) {
            showInfo("Kamar berhasil ditambahkan.");
            loadRooms();
        } else {
            showInfo("Gagal menambah kamar.");
        }
    }

    @FXML
    private void handleEditRoom() {
        Room selected = tableRooms.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Pilih kamar yang akan diedit.");
            return;
        }

        TextInputDialog dNumber = new TextInputDialog(selected.getRoomNumber());
        dNumber.setHeaderText("Edit Kamar");
        dNumber.setContentText("Nomor kamar:");
        String number = dNumber.showAndWait().orElse(null);
        if (number == null || number.trim().isEmpty()) return;

        TextInputDialog dType = new TextInputDialog(selected.getType());
        dType.setHeaderText("Edit Kamar");
        dType.setContentText("Tipe kamar:");
        String type = dType.showAndWait().orElse(null);
        if (type == null || type.trim().isEmpty()) return;

        TextInputDialog dPrice = new TextInputDialog(String.valueOf(selected.getPricePerNight()));
        dPrice.setHeaderText("Edit Kamar");
        dPrice.setContentText("Harga per malam:");
        String priceStr = dPrice.showAndWait().orElse(null);
        if (priceStr == null || priceStr.trim().isEmpty()) return;

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            showInfo("Harga harus berupa angka.");
            return;
        }

        boolean ok = roomDAO.updateRoom(selected.getId(), number.trim(), type.trim(), price, selected.getStatus());

        if (ok) {
            showInfo("Kamar berhasil diupdate.");
            loadRooms();
        } else {
            showInfo("Gagal mengupdate kamar.");
        }
    }

    @FXML
    private void handleDeleteRoom() {
        Room selected = tableRooms.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Pilih kamar yang akan dihapus.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Yakin ingin menghapus kamar " + selected.getRoomNumber() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Konfirmasi Hapus");

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            boolean ok = roomDAO.deleteRoom(selected.getId());
            if (ok) {
                showInfo("Kamar berhasil dihapus.");
                loadRooms();
            } else {
                showInfo("Gagal menghapus kamar.");
            }
        }
    }

    //RESERVASI

    @FXML
    private void handleRefreshReservations() {
        loadReservations();
    }

    @FXML
    private void handleMarkFinished() {
        Reservation res = tableReservations.getSelectionModel().getSelectedItem();
        if (res == null) {
            showInfo("Pilih reservasi terlebih dahulu.");
            return;
        }

        boolean ok = reservationDAO.updateStatus(res.getId(), "CHECKED_OUT");

        if (ok) {
            roomDAO.updateRoomStatus(res.getRoomId(), "TERSEDIA");
            showInfo("Reservasi ditandai selesai.");
            loadReservations();
            loadRooms();
        } else {
            showInfo("Gagal mengubah status reservasi.");
        }
    }
    
    @FXML
private void handleCheckIn() {
    Reservation res = tableReservations.getSelectionModel().getSelectedItem();
    if (res == null) {
        showInfo("Pilih reservasi terlebih dahulu.");
        return;
    }

    // Cek status
    if (!res.getStatus().equals("DIBAYAR")) {
        showInfo("Hanya reservasi yang sudah dibayar yang bisa di-check-in.");
        return;
    }

    // Ubah status reservasi
    boolean ok = reservationDAO.updateStatus(res.getId(), "CHECKED_IN");

    if (ok) {
        // Ubah status kamar ke DIPESAN
        roomDAO.updateRoomStatus(res.getRoomId(), "DIPESAN");

        showInfo("Tamu berhasil Check-In.");
        loadReservations();
        loadRooms();
    } else {
        showInfo("Gagal menandai Check-In.");
    }
}

    @FXML
    private void handleCancelReservation() {
        Reservation res = tableReservations.getSelectionModel().getSelectedItem();
        if (res == null) {
            showInfo("Pilih reservasi yang akan dibatalkan.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Batalkan reservasi kamar " + res.getRoomNumber() +
                " atas nama " + res.getGuestName() + "?",
                ButtonType.YES, ButtonType.NO);

        confirm.setHeaderText("Konfirmasi Pembatalan");
        if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;

        boolean ok = reservationDAO.updateStatus(res.getId(), "DIBATALKAN");
        roomDAO.updateRoomStatus(res.getRoomId(), "TERSEDIA");

        if (ok) {
            showInfo("Reservasi dibatalkan.");
            loadReservations();
            loadRooms();
        } else {
            showInfo("Gagal membatalkan reservasi.");
        }
    }

    //LOGOUT

    @FXML
    private void handleLogout() {
        Session.clear();
        try {
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //HELPER

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
