package com.agendastudy.controller;

// (Imports do JavaFX)
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

// (Imports do seu modelo - COMENTADOS pois o backend não compila)
// import com.agendastudy.DAO.ProfessorDAO;
// import com.agendastudy.model.Professor;

/**
 * Controller para a tela de Perfil do Professor (ProfessorProfile.fxml).
 *
 * @author Alexandro Costa Santos
 * @version 1.0
 * @since 2025-11-15
 */
public class PerfilProfessorController {

    // --- Componentes FXML (Baseados nos fx:id) ---
    @FXML private Button btnVoltar;
    @FXML private ImageView imgProfessor;
    @FXML private Label labelNome;
    @FXML private Label labelSobre1;
    @FXML private Label labelSobre2;
    @FXML private Label labelDisciplinas;
    @FXML private Label labelPreco;
    @FXML private Label labelLocalizacao;
    @FXML private Button btnVerHorario;
    
    // --- Componentes da SUA TAREFA (0.txt) ---
    @FXML private Label labelMediaEstrelas;
    @FXML private Label labelMediaNum;
    @FXML private Circle imgAluno; // (Está como Circle no FXML)
    @FXML private Label labelAlunoNome;
    @FXML private Label labelAlunoComentario;
    @FXML private Button btnVerTodas;
    
    // (O DAO real não pode ser usado pois o build está quebrado)
    // private ProfessorDAO professorDAO;
    // private Professor professorAtual;

    /**
     * Método de inicialização.
     * Como o backend não compila, usamos dados falsos (dummy data)
     * para preencher a tela.
     */
    @FXML
    public void initialize() {
        // this.professorDAO = new ProfessorDAO();
        // this.professorAtual = professorDAO.buscarPorId("P1"); // Lógica real
        
        // --- DADOS FALSOS (para teste visual) ---
        
        // Tenta carregar a imagem de professor (se falhar, ignora)
        try {
            Image profImg = new Image(getClass().getResourceAsStream("/com/agendastudy/image/professor.png"));
            imgProfessor.setImage(profImg);
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem 'professor.png': " + e.getMessage());
        }

        // Preenche os labels com dados falsos
        labelNome.setText("Prof. (Falso) da Silva");
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

    /**
     * Manipulador do clique no botão "Ver dia e horário".
     */
    @FXML
    private void handleVerHorario() {
        System.out.println("Botão 'Ver Horário' clicado. (Lógica de navegação iria aqui)");
    }

    /**
     * Manipulador do clique no botão "Ver todas avaliações".
     */
    @FXML
    private void handleVerTodasAvaliacoes() {
        System.out.println("Botão 'Ver Todas Avaliações' clicado.");
        // TODO: Adicionar lógica para carregar a tela 'lista-avaliacoes.fxml'
    }

    /**
     * Manipulador do clique no botão "Voltar".
     */
    @FXML
    private void handleVoltar() {
        System.out.println("Botão 'Voltar' clicado.");
    }

    // --- Handlers da Barra de Navegação ---
    @FXML private void handleNavInicio(MouseEvent event) {
        System.out.println("Nav: Início");
    }
    @FXML private void handleNavAulas(MouseEvent event) {
        System.out.println("Nav: Suas Aulas");
    }
    @FXML private void handleNavBuscar(MouseEvent event) {
        System.out.println("Nav: Buscar (Atual)");
    }
    @FXML private void handleNavPerfil(MouseEvent event) {
        System.out.println("Nav: Perfil");
    }
}