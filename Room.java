package com.sistemreservasihotel.sistemreservasihotel;

public class Room {

    private int id;
    private String roomNumber;
    private String type;
    private double pricePerNight;
    private String status; // TERSEDIA, DIPESAN, DIPAKAI, TIDAK_AKTIF

    public Room() {}

    public Room(int id, String roomNumber, String type, double pricePerNight, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    // ===== GETTER & SETTER WAJIB SESUAI NAMA KOLOM TABEL =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // OPSIONAL: sangat membantu saat tampilkan data di debug
    @Override
    public String toString() {
        return roomNumber + " (" + type + ")";
    }
}
