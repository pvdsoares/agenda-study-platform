package com.agendastudy.controller;

import com.agendastudy.DAO.AvaliacaoDAO;
import com.agendastudy.DAO.EstudanteDAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Avaliacao;
import com.agendastudy.model.Estudante;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Controller da tela "Suas Aulas", onde mostra as aulas agendadas do estudante,
 * aulas concluídas para serem avaliadas e o histórico de aulas feitas.
 *
 * @author MATHEUS PEREIRA RODRIGUES
 * @version 3.0
 * @since 2025-11-16
 */
public class TelaSuasAulasController {

    @FXML private Label btnVoltar;

    private Estudante estudante;
    private final EstudanteDAO estudanteDAO = new EstudanteDAO(new AvaliacaoDAO());

    @FXML private VBox cardAulaPrincipal;
    @FXML private HBox containerRestantes;
    @FXML private Label labelStatusAula;

    @FXML private VBox containerAulasConcluidas;

    @FXML private VBox containerHistorico;
    @FXML private VBox vboxHistorico;

    @FXML private Label btnHome;
    @FXML private Label btnSuasAulas;
    @FXML private Label btnBuscarAulas;
    @FXML private Label btnPerfil;


    /**
     * Configura o estudante atual e recarrega todas as seções da tela.
     *
     * @param estudante Estudante logado
     */
    public void configurarEstudante(Estudante estudante) {
        this.estudante = estudante;

        carregarAulasAgendadas();
        carregarAulasConcluidas();
        carregarHistorico();
    }

