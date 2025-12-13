package com.agendastudy.controller;

import com.agendastudy.DAO.UsuarioDAO;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO() {
    };

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha email e senha.");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            if (!usuario.isAtivo()) {
                mostrarAlerta(AlertType.ERROR, "Acesso Negado", "Esta conta foi desativada.");
                return;
            }

            // Verificação extra para garantir que não é admin (caso o banco ainda tenha)
            // Como removemos a classe Admin, o instanceof falharia/não compilaria se
            // usássemos a classe.
            // Mas o objeto usuario recuperado do DAO genérico pode ser problema se o DAO
            // tentar instanciar Administrador.

            navegarParaHome(usuario);
        } else {
            mostrarAlerta(AlertType.ERROR, "Falha no Login", "Email ou senha incorretos.");
        }
    }

    private void navegarParaHome(Usuario usuario) {
        try {
            String fxmlDestino;

            if (usuario instanceof Estudante) {
                fxmlDestino = "/com/agendastudy/view/tela-suas-aulas.fxml";
            } else if (usuario instanceof Professor) {
                fxmlDestino = "/com/agendastudy/view/ProfessorProfile.fxml";
            } else {
                mostrarAlerta(AlertType.ERROR, "Erro", "Tipo de usuário não suportado ou desconhecido.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlDestino));
            Parent root = loader.load();

            // Configurar Controller de Destino
            if (usuario instanceof Estudante) {
                TelaSuasAulasController controller = loader.getController();
                controller.configurarEstudante((Estudante) usuario);
            } else if (usuario instanceof Professor) {
                PerfilProfessorController controller = loader.getController();
                controller.setProfessor((Professor) usuario);
            }

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();

            String titulo = "AgendaStudy - " + usuario.getNome();
            stage.setTitle(titulo);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro de Navegação",
                    "Não foi possível carregar a tela inicial: " + e.getMessage());
        } catch (ClassCastException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro Interno", "Erro de conversão de tipo de usuário.");
        }
    }

    @FXML
    private void handleCadastro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/view/CadastroTipo.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("AgendaStudy - Escolha o Tipo");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro", "Erro ao carregar a tela de seleção de tipo.");
        }
    }

    @FXML
    private void handleVoltar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("AgendaStudy - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Adicionado para suportar os botões de cadastro direto no controller
    // (reaproveitando fxml antigo se necessário)
    @FXML
    private void handleCadastroEstudante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/view/CadastroEstudante.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("AgendaStudy - Cadastro Estudante");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCadastroProfessor() {
        mostrarAlerta(AlertType.INFORMATION, "Em Breve", "Cadastro de professor será implementado em breve.");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
