package com.agendastud.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Carrega o FXML da tela inicial (Tela 2)
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/agendastudy/view/TelaBusca.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 380, 700); // Defina um tamanho adequado
        stage.setTitle("AgendaStudy - Buscar Aulas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}