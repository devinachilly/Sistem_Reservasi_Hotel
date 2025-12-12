package com.sistemreservasihotel.sistemreservasihotel;

import com.sistemreservasihotel.sistemreservasihotel.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // LOGIN
    public User login(String username, String password) {
        User user = null;

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user; // null kalau gagal login
    }

    // CEK USERNAME SUDAH ADA BELUM
    public boolean isUsernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            return rs.next(); // true kalau ada baris 鈫� username sudah dipakai

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // REGISTER PENGUNJUNG BARU
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, full_name, phone, role) " +
                     "VALUES (?, ?, ?, ?, 'PENGUNJUNG')";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getPhone());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
