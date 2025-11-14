package com.agendastudy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CancelarAulaConfirmacaoController{

    @FXML
    private void handleVoltar(){
        // decidir para onde voltar
        // trocar a tela para "Minhas aulas"

        System.out.println("Voltando para Minhas Aulas...");

        //fechar a janela atual
        Stage stage = (Stage) ((Button) (null)).getScene().getWindow();
        stage.close();
    }
}