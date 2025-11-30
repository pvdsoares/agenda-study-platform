package com.agendastudy.controller;

import com.agendastudy.model.UsuarioAdminView;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Simulação manual da DAO para teste
class AdminGerenciamentoDAOSimulado {
    private final int[] dados;
    public AdminGerenciamentoDAOSimulado(int total, int estudantes, int professores) {
        this.dados = new int[]{total, estudantes, professores};
    }
    public int[] contarTotalUsuarios() { return dados; }
    public void alterarStatusConta(String id, boolean isAtivar) {}
    public void salvarUsuario(com.agendastudy.model.Usuario u) {}
    public com.agendastudy.model.Usuario buscarUsuarioPorId(String id) { return null; }
    public boolean buscarUsuarioAtivo(String id) { return true; }
    public java.util.List<UsuarioAdminView> buscarUsuariosComFiltro(String t, String tipo, String status) { return new java.util.ArrayList<>(); }
    public java.util.List gerarRankingAgendamentos(java.time.LocalDate d1, java.time.LocalDate d2) { return new java.util.ArrayList<>(); }
}

public class AdminTotalUsuariosControllerDAOTest {

    private AdminTotalUsuariosController controller;
    private MainControllerSimulado mainController;

    // Elementos FXML simulados
    private final Label totalGeralLabel = new Label();
    private final Label estudantesLabel = new Label();
    private final Label professoresLabel = new Label();

    @BeforeEach
    void setUp() throws Exception {
        this.controller = new AdminTotalUsuariosController();
        
        // Simulação da injeção de FXML via Reflection
        setPrivateField(controller, "totalGeralLabel", totalGeralLabel);
        setPrivateField(controller, "estudantesLabel", estudantesLabel);
        setPrivateField(controller, "professoresLabel", professoresLabel);
        
        // Injeção da DAO Simulado
        AdminGerenciamentoDAOSimulado daoSimulado = new AdminGerenciamentoDAOSimulado(150, 100, 50);
        setPrivateField(controller, "adminDAO", daoSimulado);

        this.mainController = new MainControllerSimulado();
        // setMainController aciona o carregamento de dados
        controller.setMainController(mainController);
    }
    
    // Método auxiliar para simular injeção via reflection
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testCarregarDadosDeveAtualizarLabelsCorretamente() {
        // Assert: Os dados devem ter sido carregados no setup (via setMainController)
        assertEquals("150", totalGeralLabel.getText());
        assertEquals("100", estudantesLabel.getText());
        assertEquals("50", professoresLabel.getText());
    }

    @Test
    void testHandleVoltarDeveNavegar() {
        controller.handleVoltar();
        assertTrue(mainController.goToAdminRelatoriosDashboardChamado);
    }
}
