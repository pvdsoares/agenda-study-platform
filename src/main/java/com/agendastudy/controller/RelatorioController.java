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
        totalAulasLabel.setText("—");
        taxaCancelamentoTutorLabel.setText("—");
        taxaCancelamentoAlunoLabel.setText("—");
    }

    public void setRelatorioService(RelatoriodeRendimento service) {
        this.relatorioService = service;
    }

    public void setProfessorAtual(Professor professor) {
        this.professorAtual = professor;
    }

    private LocalDateTime parseDate(String dateText, boolean fimDoDia) {
        try {
            String full = fimDoDia ? dateText + " 23:59:59" : dateText + " 00:00:00";
            return LocalDateTime.parse(full, DATE_FORMAT);
        } catch (Exception e) {
            System.err.println("Data inválida: " + dateText);
            return null;
        }
    }

    @FXML
    public void calcularEExibirRelatorio() {

        if (relatorioService == null || professorAtual == null) {
            System.err.println("Service ou Professor não definidos.");
            return;
        }

        LocalDateTime inicio = parseDate(dataInicioField.getText(), false);
        LocalDateTime fim = parseDate(dataFimField.getText(), true);

        if (inicio == null || fim == null) {
            System.err.println("Erro nas datas.");
            return;
        }

        if (fim.isBefore(inicio)) {
            System.err.println("Data final menor que inicial.");
            return;
        }

        int totalAulas = relatorioService.calcularTotalAulas(professorAtual, inicio, fim);
        double taxaTutor = relatorioService.calcularTaxaCancelamentoProfessor(professorAtual);
        double taxaAluno = relatorioService.calcularTaxaCancelamentoAluno(professorAtual);
        Map<String,Integer> cancelamentos = relatorioService.calcularCancelamentosPorAluno(professorAtual);

        totalAulasLabel.setText(String.valueOf(totalAulas));
        taxaCancelamentoTutorLabel.setText(String.format("%.1f%%", taxaTutor));
        taxaCancelamentoAlunoLabel.setText(String.format("%.1f%%", taxaAluno));

        alunoCancelamentoList.getChildren().clear();

        cancelamentos.forEach((aluno, qtd) -> {
            Label item = new Label(aluno + ": " + qtd + " cancelamento(s)");
            item.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-padding: 3;");
            alunoCancelamentoList.getChildren().add(item);
        });
    }

    @FXML
    private void handleVoltar() {
        System.out.println("Voltar para tela anterior...");
        // Aqui você troca a cena
    }
}
