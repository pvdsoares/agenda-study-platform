package com.agendastudy.controller;

import com.agendastudy.DAO.AdminGerenciamentoDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller para o Relatório de Total de Usuários (SCRUM-75).
 */
public class AdminTotalUsuariosController implements BaseController {

    private GerenciamentoUsuarioApp mainController;
    private final AdminGerenciamentoDAO adminDAO = new AdminGerenciamentoDAO();

    @FXML private Label totalGeralLabel;
    @FXML private Label estudantesLabel;
    @FXML private Label professoresLabel;

    @Override
    public void setMainController(GerenciamentoUsuarioApp mainController) {
        this.mainController = mainController;
        carregarDados();
    }

    @FXML
    public void initialize() {
        // Inicializado via setMainController
    }

    private void carregarDados() {
        int[] resumo = adminDAO.contarTotalUsuarios(); // [Total, Estudantes, Professores]
        
        totalGeralLabel.setText(String.valueOf(resumo[0]));
        estudantesLabel.setText(String.valueOf(resumo[1]));
        professoresLabel.setText(String.valueOf(resumo[2]));
    }

    @FXML
    private void handleVoltar() {
        mainController.goToAdminRelatoriosDashboard();
    }
}
