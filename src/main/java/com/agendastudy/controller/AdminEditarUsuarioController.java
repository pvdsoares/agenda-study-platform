package com.agendastudy.controller;

import com.agendastudy.DAO.AdminGerenciamentoDAO;
import com.agendastudy.model.Usuario;
import com.agendastudy.model.Professor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

/**
 * Controller para a tela de Edição e Bloqueio de Usuário (SCRUM-111, SCRUM-112).
 */
public class AdminEditarUsuarioController implements BaseController {

    private GerenciamentoUsuarioApp mainController;
    private final AdminGerenciamentoDAO adminDAO = new AdminGerenciamentoDAO();
    private Usuario usuarioAtual;

    // Elementos FXML
    @FXML private Label headerTitle;
    @FXML private Label userIdLabel;
    @FXML private Label userTypeLabel;
    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private TextField telefoneField;
    @FXML private Label statusLabel;
    @FXML private Button toggleStatusButton;

    @Override
    public void setMainController(GerenciamentoUsuarioApp mainController) {
        this.mainController = mainController;
    }
    
    /**
     * Carrega os dados do usuário a ser editado (chamado pelo MainController).
     * @param userId ID do usuário.
     */
    public void loadUserData(String userId) {
        usuarioAtual = adminDAO.buscarUsuarioPorId(userId);
        if (usuarioAtual != null) {
            preencherCampos();
        } else {
            headerTitle.setText("ERRO: Usuário não encontrado");
        }
    }
    
    private void preencherCampos() {
        String tipo = (usuarioAtual instanceof Professor) ? "Professor" : "Estudante";
        
        headerTitle.setText("EDITAR CONTA DE " + tipo.toUpperCase());
        userIdLabel.setText("ID: " + usuarioAtual.getId());
        userTypeLabel.setText("Tipo: " + tipo);
        
        nomeField.setText(usuarioAtual.getNome());
        emailField.setText(usuarioAtual.getEmail());
        telefoneField.setText(usuarioAtual.getTelefone() != null ? usuarioAtual.getTelefone() : "");
        
        atualizarStatusView();
    }
    
    private void atualizarStatusView() {
        boolean ativo = adminDAO.buscarUsuarioAtivo(usuarioAtual.getId());
        
        if (ativo) {
            statusLabel.setText("Status Atual: ATIVO");
            toggleStatusButton.setText("BLOQUEAR CONTA");
            toggleStatusButton.getStyleClass().setAll("action-button-toggle", "toggle-block");
        } else {
            statusLabel.setText("Status Atual: BLOQUEADO");
            toggleStatusButton.setText("DESBLOQUEAR CONTA");
            toggleStatusButton.getStyleClass().setAll("action-button-toggle", "toggle-unblock");
        }
    }

    /**
     * Salva os dados alterados do usuário (SCRUM-111).
     */
    @FXML
    private void handleSalvar() {
        if (usuarioAtual != null) {
            usuarioAtual.setNome(nomeField.getText());
            usuarioAtual.setTelefone(telefoneField.getText());
            
            adminDAO.salvarUsuario(usuarioAtual);
            
            // Feedback visual
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Alterações salvas com sucesso!");
            alert.showAndWait();
        }
    }

    /**
     * Bloqueia/Desbloqueia a conta (SCRUM-112).
     */
    @FXML
    private void handleToggleStatus() {
        if (usuarioAtual != null) {
            boolean isAtivoAtualmente = toggleStatusButton.getText().contains("BLOQUEAR");
            
            adminDAO.alterarStatusConta(usuarioAtual.getId(), isAtivoAtualmente);
            
            atualizarStatusView(); // Recarrega o estado do botão
        }
    }

    @FXML
    private void handleVoltar() {
        mainController.goToAdminGerenciarUsuarios();
    }
}
