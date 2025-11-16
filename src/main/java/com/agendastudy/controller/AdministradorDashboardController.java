package com.agendastudy.controller;

import javafx.fxml.FXML;

/**
 * Controller para o Dashboard do Administrador.
 */
public class AdministradorDashboardController implements BaseController {

    private GerenciamentoUsuarioApp mainController;

    @FXML
    public void setMainController(GerenciamentoUsuarioApp mainController) {
        this.mainController = mainController;
    }

    // Link para a tela de Gerenciamento de Usuários (SCRUM-113)
    @FXML
    private void handleGerenciarUsuarios() {
        mainController.goToAdminGerenciarUsuarios();
    }

    // Link para o Dashboard de Relatórios (SCRUM-73)
    @FXML
    private void handleRelatorios() {
        mainController.goToAdminRelatoriosDashboard();
    }

    @FXML
    private void handleLogout() {
        if (mainController != null) {
            mainController.logout();
        }
    }
}
