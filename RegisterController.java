package com.sistemreservasihotel.sistemreservasihotel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField txtFullName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Label lblStatus;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleRegister() {
        String fullName = txtFullName.getText().trim();
        String phone = txtPhone.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String confirm = txtConfirmPassword.getText().trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Nama, username, dan password wajib diisi.");
            return;
        }

        if (!password.equals(confirm)) {
            lblStatus.setText("Konfirmasi password tidak sama.");
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            lblStatus.setText("Username sudah digunakan, pilih yang lain.");
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setUsername(username);
        user.setPassword(password);

        boolean success = userDAO.register(user);

        if (success) {
            lblStatus.setText("");
            // setelah registrasi berhasil, kembali ke login
            try {
                App.setRoot("login");
            } catch (Exception e) {
                e.printStackTrace();
                lblStatus.setText("Registrasi berhasil, tapi gagal kembali ke login.");
            }
        } else {
            lblStatus.setText("Registrasi gagal. Coba lagi.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
