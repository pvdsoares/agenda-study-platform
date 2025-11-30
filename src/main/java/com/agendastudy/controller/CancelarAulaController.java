package com.agendastudy.controller;

import com.agendastudy.DAO.AulaDAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Usuario;
import com.agendastudy.service.ServicoAgendamento;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CancelarAulaController {

    @FXML
    private Label labelDisciplina;
    @FXML
    private Label labelProfessor;
    @FXML
    private Label labelDataHora;
    @FXML
    private AnchorPane rootPane;

    private Aula aula;
    private ServicoAgendamento servicoAgendamento;
    private Usuario usuarioLogado;

    public CancelarAulaController(ServicoAgendamento servicoAgendamento) {
        this.servicoAgendamento = servicoAgendamento;
    }

    public void setServicoAgendamento(ServicoAgendamento servicoAgendamento) {
        this.servicoAgendamento = servicoAgendamento;
    }

    public void carregarAula(Aula aula) {
        this.aula = aula;
        this.usuarioLogado = usuarioLogado;

        labelDisciplina.setText(aula.getTitulo());
        labelProfessor.setText("Prof: " + aula.getProfessor().getNome());
        labelDataHora.setText(aula.getDataHora().toString());

    }

    @FXML
    private void handleCancelarAula() {
        if (aula == null || usuarioLogado == null || servicoAgendamento == null) {
            mostrarAlerta("Erro Crítico", "Dados da aula ou serviço não carregados.");
            return;
        }

        try {
            servicoAgendamento.cancelarAgendamento(aula.getIdAula(), usuarioLogado);

            mostrarAlerta("Cancelamento", "Aula cancelada com sucesso!");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/agendastudy/view/cancelar-aula-confirmacao.fxml"));
            AnchorPane newPane = loader.load();

            if (rootPane != null) {
                rootPane.getScene().setRoot(newPane);
            }

        } catch (SecurityException e) {
            mostrarAlerta("Permissão Negada", "Você não tem permissão para cancelar esta aula.");
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao processar cancelamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoltar() {
        // Lógica para voltar à tela anterior.
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}