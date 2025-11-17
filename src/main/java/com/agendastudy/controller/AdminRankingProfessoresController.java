package com.agendastudy.controller;

import com.agendastudy.DAO.AdminGerenciamentoDAO;
import com.agendastudy.model.RankingProfessor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller para a tela de Ranking de Professores (SCRUM-72, 74).
 */
public class AdminRankingProfessoresController implements BaseController {

    private GerenciamentoUsuarioApp mainController;
    private final AdminGerenciamentoDAO adminDAO = new AdminGerenciamentoDAO();

    @FXML private TableView<RankingProfessor> rankingTableView;
    @FXML private TableColumn<RankingProfessor, Integer> posicaoCol;
    @FXML private TableColumn<RankingProfessor, String> nomeProfCol;
    @FXML private TableColumn<RankingProfessor, Integer> agendamentosCol;
    @FXML private DatePicker dataInicioPicker;
    @FXML private DatePicker dataFimPicker;

    @Override
    public void setMainController(GerenciamentoUsuarioApp mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        // Configura as colunas
        posicaoCol.setCellValueFactory(new PropertyValueFactory<>("posicaoRanking"));
        nomeProfCol.setCellValueFactory(new PropertyValueFactory<>("nomeProfessor"));
        agendamentosCol.setCellValueFactory(new PropertyValueFactory<>("totalAgendamentos"));
        
        // Define datas padrão (ex: último ano)
        dataFimPicker.setValue(LocalDate.now());
        dataInicioPicker.setValue(LocalDate.now().minusYears(1));
        
        handleGerarRanking();
    }

    /**
     * Gera e exibe o ranking de professores com base nas datas selecionadas (SCRUM-72, 74).
     */
    @FXML
    private void handleGerarRanking() {
        LocalDate dataInicio = dataInicioPicker.getValue();
        LocalDate dataFim = dataFimPicker.getValue();

        if (dataInicio != null && dataFim != null && dataInicio.isBefore(dataFim)) {
            List<RankingProfessor> ranking = adminDAO.gerarRankingAgendamentos(dataInicio, dataFim);
            rankingTableView.setItems(FXCollections.observableList(ranking));
        } else {
            rankingTableView.setItems(FXCollections.emptyObservableList());
            // Lógica para mostrar erro de data
        }
    }

    @FXML
    private void handleVoltar() {
        mainController.goToAdminRelatoriosDashboard();
    }
}
