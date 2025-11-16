package com.agendastudy.controller;

import com.agendastudy.model.Professor;
import com.agendastudy.service.RelatoriodeRendimento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Controller para a visualização de Relatórios de Rendimento.
 * @author LUARA GABRIELLI
 * @version 1.1
 * @since 2025-11-14
 */
public class RelatorioController {

    @FXML private TextField dataInicioField;
    @FXML private TextField dataFimField;
    @FXML private Label totalAulasLabel;
    @FXML private Label taxaCancelamentoTutorLabel;
    @FXML private Label taxaCancelamentoAlunoLabel;
    @FXML private VBox alunoCancelamentoList;

    private RelatoriodeRendimento relatorioService;
    private Professor professorAtual;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @FXML
    public void initialize() {
        // Opcional: mensagens iniciais
        totalAulasLabel.setText("—");
        taxaCancelamentoTutorLabel.setText("—");
        taxaCancelamentoAlunoLabel.setText("—");
    }

    // Usado por outro controller para configurar dependências
    public void setRelatorioService(RelatoriodeRendimento service) {
        this.relatorioService = service;
    }

    public void setProfessorAtual(Professor professor) {
        this.professorAtual = professor;
    }

    /**
     * Converte "dd/MM/yyyy" para LocalDateTime.
     */
    private LocalDateTime parseDate(String dateText, boolean fimDoDia) {
        try {
            String fullText = fimDoDia ?
                    dateText + " 23:59:59" :
                    dateText + " 00:00:00";

            return LocalDateTime.parse(fullText, DATE_FORMAT);
        } catch (Exception e) {
            System.err.println("Data inválida: " + dateText);
            return null;
        }
    }

    /**
     * Método principal acionado pelo botão "Gerar Relatório".
     */
    @FXML
    public void calcularEExibirRelatorio() {

        // Valida dependências
        if (relatorioService == null || professorAtual == null) {
            System.err.println("Erro: Service ou Professor não configurados.");
            return;
        }

        // Converte datas
        LocalDateTime inicio = parseDate(dataInicioField.getText(), false);
        LocalDateTime fim = parseDate(dataFimField.getText(), true);

        if (inicio == null || fim == null) {
            System.err.println("Datas inválidas.");
            return;
        }

        if (fim.isBefore(inicio)) {
            System.err.println("Data final não pode ser menor que a inicial.");
            return;
        }

        // -----------------------------
        //  MÉTODOS DO RELATORIO SERVICE
        // -----------------------------

        int totalAulas = relatorioService.calcularTotalAulas(professorAtual, inicio, fim);

        double taxaTutor = relatorioService.calcularTaxaCancelamentoProfessor(professorAtual);

        double taxaAluno = relatorioService.calcularTaxaCancelamentoAluno(professorAtual);

        Map<String, Integer> cancelamentosPorAluno =
                relatorioService.calcularCancelamentosPorAluno(professorAtual);

        // -------------------------
        //  ATUALIZAÇÃO DA INTERFACE
        // -------------------------

        totalAulasLabel.setText(String.valueOf(totalAulas));
        taxaCancelamentoTutorLabel.setText(String.format("%.1f%%", taxaTutor));
        taxaCancelamentoAlunoLabel.setText(String.format("%.1f%%", taxaAluno));

        // Limpa lista antiga
        alunoCancelamentoList.getChildren().clear();

        // Preenche dinamicamente
        for (Map.Entry<String, Integer> entry : cancelamentosPorAluno.entrySet()) {
            Label item = new Label(entry.getKey() + ": " + entry.getValue() + " cancelamento(s)");
            item.setStyle("-fx-font-size: 14px; -fx-padding: 4 0 4 0;");
            alunoCancelamentoList.getChildren().add(item);
        }
    }

    @FXML
    private void handleVoltar() {
        System.out.println("Navegar para tela anterior...");
        // Implemente a lógica de troca de cena aqui.
    }
}