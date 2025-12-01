package com.agendastudy.service;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Carrega a primeira tela (Tela 1: Bem-vindo!)
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 600); // Defina o tamanho ideal
        stage.setTitle("AgendaStudy");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}