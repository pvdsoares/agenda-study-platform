package com.agendastudy.controller;

import com.agendastudy.DAO.AulaDAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;
import com.agendastudy.service.ServicoAgendamento; // Serviço de Agendamento
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AgendamentoController {

    // Componentes FXML
    @FXML
    private TextField tituloField;
    @FXML
    private TextArea descricaoArea;
    @FXML
    private DatePicker dataPicker;
    @FXML
    private TextField horaField;
    @FXML
    private VBox agendaContainer;
    @FXML
    private Label mensagemErroLabel;

    // Dependências e Estado
    private ServicoAgendamento servicoAgendamento;
    private Professor professorLogado; // Usuário logado

    /**
     * Inicializa o controller. Configura dependências.
     */
    @FXML
    public void initialize() {
        // 1. Inicializa Dependências
        AulaDAO aulaDAO = new AulaDAO();
        this.servicoAgendamento = new ServicoAgendamento(aulaDAO, new com.agendastudy.DAO.DisponibilidadeDAO());

        dataPicker.setValue(LocalDate.now());
    }

    public void setProfessorLogado(Professor professor) {
        this.professorLogado = professor;
        carregarAgendaProfessor();
    }

    private void carregarAgendaProfessor() {
        if (professorLogado == null)
            return;

        agendaContainer.getChildren().clear();

        try {
            List<Aula> agenda = servicoAgendamento.getAgendaDoProfessor(professorLogado);

            if (agenda.isEmpty()) {
                Label placeholder = new Label("Nenhuma aula agendada. Crie a primeira disponibilidade.");
                placeholder.setStyle("-fx-text-fill: #999999; -fx-font-style: italic; -fx-padding: 10px;");
                agendaContainer.getChildren().add(placeholder);
                return;
            }

            for (Aula aula : agenda) {
                String status = aula.getEstudante() != null ? "CONFIRMADA por: " + aula.getEstudante().getNome()
                        : "DISPONÍVEL";

                String info = String.format("• %s às %s | %s",
                        aula.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        aula.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm")),
                        status);

                Label aulaLabel = new Label(info);
                aulaLabel.setStyle(
                        "-fx-padding: 5px; -fx-background-color: #e6f0ff; -fx-background-radius: 5px; -fx-min-width: 100%;");
                agendaContainer.getChildren().add(aulaLabel);
            }
        } catch (Exception e) {
            mensagemErroLabel.setText("❌ Erro ao carregar agenda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lida com a ação do botão "VOLTAR".
     */
    @FXML
    private void handleAgendarDisponibilidade() {
        try {
            // Lógica simples de agendamento (Exemplo)
            String titulo = tituloField.getText();
            String data = (dataPicker.getValue() != null) ? dataPicker.getValue().toString() : "";
            String hora = horaField.getText();

            if (titulo.isEmpty() || data.isEmpty() || hora.isEmpty()) {
                mensagemErroLabel.setText("Preencha todos os campos obrigatórios.");
                return;
            }

            // Converter para LocalDateTime (simplificado)
            LocalDateTime dataHora = LocalDateTime.parse(data + "T" + hora + ":00");

            // Criar aula/disponibilidade e salvar via servico
            String descricao = descricaoArea.getText();
            int duracao = 60; // Duração padrão em minutos (ou pegar de um campo novo)
            Aula novaAula = servicoAgendamento.criarDisponibilidade(professorLogado, titulo, descricao, dataHora,
                    duracao);

            if (novaAula != null) {
                carregarAgendaProfessor(); // Atualiza a lista
                mensagemErroLabel.setText("Disponibilidade criada com sucesso!");
                mensagemErroLabel.setStyle("-fx-text-fill: green;");
            } else {
                mensagemErroLabel.setText("Erro ao criar disponibilidade.");
            }

        } catch (DateTimeParseException e) {
            mensagemErroLabel.setText("Formato de data/hora inválido.");
        } catch (Exception e) {
            e.printStackTrace();
            mensagemErroLabel.setText("Erro ao agendar: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/agendastudy/view/relatorio-rendimento.fxml"));
            javafx.scene.Parent root = loader.load();

            // Re-configurar o RelatorioController (Dashboard)
            RelatorioController controller = loader.getController();
            controller.setProfessorAtual(this.professorLogado);

            // Recriar o serviço para o dashboard (precisamos do mapa de aulas)
            AulaDAO aulaDAO = new AulaDAO();
            java.util.Map<Professor, List<Aula>> map = new java.util.HashMap<>();
            map.put(this.professorLogado, aulaDAO.buscarAulasDoProfessor(this.professorLogado));

            controller.setRelatorioService(new com.agendastudy.service.RelatoriodeRendimento(map));

            javafx.stage.Stage stage = (javafx.stage.Stage) agendaContainer.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
