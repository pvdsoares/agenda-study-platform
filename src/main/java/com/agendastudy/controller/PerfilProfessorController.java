package com.agendastudy.controller;

// (Imports do JavaFX)
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import com.agendastudy.model.Professor;

import java.io.IOException;

/**
 * Controller para a tela de Perfil do Professor (ProfessorProfile.fxml).
 * USA DADOS FALSOS (DUMMY DATA) ENQUANTO O BACKEND ESTIVER QUEBRADO.
 *
 * @author Alexandro Costa Santos
 * @version 1.0
 * @since 2025-11-15
 */
public class PerfilProfessorController {

    // --- Componentes FXML (Baseados nos fx:id) ---
    @FXML
    private Button btnVoltar;
    @FXML
    private ImageView imgProfessor;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelSobre1;
    @FXML
    private Label labelSobre2;
    @FXML
    private Label labelDisciplinas;
    @FXML
    private Label labelPreco;
    @FXML
    private Label labelLocalizacao;
    @FXML
    private Button btnVerHorario;

    // --- Componentes da SUA TAREFA (0.txt) ---
    @FXML
    private Label labelMediaEstrelas;
    @FXML
    private Label labelMediaNum;
    @FXML
    private Circle imgAluno; // (Está como Circle no FXML)
    @FXML
    private Label labelAlunoNome;
    @FXML
    private Label labelAlunoComentario;
    @FXML
    private Button btnVerTodas;

    private Professor professorAtual;

    /**
     * Método de inicialização.
     * Como o backend não compila, usamos dados falsos (dummy data)
     * para preencher a tela.
     */
    @FXML
    public void initialize() {
        // --- DADOS FALSOS (para teste visual) ---

        // Tenta carregar a imagem de professor (se falhar, ignora)
        try {
            Image profImg = new Image(getClass().getResourceAsStream("/com/agendastudy/image/professor.png"));
            imgProfessor.setImage(profImg);
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem 'professor.png': " + e.getMessage());
        }

        // Preenche os labels com dados falsos
        if (professorAtual == null) {
            labelNome.setText("Prof. (Falso) da Silva");
        }

        labelSobre1.setText("Formado em Ciência da Computação (Falso).");
        labelSobre2.setText("10 anos de experiência.");
        labelDisciplinas.setText("Estrutura de Dados, JavaFX");
        labelPreco.setText("R$ 50,00 - R$ 100,00");
        labelLocalizacao.setText("Vitória da Conquista, BA, 45000-000");

        // Preenche a parte da SUA TAREFA com dados falsos
        labelMediaEstrelas.setText("★★★★☆");
        labelMediaNum.setText("4,7");
        labelAlunoNome.setText("Aluno Falso");
        labelAlunoComentario.setText("Aula muito boa, explicação direta ao ponto!");
        // --- Fim dos Dados Falsos ---
    }

    public void setProfessor(Professor professor) {
        this.professorAtual = professor;
        if (this.professorAtual != null) {
            labelNome.setText(professor.getNome());
            // Aqui você poderia preencher outros campos reais se o model tivesse
        }
    }

    /**
     * Manipulador do clique no botão "Ver dia e horário".
     */
    @FXML
    private void handleVerHorario() {
        System.out.println("Botão 'Ver Horário' clicado. (Lógica de navegação iria aqui)");
    }

    /**
     * Manipulador do clique no botão "Ver todas avaliações".
     * (Parte da sua tarefa 0.txt)
     */
    @FXML
    private void handleVerTodasAvaliacoes() {
        System.out.println("Botão 'Ver Todas Avaliações' clicado.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/view/AllReviewsScreen.fxml"));
            Parent root = loader.load();

            // Obter o controller da tela de reviews para passar o professor
            AllReviewsController controller = loader.getController();
            controller.setProfessor(this.professorAtual);

            Stage stage = new Stage();
            stage.setTitle("Todas as Avaliações");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao abrir tela de avaliações: " + e.getMessage());
        }
    }

    /**
     * Manipulador do clique no botão "Voltar".
     */
    @FXML
    private void handleVoltar(javafx.event.ActionEvent event) {
        trocarTela("/com/agendastudy/view/Login.fxml", (Node) event.getSource());
    }

    // --- Handlers da Barra de Navegação ---
    @FXML
    private void handleNavInicio(MouseEvent event) {
        // Redireciona para o Dashboard do Professor (Relatório)
        trocarTela("/com/agendastudy/view/relatorio-rendimento.fxml", (Node) event.getSource());
    }

    @FXML
    private void handleNavAulas(MouseEvent event) {
        trocarTela("/com/agendastudy/view/agendamento.fxml", (Node) event.getSource());
    }

    @FXML
    private void handleNavBuscar(MouseEvent event) {
        System.out.println("Nav: Buscar (Atual)");
    }

    @FXML
    private void handleNavPerfil(MouseEvent event) {
        // Já estamos no perfil
    }

    private void trocarTela(String caminhoFXML, Node node) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();

            // Se estiver indo para Relatório ou Agendamento, precisamos passar o professor
            if (caminhoFXML.contains("relatorio-rendimento.fxml")) {
                RelatorioController controller = loader.getController();
                controller.setProfessorAtual(this.professorAtual);

                // Inicializar Serviço para o relatório (Simulado ou Real)
                com.agendastudy.DAO.AulaDAO aulaDAO = new com.agendastudy.DAO.AulaDAO();
                java.util.List<com.agendastudy.model.Aula> aulas = aulaDAO.buscarAulasDoProfessor(this.professorAtual);
                java.util.Map<Professor, java.util.List<com.agendastudy.model.Aula>> map = new java.util.HashMap<>();
                map.put(this.professorAtual, aulas);
                controller.setRelatorioService(new com.agendastudy.service.RelatoriodeRendimento(map));
                controller.initialize();

            } else if (caminhoFXML.contains("agendamento.fxml")) {
                AgendamentoController controller = loader.getController();
                controller.setProfessorLogado(this.professorAtual);
            }

            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao navegar para: " + caminhoFXML);
        }
    }
}