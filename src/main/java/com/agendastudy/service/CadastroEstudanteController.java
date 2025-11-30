package com.agendastudy.service;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class CadastroEstudanteController {

    // Endereço da API do Spring Boot. Mude 'localhost' se estiver testando em um dispositivo separado.
    private static final String API_URL = "http://localhost:8080/api/usuarios/cadastro/estudante";

    @FXML private AnchorPane rootPane;
    @FXML private TextField nomeCompletoField;
    @FXML private TextField localizacaoField;
    @FXML private TextField disciplinasField;
    @FXML private TextField telefoneField;
    @FXML private TextField emailField;
    @FXML private TextField senhaField;
    @FXML private TextField confirmarSenhaField; // Note: usar PasswordField é mais seguro em JavaFX

    private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    @FXML
    private void handleComecar() {
        String nomeCompleto = nomeCompletoField.getText();
        String localizacao = localizacaoField.getText();
        String disciplinas = disciplinasField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();
        String confirmarSenha = confirmarSenhaField.getText();

        if (!senha.equals(confirmarSenha)) {
            showAlert(AlertType.ERROR, "Erro de Senha", "As senhas não coincidem.");
            return;
        }

        if (nomeCompleto.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            showAlert(AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        // Monta o JSON para o Spring Boot
        String jsonPayload = String.format(
                "{\"nomeCompleto\": \"%s\", \"localizacao\": \"%s\", \"disciplinasPreferidas\": \"%s\", " +
                        "\"telefone\": \"%s\", \"email\": \"%s\", \"senha\": \"%s\"}",
                nomeCompleto, localizacao, disciplinas, telefone, email, senha
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            // Executa a requisição de forma síncrona (bom para exemplos simples em JavaFX)
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) { // 201 Created é o que esperamos
                showAlert(AlertType.CONFIRMATION, "Sucesso", "Cadastro de Estudante realizado com sucesso!");
                // Navegar para a tela principal ou login
                // handleVoltar();
            } else if (response.statusCode() == 400) {
                // Erro de validação do backend (ex: email já cadastrado)
                String responseBody = response.body();
                showAlert(AlertType.ERROR, "Erro de Cadastro", "O e-mail já está cadastrado ou houve outro erro de validação.");
                System.out.println("Erro do Backend: " + responseBody);
            } else {
                showAlert(AlertType.ERROR, "Erro", "Erro ao conectar com o servidor: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            showAlert(AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar ao backend Spring Boot.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoltar() {
        // Lógica para voltar à tela anterior (Seleção de Tipo)
        try {
            // Carrega a tela de seleção de tipo (ou outra tela de volta)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/CadastroTipo.fxml"));
            AnchorPane parent = loader.load();
            rootPane.getScene().setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}