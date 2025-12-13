package com.agendastudy.controller;

import com.agendastudy.model.Professor;
import com.agendastudy.service.RelatoriodeRendimento;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/* @author Luara gabrielli guimarães araujo
 * @version 1.0
 * @since 2025
  */
public class RelatorioController {

    @FXML
    private TextField dataInicioField;
    @FXML
    private TextField dataFimField;
    @FXML
    private Label totalAulasLabel;
    @FXML
    private Label taxaCancelamentoTutorLabel;
    @FXML
    private Label taxaCancelamentoAlunoLabel;
    @FXML
    private VBox alunoCancelamentoList;

    private RelatoriodeRendimento relatorioService;
    private Professor professorAtual;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
        Map<String, Integer> cancelamentos = relatorioService.calcularCancelamentosPorAluno(professorAtual);

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
    private void voltarParaPerfil(javafx.event.ActionEvent event) {
        // Logout logic or return to main menu
        trocarTela("/com/agendastudy/view/Login.fxml", (Node) event.getSource());
    }

    private void trocarTela(String caminhoFXML, Node node) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao trocar de tela: " + caminhoFXML);
        }
    }

    @FXML
    private void handleNavInicio(MouseEvent event) {
        // Já estamos na tela de início (Dashboard)
        calcularEExibirRelatorio();
    }

    @FXML
    private void handleNavAlunos(MouseEvent event) {
        mostrarAlerta("Em Breve", "Funcionalidade de Alunos em desenvolvimento.");
    }

    @FXML
    private void handleNavProfessores(MouseEvent event) {
        // Talvez mostrar o perfil público do próprio professor?
        // Por enquanto, alerta.
        mostrarAlerta("Em Breve", "Funcionalidade de Professores em desenvolvimento.");
    }

    @FXML
    private void handleNavAulas(MouseEvent event) {
        // Ir para Agendamento (Gerenciar Aulas)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendastudy/view/agendamento.fxml"));
            Parent root = loader.load();

            // Configurar AgendamentoController com o professor atual
            AgendamentoController controller = loader.getController();
            controller.setProfessorLogado(this.professorAtual);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
