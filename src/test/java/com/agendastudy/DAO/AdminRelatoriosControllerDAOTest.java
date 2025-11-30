package com.agendastudy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Classe de simulação manual, estendida para relatórios
class MainControllerRelatoriosSimulado extends MainControllerSimulado {
    boolean rankingChamado = false;
    boolean totalUsuariosChamado = false;
    
    @Override public void goToAdminRankingProfessores() { rankingChamado = true; }
    @Override public void goToAdminTotalUsuarios() { totalUsuariosChamado = true; }
}

public class AdminRelatoriosControllerDAOTest {

    private AdminRelatoriosController controller;
    private MainControllerRelatoriosSimulado mainController;

    @BeforeEach
    void setUp() {
        controller = new AdminRelatoriosController();
        mainController = new MainControllerRelatoriosSimulado();
        controller.setMainController(mainController);
    }

    @Test
    void testHandleRankingProfessoresDeveNavegar() {
        controller.handleRankingProfessores();
        assertTrue(mainController.rankingChamado);
    }

    @Test
    void testHandleTotalUsuariosDeveNavegar() {
        controller.handleTotalUsuarios();
        assertTrue(mainController.totalUsuariosChamado);
    }

    @Test
    void testHandleVoltarDeveNavegarParaDashboard() {
        controller.handleVoltar();
        assertTrue(mainController.goToAdminDashboardChamado);
    }
}
