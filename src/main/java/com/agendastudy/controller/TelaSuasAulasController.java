package com.agendastudy.controller;

import com.agendastudy.DAO.AvaliacaoDAO;
import com.agendastudy.DAO.EstudanteDAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Estudante;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller para a tela "Suas Aulas", onde o estudante pode visualizar suas aulas concluídas
 * e avaliar aulas pendentes.
 *
 * @author MATHEUS PEREIRA RODRIGUES
 * @version 2.1
 * @since 2025-11-16
 */
public class TelaSuasAulasController {

    @FXML
    private VBox containerAulasConcluidas;

    private Estudante estudante;
    private final EstudanteDAO estudanteDAO = new EstudanteDAO(new AvaliacaoDAO());


    //     ==== SEÇÃO AULAS AULAS AGENDADAS ======



    @FXML
    private VBox containerAulasAgendadas; // VBox que vai receber os cards de aulas agendadas

    /**
     * Carrega as aulas agendadas do estudante e cria os cards dinamicamente.
     * Caso não haja aulas agendadas, exibe uma mensagem de "nenhuma aula agendada".
     */
    private void carregarAulasAgendadas() {
        containerAulasAgendadas.getChildren().clear();

        List<Aula> aulasAgendadas = estudanteDAO.getAulasAgendadas(estudante); // supondo que estudanteDAO tenha esse método

        if (aulasAgendadas.isEmpty()) {
            VBox cardVazio = criarCardSemAulasAgendadas();
            containerAulasAgendadas.getChildren().add(cardVazio);
            return;
        }

        for (Aula aula : aulasAgendadas) {
            VBox card = criarCardAulaAgendada(aula);
            containerAulasAgendadas.getChildren().add(card);
        }
    }

    /**
     * Cria um card para cada aula agendada.
     *
     * @param aula Aula a ser exibida
     * @return VBox representando o card
     */
    private VBox criarCardAulaAgendada(Aula aula) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setPrefSize(311, 160);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #3B7BD9; -fx-background-radius: 25; -fx-padding: 15;"); // cor de exemplo

        Label titulo = new Label("Aula Agendada");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label nomeAula = new Label(aula.getTitulo());
        nomeAula.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: white;");

        Label professor = new Label(aula.getProfessor().getNome());
        professor.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: white;");

        // Botão de detalhes ou entrar na aula
        Button btnDetalhes = new Button("Ver Detalhes");
        btnDetalhes.setPrefSize(176, 45);
        btnDetalhes.setStyle("-fx-background-color: #0F3B71; -fx-background-radius: 50; -fx-font-size: 16px; -fx-font-weight: semibold; -fx-text-fill: white;");

        btnDetalhes.setOnAction(e -> abrirDetalhesAula(aula));

