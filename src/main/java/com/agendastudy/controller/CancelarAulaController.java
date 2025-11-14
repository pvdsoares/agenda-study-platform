package com.agendastudy.controller;

import com.agendastudy.DAO.AulaDAO;
import com.agendastudy.model.Aula;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class CancelarAulaController{

    @FXML private Label labelDisciplina;
    @FXML private Label labelProfessor;
    @FXML private Label labelDataHora;

    private Aula aula;
    private AulaDAO aulaDAO = new AulaDAO();

    public void carregarAula (Aula aula){
        this.aula = aula;

        labelDisciplina.setText(aula.getTitulo());
        labelProfessor.setText("Prof: " + aula.getProfessor().getNome());
        labelDataHora.setText(aula.getDataHora().toString());
        
    }

    @FXML 
    private void handleCancelarAula(){
        try{
            aulaDAO.cancelarAula(aula.getIdAula());
            abrirTelaConfirmacao();
        } catch (Exception e){
            mostrarAlerta("Erro", e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}