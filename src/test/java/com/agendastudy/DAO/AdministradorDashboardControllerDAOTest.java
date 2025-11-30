package com.agendastudy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

// Classe de simulação manual do MainController para verificar chamadas
class MainControllerSimulado {
    boolean gerenciarUsuariosChamado = false;
    boolean relatoriosChamado = false;
    boolean logoutChamado = false;
    boolean goToAdminDashboardChamado = false;
    boolean goToAdminRelatoriosDashboardChamado = false;
    
    public void goToAdminGerenciarUsuarios() { gerenciarUsuariosChamado = true; }
    public void goToAdminRelatoriosDashboard() { relatoriosChamado = true; goToAdminRelatoriosDashboardChamado = true; }
    public void logout() { logoutChamado = true; }
    public void goToAdminDashboard() { goToAdminDashboardChamado = true; }
    public void goToAdminRankingProfessores() {}
    public void goToAdminTotalUsuarios() {}
    public void navigateWithData(String fxml, String id) {}
}

public class AdministradorDashboardControllerDAOTest {

    private AdministradorDashboardController controller;
    private MainControllerSimulado mainController;

    @BeforeEach
    void setUp() {
        controller = new AdministradorDashboardController();
        mainController = new MainControllerSimulado();
        controller.setMainController(mainController);
    }

    @Test
    void testHandleGerenciarUsuariosDeveNavegar() {
        controller.handleGerenciarUsuarios();
        assertTrue(mainController.gerenciarUsuariosChamado);
        assertFalse(mainController.relatoriosChamado);
    }

    @Test
    void testHandleRelatoriosDeveNavegar() {
        controller.handleRelatorios();
        assertTrue(mainController.relatoriosChamado);
        assertFalse(mainController.gerenciarUsuariosChamado);
    }

    @Test
    void testHandleLogoutDeveChamarLogout() {
        controller.handleLogout();
        assertTrue(mainController.logoutChamado);
    }
}
