package com.agendastudy.controller;

import com.agendastudy.service.CriteriosBusca;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FiltrosController {

    @FXML
    private CheckBox cbMatematica;
    @FXML
    private CheckBox cbPortugues;
    @FXML
    private CheckBox cbQuimica;
    @FXML
    private CheckBox cbHistoria;

    @FXML
    private TextField txtPrecoMin;
    @FXML
    private TextField txtPrecoMax;
    @FXML
    private TextField txtDistanciaMax;

    private CriteriosBusca criterios = new CriteriosBusca();
    private Stage stage; // Para fechar a janela

    // Callback para enviar os critérios de volta ao Controller principal
    private Consumer<CriteriosBusca> onFiltrosAplicados;

    public void setOnFiltrosAplicados(Consumer<CriteriosBusca> callback) {
        this.onFiltrosAplicados = callback;
    }

    // Método chamado ao clicar em "Aplicar filtros"
    @FXML
    protected void aplicarFiltros() {
        // 1. Coleta Disciplinas
        List<String> disciplinas = new ArrayList<>();
        if (cbMatematica.isSelected())
            disciplinas.add("Matemática");
        if (cbPortugues.isSelected())
            disciplinas.add("Português");
        if (cbQuimica.isSelected())
            disciplinas.add("Química");
        if (cbHistoria.isSelected())
            disciplinas.add("História");
        criterios.disciplinasSelecionadas = disciplinas;

        // 2. Coleta Preço e trata conversão
        try {
            criterios.precoMin = parseDouble(txtPrecoMin.getText());
            criterios.precoMax = parseDouble(txtPrecoMax.getText());
        } catch (NumberFormatException e) {
            // Tratar erro (ex: exibir alerta ao usuário)
        }

        // 3. Coleta Distância
        try {
            criterios.distanciaMaxKm = parseDouble(txtDistanciaMax.getText());
        } catch (NumberFormatException e) {
            // Tratar erro
        }

        // 4. Envia os critérios para o Controller principal
        if (onFiltrosAplicados != null) {
            onFiltrosAplicados.accept(criterios);
        }

        // 5. Fecha a tela de filtros
        if (stage != null) {
            stage.close();
        }
    }

    // Método auxiliar para converter texto para Double
    private Double parseDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        // Substituir vírgula por ponto para conversão Double
        return Double.parseDouble(text.replace(",", "."));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}