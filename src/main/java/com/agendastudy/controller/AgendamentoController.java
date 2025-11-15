package com.agendastudy.controller;

import com.agendastudy.DAO.AulaDAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;
import com.agendastudy.service.ServicoAgendamento; // Serviço de Agendamento
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AgendamentoController {

    // Componentes FXML
    @FXML private TextField tituloField;
    @FXML private TextArea descricaoArea;
    @FXML private DatePicker dataPicker;
    @FXML private TextField horaField; 
    @FXML private VBox agendaContainer;
    @FXML private Label mensagemErroLabel;

    // Dependências e Estado
    private ServicoAgendamento servicoAgendamento;
    private Professor professorLogado; // Usuário logado

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); 

    /**
     * Inicializa o controller. Configura dependências e carrega dados iniciais.
     */
    @FXML
    public void initialize() {
        // 1. Inicializa Dependências
        AulaDAO aulaDAO = new AulaDAO(); 
        this.servicoAgendamento = new ServicoAgendamento(aulaDAO);
        
        // 2. Simulação do Professor Logado (MOCK) - Substitua pelo usuário autenticado
        this.professorLogado = new Professor("USER_PROF_001", "Dr. Lógica", "logica@study.com", "senha123"); 
        
        dataPicker.setValue(LocalDate.now());

        carregarAgendaProfessor();
    }

    /**
     * Carrega a agenda atual do professor (Disponibilidades e Aulas Confirmadas).
     */
    private void carregarAgendaProfessor() {
        if (professorLogado == null) return;
        
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
                String status = aula.getEstudante() != null ? 
                               "CONFIRMADA por: " + aula.getEstudante().getNome() : 
                               "DISPONÍVEL";
                
                String info = String.format("• %s às %s | %s", 
                                            aula.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                            aula.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm")),
                                            status);
                
                Label aulaLabel = new Label(info);
                aulaLabel.setStyle("-fx-padding: 5px; -fx-background-color: #e6f0ff; -fx-background-radius: 5px; -fx-min-width: 100%;");
                agendaContainer.getChildren().add(aulaLabel);
            }
        } catch (Exception e) {
            mensagemErroLabel.setText("❌ Erro ao carregar agenda: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Lida com a ação do botão "AGENDAR DISPONIBILIDADE".
     * Cria a disponibilidade de aula, validando o horário.
     */
    @FXML
    private void handleAgendarDisponibilidade() {
        mensagemErroLabel.setText("");

        String titulo = tituloField.getText();
        String descricao = descricaoArea.getText();
        LocalDate data = dataPicker.getValue();
        String horaTexto = horaField.getText();

        if (titulo.trim().isEmpty() || data == null || horaTexto.trim().isEmpty()) {
            mensagemErroLabel.setText("Preencha todos os campos obrigatórios.");
            return;
        }

        try {
            LocalTime hora = LocalTime.parse(horaTexto.trim(), timeFormatter);
            LocalDateTime dataHora = LocalDateTime.of(data, hora);

            // Chamada ao Serviço (Implementar interface de agendamento de aula)
            Aula novaAula = servicoAgendamento.criarDisponibilidade(
                professorLogado, 
                titulo, 
                descricao, 
                dataHora
            );

            mensagemErroLabel.setText("✅ Disponibilidade agendada com sucesso! ID: " + novaAula.getIdAula());
            
            tituloField.clear();
            descricaoArea.clear();
            horaField.clear();
            
            carregarAgendaProfessor(); // Atualiza a agenda

        } catch (DateTimeParseException e) {
            mensagemErroLabel.setText("❌ Formato de horário inválido. Use HH:MM (Ex: 14:30).");
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Conflito de horário ou data inválida (Validar disponibilidade de horário)
            mensagemErroLabel.setText("❌ Erro: " + e.getMessage());
        } catch (Exception e) {
            mensagemErroLabel.setText("❌ Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lida com a ação do botão "VOLTAR".
     */
    @FXML
    private void handleCancelar() {
        System.out.println("Voltando para a tela anterior.");
    }
}
