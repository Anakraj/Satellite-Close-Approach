package com.example.orbittracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        /*
        *URL url = new File("src/main/java/ua/adeptius/goit/sample.fxml").toURI().toURL();
Parent root = FXMLLoader.load(url);
        * */
        URL url = new File("src/main/resources/scene.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);

        Scene scene = new Scene(root);

        URL url2 = new File("src/main/resources/styles.css").toURI().toURL();
        scene.getStylesheets().add(url2.toExternalForm());

        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
