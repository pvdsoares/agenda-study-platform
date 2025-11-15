package com.agendastudy.controller;

import com.agendastudy.DAO.AvaliacaoDAO;
import com.agendastudy.DAO.EstudanteDAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Estudante;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color; 

/**
 * Controller responsável pelo modal de avaliação de aula.
 * Controla clique nas estrelas, comentário opcional e envio da avaliação.
 */
public class AvaliacaoModalController {

    // --- Ícones das estrelas ---
    private Image starEmpty;
    private Image starFilled;

    // --- Dados necessários para registrar a avaliação ---
    private Aula aula;
    private Estudante estudante;

    private final EstudanteDAO estudanteDAO = new EstudanteDAO(new AvaliacaoDAO());

    private int notaSelecionada = 0;

    // --- Componentes FXML ---
    @FXML private ImageView star1;
    @FXML private ImageView star2;
    @FXML private ImageView star3;
    @FXML private ImageView star4;
    @FXML private ImageView star5;

    @FXML private TextArea fieldComentario;
    @FXML private Button btnEnviar;
    @FXML private ImageView btnVoltar;

    /**
     * Carrega as imagens das estrelas e configura os eventos de clique.
     */
    @FXML
    private void initialize() {

        starEmpty = new Image(getClass().getResourceAsStream("/com/agendastudy/image/star_empty.png"));
        starFilled = new Image(getClass().getResourceAsStream("/com/agendastudy/image/star_filled.png"));

        // Garante que o botão use o estilo DESATIVADO inicial com as dimensões corretas
        btnEnviar.setStyle(
                "-fx-background-color: #8C857E;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: semi-bold;" +
                        "-fx-background-radius: 50;" +
                        "-fx-cursor: hand;"
        );

        configurarCliqueEstrela(star1, 1);
        configurarCliqueEstrela(star2, 2);
        configurarCliqueEstrela(star3, 3);
        configurarCliqueEstrela(star4, 4);
        configurarCliqueEstrela(star5, 5);

        btnVoltar.setOnMouseClicked(e -> fechar());
    }

    /**
     * Configura cada estrela para responder ao clique.
     */
    private void configurarCliqueEstrela(ImageView star, int valor) {
        star.setOnMouseClicked(e -> {
            notaSelecionada = valor;
            atualizarEstrelas();
            btnEnviar.setDisable(false);

            // Define o estilo ATIVADO com as dimensões finais
            btnEnviar.setStyle(
                    "-fx-background-color: #52B371;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 24px;" +
                            "-fx-font-weight: semi-bold;" +
                            "-fx-background-radius: 50;" +
                            "-fx-cursor: hand;"
            );
        });
    }

    /**
     * Atualiza visualmente as estrelas (cheias/vazias).
     */
    private void atualizarEstrelas() {
        ImageView[] estrelas = {star1, star2, star3, star4, star5};

        for (int i = 0; i < estrelas.length; i++) {
            estrelas[i].setImage((i < notaSelecionada) ? starFilled : starEmpty);
        }
    }

    /**
     * Recebe dados antes de abrir o modal.
     */
    public void configurarDados(Estudante estudante, Aula aula) {
        this.estudante = estudante;
        this.aula = aula;
    }

    /**
     * Envia a avaliação usando EstudanteDAO.
     */
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
            abrirPopupConfirmacao();
            fechar(); // fecha o modal de avaliação
        } else {
            mostrarAlerta("Erro", "Não foi possível registrar a avaliação.");
        }
    }

    /**
     * Abre o pop up confirmando que a avaliação da aula foi realizada com sucesso.
     */
    private void abrirPopupConfirmacao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/agendastudy/view/confirmacaoAvaliacao.fxml"
            ));

            Parent root = loader.load();

            ConfirmacaoAvaliacaoController controller = loader.getController();
            controller.setMensagem("Obrigado pela sua Avaliação!");

            Stage popup = new Stage();
            Scene scene = new Scene(root);

            // Garante que a Scene seja transparente
            scene.setFill(Color.TRANSPARENT);

            //  Remove a decoração da janela (barra de título)
            popup.initStyle(StageStyle.TRANSPARENT);

            popup.setScene(scene);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setResizable(false);
            popup.showAndWait();  // espera fechar antes de continuar

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Fecha o modal atual.
     */
    private void fechar() {
        Stage stage = (Stage) btnEnviar.getScene().getWindow();
        stage.close();
    }

    /**
     * Alerta simples reutilizável.
     */
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}