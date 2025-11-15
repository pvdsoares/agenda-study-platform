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
        Image check = new Image(getClass()
                .getResourceAsStream("/com/agendastudy/image/check_icon.png"));
        iconCheck.setImage(check);
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
