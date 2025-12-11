package com.sistemreservasihotel.sistemreservasihotel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblStatus;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
    String username = txtUsername.getText().trim();
    String password = txtPassword.getText().trim();

    if (username.isEmpty() || password.isEmpty()) {
        lblStatus.setText("Username dan password wajib diisi.");
        return;
    }

    User user = userDAO.login(username, password);

    if (user == null) {
        lblStatus.setText("Login gagal! Username / password salah.");
    } else {
        lblStatus.setText("");

        try {
            Session.setCurrentUser(user); // simpan user login

            if ("RESEPSIONIS".equalsIgnoreCase(user.getRole())) {
                App.setRoot("resepsionis_dashboard");
            } else {
                App.setRoot("pengunjung_dashboard");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Gagal membuka dashboard.");
        }
    }
}


    @FXML
    private void handleGoToRegister() {
        try {
            App.setRoot("register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
