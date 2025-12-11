package com.sistemreservasihotel.sistemreservasihotel;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    private static final String URL  = "jdbc:mysql://localhost:3306/reservasi_hotel";
    private static final String USER = "root";
    private static final String PASS = ""; // atau password MySQL kamu

    public static Connection getKoneksi() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
