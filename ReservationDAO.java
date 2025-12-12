package com.sistemreservasihotel.sistemreservasihotel.dao;

import com.sistemreservasihotel.sistemreservasihotel.DBConnect;
import com.sistemreservasihotel.sistemreservasihotel.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class ReservationDAO {

    // CREATE RESERVATION (dipakai pengunjung)
    public boolean createReservation(int userId, int roomId,
                                     LocalDate in, LocalDate out,
                                     double total) {

        String sql = "INSERT INTO reservations (user_id, room_id, check_in_date, check_out_date, total_price, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, roomId);
            ps.setDate(3, Date.valueOf(in));
            ps.setDate(4, Date.valueOf(out));
            ps.setDouble(5, total);
            ps.setString(6, "DIBAYAR");

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("=== ERROR INSERT RESERVATION ===");
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // DIPAKAI RESEPSIONIS: ambil semua reservasi (join user + kamar)
    public ObservableList<Reservation> getAllReservations() {
        ObservableList<Reservation> list = FXCollections.observableArrayList();

        String sql = "SELECT r.id, r.user_id, r.room_id, r.check_in_date, r.check_out_date, " +
                     "r.total_price, r.status, u.full_name AS guest_name, ro.room_number " +
                     "FROM reservations r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "JOIN rooms ro ON r.room_id = ro.id " +
                     "ORDER BY r.id DESC";

        try (Connection conn = DBConnect.getKoneksi();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id"));
                r.setRoomId(rs.getInt("room_id"));
                r.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
                r.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
                r.setTotalPrice(rs.getDouble("total_price"));
                r.setStatus(rs.getString("status"));
                r.setGuestName(rs.getString("guest_name"));
                r.setRoomNumber(rs.getString("room_number"));

                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // DIPAKAI PENGUNJUNG: ambil reservasi milik 1 user
    public ObservableList<Reservation> getReservationsByUser(int userId) {
        ObservableList<Reservation> list = FXCollections.observableArrayList();

        String sql = "SELECT r.id, r.user_id, r.room_id, r.check_in_date, r.check_out_date, " +
                     "r.total_price, r.status, ro.room_number " +
                     "FROM reservations r " +
                     "JOIN rooms ro ON r.room_id = ro.id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.id DESC";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id"));
                r.setRoomId(rs.getInt("room_id"));
                r.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
                r.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
                r.setTotalPrice(rs.getDouble("total_price"));
                r.setStatus(rs.getString("status"));
                r.setRoomNumber(rs.getString("room_number"));

                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // UPDATE STATUS (DIPESAN / SELESAI / BATAL)
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";

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

    // DELETE (kalau mau benar-benar dihapus)
    public boolean deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
