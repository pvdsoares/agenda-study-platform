package com.agendastudy.controller;

import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Usuario;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Simulação da DAO para edição de usuário
class AdminGerenciamentoDAOEdicaoSimulado extends AdminGerenciamentoDAOSimulado {
    private final Usuario professorAtivo = new Professor("p123", "Prof Ativo", "p@a.com", "pass", "111");
    private final Usuario estudanteBloqueado = new Estudante("e456", "Estudante Bloqueado", "e@b.com", "pass", null);
    private boolean isAtivo = false;
    public boolean statusAlterado = false;
    public Usuario usuarioSalvo = null;
    
    public AdminGerenciamentoDAOEdicaoSimulado() { super(0, 0, 0); isAtivo = true; } 
    
    @Override
    public Usuario buscarUsuarioPorId(String id) {
        if (id.equals("p123")) return professorAtivo;
        if (id.equals("e456")) return estudanteBloqueado;
        return null;
    }
    
    @Override
    public boolean buscarUsuarioAtivo(String id) { 
        if (id.equals("p123") && statusAlterado) return isAtivo; 
        if (id.equals("e456") && !statusAlterado) return false; 
        return isAtivo;
    }

    @Override
    public void alterarStatusConta(String id, boolean isBloquear) {
        statusAlterado = true;
        isAtivo = !isBloquear; 
    }
    
    @Override
    public void salvarUsuario(Usuario u) {
        usuarioSalvo = u;
    }
}

public class AdminEditarUsuarioControllerDAOTest {

    private AdminEditarUsuarioController controller;
    private MainControllerSimulado mainController; 
    private AdminGerenciamentoDAOEdicaoSimulado daoSimulado;

    // Elementos FXML simulados
    private final Label headerTitle = new Label();
    private final Label userIdLabel = new Label();
    private final Label userTypeLabel = new Label();
    private final TextField nomeField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField telefoneField = new TextField();
    private final Label statusLabel = new Label();
    private final Button toggleStatusButton = new Button();

    @BeforeEach
    void setUp() throws Exception {
        this.controller = new AdminEditarUsuarioController();
        this.daoSimulado = new AdminGerenciamentoDAOEdicaoSimulado();
        
        // Injeção de FXML e DAO Simulado via Reflection
        setPrivateField(controller, "adminDAO", daoSimulado);
        setPrivateField(controller, "headerTitle", headerTitle);
        setPrivateField(controller, "userIdLabel", userIdLabel);
        setPrivateField(controller, "userTypeLabel", userTypeLabel);
        setPrivateField(controller, "nomeField", nomeField);
        setPrivateField(controller, "emailField", emailField);
        setPrivateField(controller, "telefoneField", telefoneField);
        setPrivateField(controller, "statusLabel", statusLabel);
        setPrivateField(controller, "toggleStatusButton", toggleStatusButton);
        
        this.mainController = new MainControllerSimulado();
        controller.setMainController(mainController);
    }
    
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // --- Testes de Carregamento ---
    
    @Test
    void testLoadUserDataParaProfessorAtivo() {
        controller.loadUserData("p123");

        assertEquals("EDITAR CONTA DE PROFESSOR", headerTitle.getText());
        assertEquals("Prof Ativo", nomeField.getText());
        assertEquals("Status Atual: ATIVO", statusLabel.getText());
        assertEquals("BLOQUEAR CONTA", toggleStatusButton.getText());
    }

    @Test
    void testLoadUserDataParaEstudanteBloqueado() {
        controller.loadUserData("e456");

        assertEquals("EDITAR CONTA DE ESTUDANTE", headerTitle.getText());
        assertEquals("Estudante Bloqueado", nomeField.getText());
        assertEquals("Status Atual: BLOQUEADO", statusLabel.getText());
        assertEquals("DESBLOQUEAR CONTA", toggleStatusButton.getText());
    }
    
    // --- Testes de Ação ---

    @Test
    void testHandleSalvarDeveAtualizarNomeETelefone() throws Exception {
        controller.loadUserData("p123");
        
        nomeField.setText("Novo Nome do Professor");
        telefoneField.setText("9999-8888");

        controller.handleSalvar();

        Usuario savedUser = daoSimulado.usuarioSalvo;
        assertEquals("Novo Nome do Professor", savedUser.getNome());
        assertEquals("9999-8888", savedUser.getTelefone());
    }

    @Test
    void testHandleToggleStatusDeveBloquearConta() throws Exception {
        controller.loadUserData("p123");
        
        controller.handleToggleStatus();

        // Verifica se a view foi atualizada para BLOQUEADO
        assertEquals("Status Atual: BLOQUEADO", statusLabel.getText());
        assertEquals("DESBLOQUEAR CONTA", toggleStatusButton.getText());
    }
    
    @Test
    void testHandleToggleStatusDeveDesbloquearConta() throws Exception {
        controller.loadUserData("e456"); 
        
        controller.handleToggleStatus();

        // Verifica se a view foi atualizada para ATIVO
        assertEquals("Status Atual: ATIVO", statusLabel.getText());
        assertEquals("BLOQUEAR CONTA", toggleStatusButton.getText());
    }

    @Test
    void testHandleVoltarDeveNavegar() {
        controller.handleVoltar();
        assertTrue(mainController.goToAdminGerenciarUsuariosChamado);
    }
}
