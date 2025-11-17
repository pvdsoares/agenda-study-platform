package com.agendastudy.controller;

import javafx.fxml.FXML;

/**
 * Controller para o Dashboard de Relatórios Administrativos (SCRUM-73).
 * Gerencia a navegação entre os relatórios específicos.
 */
public class AdminRelatoriosController implements BaseController {

    private GerenciamentoUsuarioApp mainController;

    @FXML
    public void setMainController(GerenciamentoUsuarioApp mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleRankingProfessores() {
        mainController.goToAdminRankingProfessores();
    }

    @FXML
    private void handleTotalUsuarios() {
        mainController.goToAdminTotalUsuarios();
    }

    @FXML
    private void handleVoltar() {
        mainController.goToAdminDashboard();
    }
}
