package com.agendastud.controller;

import com.agendastud.model.Professor;
import com.agendastud.service.BuscaService;
import com.agendastud.service.CriteriosBusca;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class BuscaController {

    @FXML private TextField txtBuscaLivre;
    @FXML private VBox resultsContainer; // Contêiner onde os cards dos professores serão adicionados

    private final BuscaService buscaService;
    private CriteriosBusca criteriosAtuais = new CriteriosBusca();

    // Lista inicial (simulação de dados)
    private final List<Professor> professoresIniciais = List.of(
            new Professor("Ana Souza", "Matemática", 80.0, "Centro", 2.5, 4.8),
            new Professor("Beto Alves", "Física", 120.0, "Bairro A", 5.0, 4.2),
            new Professor("Carla Lima", "História", 55.0, "Bairro B", 0.8, 4.9)
    );

    // Construtor (o JavaFX pode precisar do construtor padrão ou de um inicializador)
    public BuscaController() {
        this.buscaService = new BuscaService(professoresIniciais);
    }

    @FXML
    public void initialize() {
        // Inicializa com uma busca vazia ou recomendada
        realizarBusca(criteriosAtuais);

        // Listener para a busca livre (ao pressionar Enter)
        txtBuscaLivre.setOnAction(event -> handleBuscaLivre());
    }

    // --- Ações de Filtro e Busca ---

    // 1. Ação para a busca livre (texto da barra)
    @FXML
    protected void handleBuscaLivre() {
        criteriosAtuais.textoBuscaLivre = txtBuscaLivre.getText();
        realizarBusca(criteriosAtuais);
    }

    // 2. Ação para abrir a Tela de Filtros (Ícone do funil)
    @FXML
    protected void abrirTelaFiltros() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/view/TelaFiltros.fxml"));
        Parent root = loader.load();

        com.agendastud.controller.FiltrosController controller = loader.getController();

        // Cria a nova janela (modal)
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // Bloqueia a tela principal
        stage.setTitle("Aplicar Filtros");
        controller.setStage(stage);

        // Define o callback que será chamado ao aplicar os filtros
        controller.setOnFiltrosAplicados(novosCriterios -> {
            this.criteriosAtuais = novosCriterios;
            realizarBusca(this.criteriosAtuais); // Refaz a busca com os novos critérios
        });

        stage.showAndWait();
    }

    // 3. Método principal que chama o serviço e atualiza a tela
    private void realizarBusca(CriteriosBusca criterios) {
        // Chamada à sua lógica de negócio pronta!
        List<Professor> resultados = buscaService.filtrarProfessores(criterios);

        // Por padrão, ordena por melhor avaliação para a Tela 4
        ordenarPorAvaliacao(resultados);

        exibirResultados(resultados);
    }

    // --- Métodos de Ordenação (para os botões "Melhores Avaliados", etc.) ---

    @FXML
    protected void ordenarMelhoresAvaliados() {
        List<Professor> resultados = buscaService.filtrarProfessores(criteriosAtuais);
        ordenarPorAvaliacao(resultados);
        exibirResultados(resultados);
    }

    private void ordenarPorAvaliacao(List<Professor> lista) {
        // Ordena pela avaliação (maior primeiro)
        lista.sort(Comparator.comparing(Professor::getAvaliacao).reversed());
    }

    // (Adicione aqui os métodos para ordenar 'Mais Baratos', 'Perto de Mim', etc.)

    // --- Exibição de Resultados (Tela 4) ---

    private void exibirResultados(List<Professor> professores) {
        resultsContainer.getChildren().clear(); // Limpa resultados anteriores

        if (professores.isEmpty()) {
            // Exibir mensagem de "Nenhum resultado encontrado"
            return;
        }

        // Simplesmente cria e adiciona um Label por professor (substituir por um card FXML real)
        for (Professor p : professores) {
            String info = String.format("%s (%s) - R$%.2f/h - Avaliação: %.1f",
                    p.getNome(), p.getDisciplina(), p.getPrecoHora(), p.getAvaliacao());

            // Aqui você deve carregar o "card" do professor (como na Tela 4)
            // Exemplo de como ficaria o card visualmente (usando um Label):
            javafx.scene.control.Label card = new javafx.scene.control.Label(info);
            card.setStyle("-fx-background-color: #2c3e50; -fx-padding: 15px; -fx-text-fill: white; -fx-max-width: infinity;");

            resultsContainer.getChildren().add(card);
        }
    }
}