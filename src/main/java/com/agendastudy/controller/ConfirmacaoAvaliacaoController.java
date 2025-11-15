package com.agendastudy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controller para o pop-up de confirmação após enviar avaliação.
 */
public class ConfirmacaoAvaliacaoController {

    @FXML private Label labelMensagem;
    @FXML private ImageView iconCheck;

    /**
     * Inicializa o popup carregando o ícone.
     */
    @FXML
    private void initialize() {
        try {
            String imagePath = "/com/agendastudy/image/check_icon.png";

            Image image = new Image(getClass().getResourceAsStream(imagePath));

            iconCheck.setImage(image);

        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem: " + e.getMessage());
        }
    }

    /**
     * Permite definir o texto mostrado no popup.
     */
    public void setMensagem(String mensagem) {
        labelMensagem.setText(mensagem);
    }

    /**
     * Fecha apenas o popup.
     */
    @FXML
    private void fecharPopup() {
        Stage stage = (Stage) labelMensagem.getScene().getWindow();
        stage.close();
    }
}
