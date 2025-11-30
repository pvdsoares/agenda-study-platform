package com.agendastudy.controller;

import com.agendastudy.DAO.AdminGerenciamentoDAO;
import com.agendastudy.model.UsuarioAdminView;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Simulação da DAO para filtros
class AdminGerenciamentoDAOFiltroSimulado extends AdminGerenciamentoDAOSimulado {
    private final List<UsuarioAdminView> listaSimulada = new ArrayList<>();
    
    public AdminGerenciamentoDAOFiltroSimulado() {
        super(0, 0, 0); 
        listaSimulada.add(new UsuarioAdminView("1", "User Init", "Estudante", "Ativo"));
    }
    
    @Override
    public List<UsuarioAdminView> buscarUsuariosComFiltro(String termoBusca, String tipoFiltro, String statusFiltro) {
        if (termoBusca.contains("Maria") && tipoFiltro.equals("Estudante") && statusFiltro.equals("Ativo")) {
            return Arrays.asList(new UsuarioAdminView("2", "Maria da Silva", "Estudante", "Ativo"));
        }
        if (tipoFiltro.equals("Professor") && statusFiltro.equals("Bloqueado")) {
            return Arrays.asList(new UsuarioAdminView("3", "Prof. Bloqueado", "Professor", "Bloqueado"));
        }
        // Retorna a lista padrão se nenhum filtro específico for ativado
        return Arrays.asList(new UsuarioAdminView("4", "Outro User", "Estudante", "Ativo"));
    }
}

public class AdminGerenciarUsuariosControllerDAOTest {

    private AdminGerenciarUsuariosController controller;
    private MainControllerSimulado mainController; 

    // Elementos FXML simulados
    private final TextField searchField = new TextField();
    private final ChoiceBox<String> tipoChoiceBox = new ChoiceBox<>();
    private final ChoiceBox<String> statusChoiceBox = new ChoiceBox<>();
    private final ListView<UsuarioAdminView> usuariosListView = new ListView<>();

    @BeforeEach
    void setUp() throws Exception {
        this.controller = new AdminGerenciarUsuariosController();
        
        // Injeção de FXML e DAO Simulado via Reflection
        setPrivateField(controller, "searchField", searchField);
        setPrivateField(controller, "tipoChoiceBox", tipoChoiceBox);
        setPrivateField(controller, "statusChoiceBox", statusChoiceBox);
        setPrivateField(controller, "usuariosListView", usuariosListView);
        setPrivateField(controller, "adminDAO", new AdminGerenciamentoDAOFiltroSimulado());

        // Inicialização dos ChoiceBoxes antes do initialize()
        tipoChoiceBox.setItems(FXCollections.observableArrayList("Todos", "Estudante", "Professor"));
        statusChoiceBox.setItems(FXCollections.observableArrayList("Todos", "Ativo", "Bloqueado"));
        
        controller.initialize(); // Chamado para carregar os usuários iniciais
        
        this.mainController = new MainControllerSimulado();
        controller.setMainController(mainController);
    }
    
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testInitializeDeveDefinirFiltrosPadraoEChamarLoad() {
        // Assert: Filtros padrão definidos
        assertEquals("Todos", tipoChoiceBox.getValue());
        assertEquals("Todos", statusChoiceBox.getValue());
        // Assert: Usuários iniciais carregados (Lista padrão do DAO Simulado)
        assertEquals(1, usuariosListView.getItems().size());
    }

    @Test
    void testHandleSearchDeveFiltrarPorTermoETipo() {
        // Arrange
        searchField.setText("Maria");
        tipoChoiceBox.setValue("Estudante");
        statusChoiceBox.setValue("Ativo");

        // Act
        controller.handleSearch();

        // Assert
        assertEquals(1, usuariosListView.getItems().size());
        assertEquals("Maria da Silva", usuariosListView.getItems().get(0).getNome());
    }

    @Test
    void testHandleFilterDeveFiltrarPorStatus() {
        // Arrange
        searchField.setText("");
        tipoChoiceBox.setValue("Professor");
        statusChoiceBox.setValue("Bloqueado");
        
        // Act
        controller.handleFilter();

        // Assert
        assertEquals(1, usuariosListView.getItems().size());
        assertEquals("Prof. Bloqueado", usuariosListView.getItems().get(0).getNome());
    }

    @Test
    void testHandleVoltarDeveNavegar() {
        controller.handleVoltar();
        assertTrue(mainController.goToAdminDashboardChamado);
    }
}
