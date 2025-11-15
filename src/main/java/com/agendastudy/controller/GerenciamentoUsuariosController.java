package com.agendastudy.controller;

import com.agendastudy.DAO.AdministradorDAO;
import com.agendastudy.DAO.UsuarioDAO;
import com.agendastudy.model.Usuario;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Administrador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Map;

public class GerenciamentoUsuariosFXController {

    // Instancia o DAO (Em produção, usaria injeção de dependência)
    private final UsuarioDAO usuarioDAO = new UsuarioDAO() {}; 
    private final AdministradorDAO administradorDAO = new AdministradorDAO();

    // --- Componentes FXML injetados ---
    @FXML private TextField termoBuscaField;
    @FXML private ChoiceBox<String> tipoUsuarioChoiceBox;
    @FXML private ChoiceBox<String> statusUsuarioChoiceBox;
    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, String> nomeColumn;
    @FXML private TableColumn<Usuario, String> emailColumn;
    @FXML private TableColumn<Usuario, String> tipoColumn;
    @FXML private TableColumn<Usuario, String> statusColumn;
    @FXML private TableColumn<Usuario, Void> acoesColumn;
    @FXML private Label infoPaginacaoLabel;
    @FXML private Button anteriorButton;
    @FXML private Button proximaButton;
    @FXML private VBox relatorioContainer;

    // --- Variáveis de Paginação e Dados ---
    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private int paginaAtual = 1;
    private final int tamanhoPagina = 10;
    private int totalPaginas = 1;

    // --- Inicialização (Chamado após o FXML ser carregado) ---
    @FXML
    public void initialize() {
        configurarFiltros();
        configurarTabela();
        aplicarFiltros(); // Carrega a lista inicial
        gerarRelatorio(); // Gera e exibe o relatório de contagem
    }
    
    // --- Lógica de Inicialização ---
    private void configurarFiltros() {
        // Opções de Tipo
        tipoUsuarioChoiceBox.getItems().addAll("Todos", "Aluno", "Tutor", "Administrador");
        tipoUsuarioChoiceBox.setValue("Todos");

        // Opções de Status
        statusUsuarioChoiceBox.getItems().addAll("Todos", "Ativo", "Inativo");
        statusUsuarioChoiceBox.setValue("Todos");
    }

    private void configurarTabela() {
        // Liga as colunas aos getters da classe Usuario
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // Coluna Tipo (Customizada)
        tipoColumn.setCellValueFactory(cellData -> {
            String tipo = getTipoUsuarioLegivel(cellData.getValue());
            return new javafx.beans.property.SimpleStringProperty(tipo);
        });

        // Coluna Status (Customizada)
        statusColumn.setCellValueFactory(cellData -> {
            String status = cellData.getValue().isAtivo() ? "Ativo" : "Inativo";
            return new javafx.beans.property.SimpleStringProperty(status);
        });
        
        // Coluna Ações com Botão (Customizada)
        acoesColumn.setCellFactory(col -> new TableCell<Usuario, Void>() {
            private final Button btn = new Button();
            {
                btn.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    handleAlterarStatus(usuario);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Usuario usuario = getTableRow().getItem();
                    // Define texto e classe CSS do botão
                    boolean ativo = usuario.isAtivo();
                    btn.setText(ativo ? "Desativar" : "Ativar");
                    btn.getStyleClass().clear();
                    btn.getStyleClass().addAll("btn", "btn-alterar-status", ativo ? "btn-desativar" : "btn-ativar");
                    setGraphic(btn);
                }
            }
        });

        tabelaUsuarios.setItems(listaUsuarios);
    }

    // --- Lógica de Filtros e Paginação ---

    @FXML
    private void handleAplicarFiltros() {
        paginaAtual = 1;
        aplicarFiltros();
    }
    
    @FXML
    private void handleLimparFiltros() {
        termoBuscaField.clear();
        tipoUsuarioChoiceBox.setValue("Todos");
        statusUsuarioChoiceBox.setValue("Todos");
        paginaAtual = 1;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String termo = termoBuscaField.getText();
        
        // Mapeia o valor da ChoiceBox para o formato esperado pelo DAO
        String tipo = tipoUsuarioChoiceBox.getValue();
        String tipoParaDAO = tipo.equals("Todos") ? null : 
                             tipo.equals("Aluno") ? "Estudante" : 
                             tipo.equals("Tutor") ? "Professor" : tipo;
                             
        Boolean statusParaDAO = null;
        if (statusUsuarioChoiceBox.getValue().equals("Ativo")) {
            statusParaDAO = true;
        } else if (statusUsuarioChoiceBox.getValue().equals("Inativo")) {
            statusParaDAO = false;
        }

        // 1. Contagem total
        int totalUsuarios = usuarioDAO.contarUsuariosFiltrados(termo, tipoParaDAO, statusParaDAO);
        totalPaginas = (int) Math.ceil((double) totalUsuarios / tamanhoPagina);
        if (totalPaginas == 0) totalPaginas = 1;

        // 2. Ajuste de página
        if (paginaAtual > totalPaginas) paginaAtual = totalPaginas;
        if (paginaAtual < 1) paginaAtual = 1;

        // 3. Busca paginada
        List<Usuario> resultados = usuarioDAO.buscarPaginada(termo, tipoParaDAO, statusParaDAO, paginaAtual, tamanhoPagina);
        listaUsuarios.setAll(resultados);

        // 4. Atualiza paginação na interface
        atualizarPaginacaoUI();
    }

    private void atualizarPaginacaoUI() {
        infoPaginacaoLabel.setText("Página " + paginaAtual + " de " + totalPaginas);
        anteriorButton.setDisable(paginaAtual == 1);
        proximaButton.setDisable(paginaAtual == totalPaginas);
    }
    
    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 1) {
            paginaAtual--;
            aplicarFiltros();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas) {
            paginaAtual++;
            aplicarFiltros();
        }
    }
    
    // --- Lógica de Ações ---

    private void handleAlterarStatus(Usuario usuario) {
        boolean novoStatus = !usuario.isAtivo();
        if (usuarioDAO.alterarStatus(usuario.getId(), novoStatus)) {
            // Atualiza o modelo na tabela e força um refresh na linha para atualizar o botão/status
            usuario.setAtivo(novoStatus); 
            tabelaUsuarios.refresh(); 
            // Você pode adicionar uma notificação Toast ou Alert aqui
        }
    }
    
    // --- Lógica de Relatório ---

    private void gerarRelatorio() {
        Map<String, Integer> relatorio = administradorDAO.gerarRelatorioContagem();

        relatorioContainer.getChildren().clear();
        
        Label titulo = new Label("Relatório de Usuários Cadastrados");
        titulo.getStyleClass().add("relatorio-titulo");
        relatorioContainer.getChildren().add(titulo);

        for (Map.Entry<String, Integer> entry : relatorio.entrySet()) {
            HBox item = new HBox(10);
            Label chave = new Label(entry.getKey() + ":");
            chave.getStyleClass().add("relatorio-chave");
            Label valor = new Label(entry.getValue().toString());
            valor.getStyleClass().add("relatorio-valor");
            item.getChildren().addAll(chave, valor);
            relatorioContainer.getChildren().add(item);
        }
    }

    // --- Métodos Auxiliares ---
    
    private String getTipoUsuarioLegivel(Usuario usuario) {
        if (usuario instanceof Estudante) {
            return "Aluno";
        } else if (usuario instanceof Professor) {
            return "Tutor";
        } else if (usuario instanceof Administrador) {
            return "Administrador";
        }
        return "Outro";
    }
}