    /**
     * Evento do botão de voltar.
     * Fecha a janela atual.
     *
     * @param event clique do mouse
     */
    @FXML
    private void voltar(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // SEÇÃO: AULAS AGENDADAS
    /**
     * Carrega e exibe as aulas agendadas (card principal + cards restantes).
     */
    private void carregarAulasAgendadas() {
        cardAulaPrincipal.getChildren().clear();
        containerRestantes.getChildren().clear();

        List<Aula> aulas = estudanteDAO.getAulasAgendadas(estudante);

        if (aulas.isEmpty()) {
            Label nenhum = new Label("Nenhuma aula agendada");
            nenhum.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            cardAulaPrincipal.getChildren().add(nenhum);
            return;
        }

        Aula aulaPrincipal = aulas.get(0);
        cardAulaPrincipal.getChildren().add(criarCardAulaPrincipal(aulaPrincipal));
        labelStatusAula.setText(aulaPrincipal.getStatus().name());

        if (aulas.size() > 1) {
            aulas.subList(1, aulas.size())
                    .forEach(a -> containerRestantes.getChildren().add(criarCardAulaMenor(a)));
        }
    }

    /**
     * Cria o card principal da aula mais próxima.
     *
     * @param aula Aula exibida
     * @return VBox com o card
     */
    private VBox criarCardAulaPrincipal(Aula aula) {
        VBox card = new VBox();
        card.setPrefSize(322, 163);
        card.setSpacing(8);
        card.setStyle("-fx-background-color: #9395FF; -fx-background-radius: 20; -fx-padding: 15;");

        Label titulo = new Label(aula.getTitulo());
        titulo.setStyle("-fx-font-size: 26px; -fx-font-weight: semibold;");

        Label professor = new Label(aula.getProfessor().getNome());
        professor.setStyle("-fx-font-size: 16px;");

        Label data = new Label(aula.getDataHora().toLocalDate().toString());
        data.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Button btnDetalhes = new Button("⋮");
        btnDetalhes.setStyle("-fx-background-color: transparent; -fx-font-size: 18px;");
        btnDetalhes.setOnAction(e -> abrirDetalhesAula(aula));

        HBox top = new HBox(btnDetalhes);
        top.setAlignment(Pos.TOP_RIGHT);

        card.getChildren().addAll(top, titulo, professor, data);
        return card;
    }

    /**
     * Cria card menor para outras aulas agendadas.
     *
     * @param aula Aula
     * @return VBox representando o card
     */
    private VBox criarCardAulaMenor(Aula aula) {
        VBox card = new VBox();
        card.setPrefSize(100.5, 64);
        card.setSpacing(4);

        card.setStyle("""
                -fx-background-color: #D9D9D9;
                -fx-border-color: #306BB3;
                -fx-border-width: 1;
                -fx-padding: 5;
                """);

        Label titulo = new Label(aula.getTitulo());
        titulo.setStyle("-fx-font-size: 12px;");

        Label data = new Label(aula.getDataHora().toLocalDate().toString());
        data.setStyle("-fx-font-size: 12px;");

        Button btnDetalhes = new Button("⋮");
        btnDetalhes.setStyle("-fx-background-color: transparent;");
        btnDetalhes.setOnAction(e -> abrirDetalhesAula(aula));

        card.setOnMouseClicked(e -> abrirDetalhesAula(aula));

        card.getChildren().addAll(btnDetalhes, titulo, data);
        return card;
    }

    /**
     * Abre detalhes da aula selecionada.
     *
     * @param aula Aula clicada
     */
    private void abrirDetalhesAula(Aula aula) {
        System.out.println("Abrir detalhes da aula: " + aula.getTitulo());
    }

    // SEÇÃO: A AVALIAR
    /**
     * Carrega cards de aulas já concluídas mas ainda não avaliadas.
     * Exibe card especial caso não existam.
     */
    private void carregarAulasConcluidas() {
        containerAulasConcluidas.getChildren().clear();

        List<Aula> aulas = estudanteDAO.getAulasConcluidas(estudante);

        boolean temPendentes = false;

        for (Aula aula : aulas) {
            if (!estudanteDAO.jaAvaliouAula(estudante, aula)) {
                temPendentes = true;
                containerAulasConcluidas.getChildren().add(criarCardAvaliacao(aula));
            }
        }

        if (!temPendentes) {
            containerAulasConcluidas.getChildren().add(criarCardSemPendentes());
        }
    }

    /**
     * Cria card verde para avaliar aula.
     *
     * @param aula Aula pendente
     * @return VBox com card
     */
    private VBox criarCardAvaliacao(Aula aula) {
        VBox card = new VBox(10);
        card.setPrefSize(311, 160);
        card.setStyle("-fx-background-color: #52B371; -fx-background-radius: 25; -fx-padding: 15;");

        Label titulo = new Label("Avalie sua última aula!");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label nomeAula = new Label(aula.getTitulo());
        nomeAula.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Label prof = new Label(aula.getProfessor().getNome());
        prof.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Button btn = new Button();
        btn.setPrefSize(176, 45);
        btn.setStyle("-fx-background-color: #0F3B71; -fx-background-radius: 50;");

        Label txt = new Label("Avaliar agora");
        txt.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: semibold;");

        Label star = new Label("★");
        star.setStyle("-fx-font-size: 24px; -fx-text-fill: #FFDF00;");

        HBox box = new HBox(10, txt, star);
        box.setAlignment(Pos.CENTER);
        btn.setGraphic(box);

        btn.setOnAction(e -> abrirModalAvaliacao(aula));

        card.getChildren().addAll(titulo, nomeAula, prof, btn);
        return card;
    }

    /**
     * Card exibido quando não há aulas pendentes de avaliação.
     *
     * @return VBox card
     */
    private VBox criarCardSemPendentes() {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(311, 160);
        card.setStyle("-fx-background-color: #52B371; -fx-background-radius: 25; -fx-padding: 15;");

        Label titulo = new Label("Nenhuma avaliação pendente.");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label msg = new Label("Bom trabalho!");
        msg.setStyle("-fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: white;");

        card.getChildren().addAll(titulo, msg);
        return card;
    }

    /**
     * Abre modal de avaliação.
     *
     * @param aula Aula
     */
    private void abrirModalAvaliacao(Aula aula) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/view/AvaliacaoModal.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            AvaliacaoModalController c = loader.getController();
            c.configurarDados(estudante, aula);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarAulasConcluidas();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // SEÇÃO: HISTÓRICO
    /**
     * Carrega os 3 cards mais recentes de aulas concluídas.
     */
    private void carregarHistorico() {
        containerHistorico.getChildren().clear();

        List<Aula> aulas = estudanteDAO.getAulasConcluidas(estudante)
                .stream()
                .sorted(Comparator.comparing(Aula::getDataHora).reversed())
                .limit(3)
                .toList();

        aulas.forEach(a -> containerHistorico.getChildren().add(criarCardHistorico(a)));
    }

    /**
     * Cria card azul do histórico.
     *
     * @param aula Aula concluída
     * @return HBox card
     */
    private HBox criarCardHistorico(Aula aula) {
        HBox card = new HBox(10);
        card.setPrefSize(313, 112);
        card.setStyle("-fx-background-color: #306BB3; -fx-background-radius: 15; -fx-padding: 15;");

        VBox info = new VBox(5);

        Label titulo = new Label(aula.getTitulo());
        titulo.setStyle("-fx-font-size: 16px;");

        Label prof = new Label(aula.getProfessor().getNome());
        prof.setStyle("-fx-font-size: 16px;");

        Label dataHora = new Label(aula.getDataHora()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dataHora.setStyle("-fx-font-size: 16px;");

        info.getChildren().addAll(titulo, prof, dataHora);

        Button btn = new Button("Ver Resumo");
        btn.setPrefSize(72, 64);
        btn.setStyle("-fx-background-color: #CEFADA; -fx-font-weight: bold;");
        btn.setOnAction(e -> abrirResumoAula(aula));

        HBox.setHgrow(info, Priority.ALWAYS);
        card.getChildren().addAll(info, btn);

        return card;
    }

    /**
     * Abre o resumo da aula.
     *
     * @param aula Aula
     */
    private void abrirResumoAula(Aula aula) {
        System.out.println("Abrir resumo: " + aula.getTitulo());
    }


    /**
     * Abre a tela de Menu.
     */
    @FXML
    private void abrirHome() {
        trocarTela("/view/TelaMenu.fxml");
    }

    /**
     * Recarrega a tela atual (Suas Aulas).
     */
    @FXML
    private void abrirSuasAulas() {
        trocarTela("/view/TelaSuasAulas.fxml");
    }

    /**
     * Vai para a tela de busca de aulas.
     */
    @FXML
    private void abrirBuscarAulas() {
        trocarTela("/view/TelaBuscar.fxml");
    }

    /**
     * Vai para o perfil do estudante.
     */
    @FXML
    private void abrirPerfil() {
        trocarTela("/view/TelaPerfil.fxml");
    }

    /**
     * Troca a cena atual pela FXML informada.
     *
     * @param caminho caminho do arquivo FXML desejado
     */
    private void trocarTela(String caminho) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Parent root = loader.load();

            Stage stage = (Stage) vboxHistorico.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
