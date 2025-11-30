package com.agendastudy.service;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private AnchorPane rootPane; // Assumindo que a raiz do FXML é um AnchorPane

    // --- Lógica da Tela 1 (Bem-vindo!) ---

    @FXML
    private void handleLogin() {
        // Em um app real, você abriria uma tela de formulário de login aqui.
        // Por enquanto, apenas um alerta simulado.
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText("A tela de Login será aberta para autenticação.");
        alert.showAndWait();

        // Simulação de chamada ao Backend (POST /api/usuarios/login)
        // Você precisaria de um formulário de email/senha para isso.
    }

    @FXML
    private void handleCadastro() {
        // Transição para a Tela 2 (Seleção de Tipo)
        try {
            // Carrega o FXML da próxima tela (ou uma parte do mesmo FXML, se for um único arquivo)
            // Aqui, vamos assumir que navegamos para a seleção de tipo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/CadastroTipo.fxml"));
            Parent cadastroTipoRoot = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(cadastroTipoRoot));
            stage.setTitle("AgendaStudy - Escolha o Tipo");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Erro ao carregar a tela de seleção de tipo.");
            alert.showAndWait();
        }
    }

    // --- Lógica da Tela 2 (Como gostaria de se cadastrar?) ---

    @FXML
    private void handleCadastroEstudante() {
        // Transição para a Tela 3 (Cadastro Estudante)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/CadastroEstudante.fxml"));
            Parent cadastroEstudanteRoot = loader.load();

            // Define o controller para passar referências, se necessário.
            // CadastroEstudanteController controller = loader.getController();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(cadastroEstudanteRoot));
            stage.setTitle("AgendaStudy - Cadastro Estudante");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCadastroProfessor() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Cadastro Professor");
        alert.setHeaderText(null);
        alert.setContentText("A tela de Cadastro de Professor será aberta em breve.");
        alert.showAndWait();
    }
}
