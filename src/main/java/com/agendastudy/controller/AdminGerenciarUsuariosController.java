package com.agendastudy.controller;

import com.agendastudy.DAO.AdminGerenciamentoDAO;
import com.agendastudy.model.UsuarioAdminView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * Controller para a tela de Gerenciamento de Usuários (SCRUM-113).
 */
public class AdminGerenciarUsuariosController implements BaseController {

    private GerenciamentoUsuarioApp mainController;
    private final AdminGerenciamentoDAO adminDAO = new AdminGerenciamentoDAO();

    @FXML private TextField searchField;
    @FXML private ChoiceBox<String> tipoChoiceBox;
    @FXML private ChoiceBox<String> statusChoiceBox;
    @FXML private ListView<UsuarioAdminView> usuariosListView;

    @Override
    public void setMainController(GerenciamentoUsuarioApp mainController) {
        this.mainController = mainController;
    }
    
    @FXML
    public void initialize() {
        tipoChoiceBox.setItems(FXCollections.observableArrayList("Todos", "Estudante", "Professor"));
        tipoChoiceBox.setValue("Todos");
        
        statusChoiceBox.setItems(FXCollections.observableArrayList("Todos", "Ativo", "Bloqueado"));
        statusChoiceBox.setValue("Todos");
        
        loadUsuarios();
        
        // Permite abrir a tela de edição com duplo clique (SCRUM-111)
        usuariosListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleEditUser(event);
            }
        });
    }

    /**
     * Carrega a lista de usuários aplicando os filtros atuais (SCRUM-113).
     */
    private void loadUsuarios() {
        String termoBusca = searchField.getText();
        String tipoFiltro = tipoChoiceBox.getValue();
        String statusFiltro = statusChoiceBox.getValue();
        
        List<UsuarioAdminView> lista = adminDAO.buscarUsuariosComFiltro(termoBusca, tipoFiltro, statusFiltro);
        usuariosListView.setItems(FXCollections.observableList(lista));
    }
    
    @FXML
    private void handleSearch() {
        loadUsuarios();
    }
    
    @FXML
    private void handleFilter() {
        loadUsuarios();
    }
    
    /**
     * Navega para a tela de edição de usuário, passando o ID (SCRUM-111).
     */
    private void handleEditUser(MouseEvent event) {
        UsuarioAdminView selectedUser = usuariosListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null && mainController != null) {
            mainController.navigateWithData("/com/agendastudy/view/admin_editar_usuario.fxml", selectedUser.getId()); 
        }
    }

    @FXML
    private void handleVoltar() {
        mainController.goToAdminDashboard();
    }
}