        card.getChildren().addAll(titulo, nomeAula, professor, btnDetalhes);
        return card;
    }

    /**
     * Cria um card exibido quando não há aulas agendadas.
     *
     * @return VBox representando o card de "nenhuma aula agendada"
     */
    private VBox criarCardSemAulasAgendadas() {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setPrefSize(311, 160);
        card.setAlignment(Pos.CENTER); // centraliza tudo
        card.setStyle("-fx-background-color: #3B7BD9; -fx-background-radius: 25; -fx-padding: 15;");

        Label titulo = new Label("Nenhuma aula agendada.");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label mensagem = new Label("Aproveite para se planejar!");
        mensagem.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: white;");

        card.getChildren().addAll(titulo, mensagem);
        return card;
    }

    /**
     * Método chamado ao clicar em "Ver Detalhes" de uma aula agendada.
     *
     * @param aula Aula selecionada
     */
    private void abrirDetalhesAula(Aula aula) {
        // Aqui você pode abrir um modal ou outra tela com informações da aula
        System.out.println("Abrir detalhes da aula: " + aula.getTitulo());
    }



    //     ==== SEÇÃO AULAS CONCLUÍDAS ======

    // ----- SUB-SECÇÃO AUALAS A AVALIAR -------

    /**
     * Configura o estudante logado e carrega as aulas concluídas.
     *
     * @param estudante Estudante logado
     */
    public void configurarEstudante(Estudante estudante) {
        this.estudante = estudante;
        carregarAulasConcluidas();
    }

    /**
     * Carrega as aulas concluídas do estudante e cria os cards dinamicamente.
     * Se houver avaliações pendentes, cria os cards de avaliação.
     * Caso contrário, exibe card de "nenhuma avaliação pendente".
     */
    private void carregarAulasConcluidas() {
        containerAulasConcluidas.getChildren().clear();

        List<Aula> aulasConcluidas = estudanteDAO.getAulasConcluidas(estudante);

        boolean temPendentes = false;

        for (Aula aula : aulasConcluidas) {
            if (!estudanteDAO.jaAvaliouAula(estudante, aula)) {
                VBox card = criarCardAvaliacao(aula);
                containerAulasConcluidas.getChildren().add(card);
                temPendentes = true;
            }
        }

        if (!temPendentes) {
            VBox cardSemPendentes = criarCardSemPendentes();
            containerAulasConcluidas.getChildren().add(cardSemPendentes);
        }
    }

    /**
     * Cria um card de avaliação para uma aula pendente.
     *
     * @param aula Aula a ser avaliada
     * @return VBox representando o card
     */
    private VBox criarCardAvaliacao(Aula aula) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setPrefSize(311, 160);
        card.setAlignment(Pos.CENTER_LEFT); // centraliza vertical e horizontalmente os elementos
        card.setStyle("-fx-background-color: #52B371; -fx-background-radius: 25; -fx-padding: 15;");

        Label titulo = new Label("Avalie sua última aula!");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        titulo.setAlignment(Pos.CENTER_LEFT);

        Label nomeAula = new Label(aula.getTitulo());
        nomeAula.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: white;");

        Label professor = new Label(aula.getProfessor().getNome());
        professor.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: white;");

        // Botão "Avaliar agora" com estrela alinhada no centro
        Button btnAvaliar = new Button();
        btnAvaliar.setPrefSize(176, 45);
        btnAvaliar.setStyle("-fx-background-color: #0F3B71; -fx-background-radius: 50; -fx-font-size: 16px; -fx-font-weight: semibold;");

        Label textoBotao = new Label("Avaliar agora");
        textoBotao.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: semibold;");

        Label estrela = new Label("★");
        estrela.setStyle("-fx-font-size: 24px; -fx-text-fill: #FFDF00;");

        HBox hBoxBotao = new HBox(10, textoBotao, estrela);
        hBoxBotao.setAlignment(Pos.CENTER);
        btnAvaliar.setGraphic(hBoxBotao);

        btnAvaliar.setOnAction(e -> abrirModalAvaliacao(aula));

        card.getChildren().addAll(titulo, nomeAula, professor, btnAvaliar);
        return card;
    }

    /**
     * Cria o card exibido quando não há avaliações pendentes.
     *
     * @return VBox representando o card de "nenhuma avaliação pendente"
     */
    private VBox criarCardSemPendentes() {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setPrefSize(311, 160);
        card.setAlignment(Pos.CENTER); // centraliza tudo
        card.setStyle("-fx-background-color: #52B371; -fx-background-radius: 25; -fx-padding: 15;");

        Label titulo = new Label("Nenhuma avaliação pendente.");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label mensagem = new Label("Bom trabalho!");
        mensagem.setStyle("-fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: white;");

        card.getChildren().addAll(titulo, mensagem);
        return card;
    }

    /**
     * Abre o modal de avaliação da aula.
     *
     * @param aula Aula a ser avaliada
     */
    private void abrirModalAvaliacao(Aula aula) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/view/AvaliacaoModal.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            AvaliacaoModalController controller = loader.getController();
            controller.configurarDados(estudante, aula);

            stage.showAndWait();

            // Recarrega os cards após fechar o modal
            carregarAulasConcluidas();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // ----- SUB-SECÇÃO HISTÓRICO -------

}
