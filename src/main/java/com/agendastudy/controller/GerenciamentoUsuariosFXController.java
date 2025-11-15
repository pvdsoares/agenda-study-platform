package com.agendastudy.controller;

import com.agendastudy.DAO.AdministradorDAO;
import com.agendastudy.model.Administrador;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;


public class GerenciamentoUsuariosFXController {

    // Usando AdministradorDAO que herda todos os métodos de UsuarioDAO
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

    // --- Inicialização ---
    @FXML
    public void initialize() {
        configurarFiltros();
        configurarTabela();
        aplicarFiltros(); 
        gerarRelatorio(); 
    }
    
    // --- Lógica de Configuração ---
    private void configurarFiltros() {
        tipoUsuarioChoiceBox.getItems().addAll("Todos", "Aluno", "Tutor", "Administrador");
        tipoUsuarioChoiceBox.setValue("Todos");
        statusUsuarioChoiceBox.getItems().addAll("Todos", "Ativo", "Inativo");
        statusUsuarioChoiceBox.setValue("Todos");
    }

    private void configurarTabela() {
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // Tipo (Aluno, Tutor, Admin)
        tipoColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(getTipoUsuarioLegivel(cellData.getValue())));

        // Status (Ativo/Inativo)
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().isAtivo() ? "Ativo" : "Inativo"));
        
        // Coluna Ações com Botão de Desativação/Ativação
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
    
    private String getTipoUsuarioLegivel(Usuario usuario) {
        if (usuario instanceof Estudante) return "Aluno";
        if (usuario instanceof Professor) return "Tutor";
        if (usuario instanceof Administrador) return "Administrador";
        return "Outro";
    }

    // --- Lógica de Filtros e Paginação ---

    @FXML private void handleAplicarFiltros() {
        paginaAtual = 1;
        aplicarFiltros();
    }
    
    @FXML private void handleLimparFiltros() {
        termoBuscaField.clear();
        tipoUsuarioChoiceBox.setValue("Todos");
        statusUsuarioChoiceBox.setValue("Todos");
        paginaAtual = 1;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String termo = termoBuscaField.getText();
        
        String tipoParaDAO = switch (tipoUsuarioChoiceBox.getValue()) {
            case "Aluno" -> "Estudante";
            case "Tutor" -> "Professor";
            case "Administrador" -> "Administrador";
            default -> null; 
        };
                             
        Boolean statusParaDAO = switch (statusUsuarioChoiceBox.getValue()) {
            case "Ativo" -> true;
            case "Inativo" -> false;
            default -> null;
        };

        // 1. Contagem total
        int totalUsuarios = administradorDAO.contarUsuariosFiltrados(termo, tipoParaDAO, statusParaDAO);
        totalPaginas = Math.max(1, (int) Math.ceil((double) totalUsuarios / tamanhoPagina));

        // 2. Ajuste de página
        if (paginaAtual > totalPaginas) paginaAtual = totalPaginas;
        if (paginaAtual < 1) paginaAtual = 1;

        // 3. Busca paginada
        List<Usuario> resultados = administradorDAO.buscarPaginada(termo, tipoParaDAO, statusParaDAO, paginaAtual, tamanhoPagina);
        listaUsuarios.setAll(resultados);

        // 4. Atualiza paginação na interface
        atualizarPaginacaoUI();
    }

    private void atualizarPaginacaoUI() {
        infoPaginacaoLabel.setText("Página " + paginaAtual + " de " + totalPaginas);
        anteriorButton.setDisable(paginaAtual <= 1);
        proximaButton.setDisable(paginaAtual >= totalPaginas);
    }
    
    @FXML private void handlePaginaAnterior() {
        if (paginaAtual > 1) {
            paginaAtual--;
            aplicarFiltros();
        }
    }

    @FXML private void handleProximaPagina() {
        if (paginaAtual < totalPaginas) {
            paginaAtual++;
            aplicarFiltros();
        }
    }
    
    // --- Lógica de Ações ---

    private void handleAlterarStatus(Usuario usuario) {
        boolean novoStatus = !usuario.isAtivo();
        if (administradorDAO.alterarStatus(usuario.getId(), novoStatus)) {
            usuario.setAtivo(novoStatus); 
            tabelaUsuarios.refresh(); 
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
}
