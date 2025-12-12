package com.sistemreservasihotel.sistemreservasihotel.dao;

import com.sistemreservasihotel.sistemreservasihotel.DBConnect;
import com.sistemreservasihotel.sistemreservasihotel.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class RoomDAO {

    public ObservableList<Room> getAllRooms() {
        ObservableList<Room> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM rooms ORDER BY id ASC";

        try (Connection conn = DBConnect.getKoneksi();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price_per_night"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ObservableList<Room> getAvailableRooms() {
        ObservableList<Room> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM rooms WHERE status = 'TERSEDIA'";

        try (Connection conn = DBConnect.getKoneksi();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price_per_night"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addRoom(String number, String type, double price, String status) {
        String sql = "INSERT INTO rooms (room_number, type, price_per_night, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, number);
            ps.setString(2, type);
            ps.setDouble(3, price);
            ps.setString(4, status);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRoom(int id, String number, String type, double price, String status) {
        String sql = "UPDATE rooms SET room_number=?, type=?, price_per_night=?, status=? WHERE id=?";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, number);
            ps.setString(2, type);
            ps.setDouble(3, price);
            ps.setString(4, status);
            ps.setInt(5, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id=?";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateRoomStatus(int id, String status) {
        String sql = "UPDATE rooms SET status=? WHERE id=?";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
