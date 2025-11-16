// Pacote corrigido para se alinhar ao projeto
package com.agendastudy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Import do Avatar MANTIDO, como você pediu
import com.gluonhq.charm.glisten.control.Avatar;

// Imports dos Modelos REAIS do seu projeto
// (Estes arquivos precisam existir no seu projeto)
import com.agendastudy.model.Avaliacao;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Aula;

import java.time.LocalDateTime;

/**
 * Controller para a tela de todas as avaliações (AllReviewsScreen.fxml)
 * Gerencia a listagem e exibição de reviews dos alunos
 * (Corrigido para usar "Dummy Data" com os modelos reais do projeto)
 */
public class AllReviewsController {

    @FXML
    private VBox reviewsContainer; // Conectado ao FXML

    @FXML
    private Button backButton; // Conectado ao FXML

    // A lista agora usa o modelo REAL "Avaliacao"
    private ObservableList<Avaliacao> reviews;

    @FXML
    public void initialize() {
        // backButton.setOnAction(event -> goBack()); // O FXML já faz isso com onAction
        loadReviews();
    }

    /**
     * Carrega todas as avaliações do professor.
     * Como o backend está quebrado, usamos "Dados Falsos" (dummy data).
     */
    private void loadReviews() {
        reviews = FXCollections.observableArrayList();

        // --- DADOS FALSOS ---
        // Precisamos criar Professores, Estudantes e Aulas falsos
        // para poder criar uma Avaliacao real.
        Estudante est1 = new Estudante("E1", "Aluno Falso 1", "e1@email.com", "123");
        Estudante est2 = new Estudante("E2", "Aluno Falso 2", "e2@email.com", "123");
        Professor prof = new Professor("P1", "Professor Falso", "p1@email.com", "123");
        Aula aula1 = new Aula("A1", "Aula Falsa 1", "...", prof, est1, LocalDateTime.now());
        Aula aula2 = new Aula("A2", "Aula Falsa 2", "...", prof, est2, LocalDateTime.now());

        reviews.add(new Avaliacao(
            "AV1", 
            est1, 
            aula1, 
            5, 
            "Aula boa, professora explica o assunto de forma leve e didática."
        ));

        reviews.add(new Avaliacao(
            "AV2", 
            est2, 
            aula2, 
            4, 
            "Professor(a) domina o conteúdo e tem ótima didática."
        ));
        
        reviews.add(new Avaliacao(
            "AV3", 
            est1, 
            aula1, 
            3, 
            "Achei um pouco confuso no começo, mas depois melhorou."
        ));
        // --- FIM DOS DADOS FALSOS ---

        // Agora, renderiza os dados
        renderReviews();
    }

    /**
     * Limpa o container e renderiza todas as avaliações da lista.
     */
    private void renderReviews() {
        reviewsContainer.getChildren().clear();
        for (Avaliacao review : reviews) {
            VBox reviewCard = createReviewSection(review);
            reviewsContainer.getChildren().add(reviewCard);
        }
    }

    /**
     * Cria um "card" de avaliação (VBox) para um objeto Avaliacao.
     * (Esta é a lógica de construção de FXML em Java)
     *
     * @param review O objeto Avaliacao com os dados.
     * @return Um VBox pronto para ser adicionado à tela.
     */
    private VBox createReviewSection(Avaliacao review) {
        
        // --- Card Principal ---
        VBox reviewCard = new VBox();
        reviewCard.getStyleClass().add("review-card");

        // --- Header (Título e Estrelas) ---
        HBox reviewHeader = new HBox();
        reviewHeader.getStyleClass().add("review-header");
        
        Label titleLabel = new Label("Avaliações");
        titleLabel.getStyleClass().add("review-label");
        
        HBox ratingDisplay = new HBox();
        ratingDisplay.getStyleClass().add("rating-display");
        ratingDisplay.getProperties().put("HBox.hgrow", "ALWAYS"); // HBox.hgrow="ALWAYS"

        // Converte a nota (int) para estrelas (String)
        String stars = "★★★★★".substring(0, review.getNota());
        Label starsLabel = new Label(stars);
        starsLabel.getStyleClass().add("stars-filled");
        
        Label ratingScore = new Label(String.format("%.1f", (double)review.getNota()));
        ratingScore.getStyleClass().add("rating-score");
        
        ratingDisplay.getChildren().addAll(starsLabel, ratingScore);
        reviewHeader.getChildren().addAll(titleLabel, ratingDisplay);

        // --- Seção do Avaliador (Foto e Nome) ---
        HBox reviewerSection = new HBox();
        reviewerSection.getStyleClass().add("reviewer-section");
        
        // --- Avatar (Mantido como você pediu) ---
        Avatar avatar = new Avatar();
        avatar.setRadius(25.0); // Raio 25 como no seu FXML original
        avatar.getStyleClass().add("reviewer-avatar");
        try {
            // Carrega a imagem dummy
            avatar.setImage(new Image(getClass().getResourceAsStream("/com/agendastudy/image/Dummyphoto.jpg")));
        } catch (Exception e) {
            System.err.println("Erro ao carregar Dummyphoto.jpg: " + e.getMessage());
        }

        // --- Conteúdo (Nome e Comentário) ---
        VBox reviewContent = new VBox();
        reviewContent.getStyleClass().add("review-content");

        Label studentNameLabel = new Label(review.getEstudante().getNome());
        studentNameLabel.getStyleClass().add("reviewer-name");

        Label commentLabel = new Label(review.getComentario());
        commentLabel.getStyleClass().add("review-comment-line");
        commentLabel.setWrapText(true);
        
        reviewContent.getChildren().addAll(studentNameLabel, commentLabel);
        reviewerSection.getChildren().addAll(avatar, reviewContent);
        
        // Adiciona tudo ao card
        reviewCard.getChildren().addAll(reviewHeader, reviewerSection);

        return reviewCard;
    }

    /**
     * Volta para a tela anterior (chamado pelo onAction do FXML)
     */
    @FXML
    private void goBack() {
        System.out.println("Voltando para tela de perfil...");
        // Implementar navegação de volta (ex: App.setRoot("ProfessorProfile"))
    }
}