package com.sistemreservasihotel.sistemreservasihotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage mainStage;

    @Override
    public void start(Stage stage) {
        try {
            mainStage = stage;
            scene = new Scene(loadFXML("login"), 800, 600);

            stage.setTitle("Sistem Reservasi Hotel");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Gagal load halaman login!");
            e.printStackTrace();
        }
    }

    //Mengganti halaman (root FXML)
    public static void setRoot(String fxml) {
        try {
            Parent p = loadFXML(fxml);
            scene.setRoot(p);
        } catch (IOException e) {
            System.out.println("ERROR: Tidak bisa membuka halaman: " + fxml);
            e.printStackTrace();
        }
    }

    //Mengganti halaman dengan resolusi berbeda (opsional)
    public static void setScene(String fxml, int width, int height) {
        try {
            Parent p = loadFXML(fxml);
            Scene newScene = new Scene(p, width, height);
            mainStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("ERROR: Tidak bisa membuka scene: " + fxml);
            e.printStackTrace();
        }
    }

    //Loader FXML
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource(fxml + ".fxml")
        );
        return loader.load();
    }

    public static Stage getStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}
