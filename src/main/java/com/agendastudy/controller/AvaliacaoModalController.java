package com.agendastudy.controller;

import com.agendastudy.DAO.AvaliacaoDAO;
import com.agendastudy.DAO.EstudanteDAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Estudante;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * @author MATHEUS PEREIRA RODRIGUES
 */
public class AvaliacaoModalController {

    private Aula aula;
    private Estudante estudante;

    private final EstudanteDAO estudanteDAO = new EstudanteDAO(new AvaliacaoDAO());

    private int notaSelecionada = 0;

    @FXML private Label star1;
    @FXML private Label star2;
    @FXML private Label star3;
    @FXML private Label star4;
    @FXML private Label star5;

    @FXML private TextArea fieldComentario;
    @FXML private Button btnEnviar;

    @FXML private Label btnVoltar;

    @FXML
    private void initialize() {

        configurarClique(star1, 1);
        configurarClique(star2, 2);
        configurarClique(star3, 3);
        configurarClique(star4, 4);
        configurarClique(star5, 5);

        btnVoltar.setOnMouseClicked(e -> fechar());
    }

    /**
     * Método chamado ao clicar no botão de voltar.
     * Fecha a tela atual.
     *
     * @param event Evento do clique do mouse
     */
    @FXML
    private void voltar(MouseEvent event) {
        // Fecha a janela atual
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void configurarClique(Label estrela, int valor) {
        estrela.setOnMouseClicked(e -> {
            notaSelecionada = valor;
            atualizarEstrelas();
            btnEnviar.setDisable(false);
            btnEnviar.setStyle("-fx-background-color: #52B371;");
        });
    }

    private void atualizarEstrelas() {
        Label[] estrelas = {star1, star2, star3, star4, star5};

        for (int i = 0; i < estrelas.length; i++) {
            if (i < notaSelecionada) {
                estrelas[i].getStyleClass().remove("estrela");
                if (!estrelas[i].getStyleClass().contains("estrela-selecionada"))
                    estrelas[i].getStyleClass().add("estrela-selecionada");
            } else {
                estrelas[i].getStyleClass().remove("estrela-selecionada");
                if (!estrelas[i].getStyleClass().contains("estrela"))
                    estrelas[i].getStyleClass().add("estrela");
            }
        }
    }

    public void configurarDados(Estudante estudante, Aula aula) {
        this.estudante = estudante;
        this.aula = aula;
    }

    @FXML
    private void enviarAvaliacao() {

        if (notaSelecionada == 0) {
            mostrarAlerta("Atenção", "Selecione pelo menos 1 estrela.");
            return;
        }

        boolean sucesso = estudanteDAO.avaliarAula(
                estudante,
                aula,
                notaSelecionada,
                fieldComentario.getText().trim()
        );

        if (sucesso) {
            fechar();
        } else {
            mostrarAlerta("Erro", "Não foi possível registrar a avaliação.");
        }
    }

    private void fechar() {
        Stage stage = (Stage) btnEnviar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
