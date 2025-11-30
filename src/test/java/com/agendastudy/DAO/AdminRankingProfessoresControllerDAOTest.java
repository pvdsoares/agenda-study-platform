package com.agendastudy.controller;

import com.agendastudy.model.RankingProfessor;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Simulação da DAO para ranking
class AdminGerenciamentoDAORankingSimulado extends AdminGerenciamentoDAOSimulado {
    
    public AdminGerenciamentoDAORankingSimulado() { super(0, 0, 0); }
    
    @Override
    public List<RankingProfessor> gerarRankingAgendamentos(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio != null && dataFim != null && dataInicio.isBefore(dataFim)) {
            // Retorna dados simulados se as datas forem válidas
            return Arrays.asList(
                new RankingProfessor(1, "Prof Top", 100),
                new RankingProfessor(2, "Prof Med", 50)
            );
        }
        return new ArrayList<>();
    }
}

public class AdminRankingProfessoresControllerDAOTest {

    private AdminRankingProfessoresController controller;
    private MainControllerSimulado mainController; 
    private AdminGerenciamentoDAORankingSimulado daoSimulado;

    // Elementos FXML simulados
    private final DatePicker dataInicioPicker = new DatePicker();
    private final DatePicker dataFimPicker = new DatePicker();
    private final TableView<RankingProfessor> rankingTableView = new TableView<>();

    @BeforeEach
    void setUp() throws Exception {
        this.controller = new AdminRankingProfessoresController();
        this.daoSimulado = new AdminGerenciamentoDAORankingSimulado();
        
        // Injeção de FXML e DAO Simulado via Reflection
        setPrivateField(controller, "adminDAO", daoSimulado);
        setPrivateField(controller, "dataInicioPicker", dataInicioPicker);
        setPrivateField(controller, "dataFimPicker", dataFimPicker);
        setPrivateField(controller, "rankingTableView", rankingTableView);

        this.mainController = new MainControllerSimulado();
        controller.setMainController(mainController);
        
        controller.initialize();
    }
    
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testInitializeDeveDefinirDatasPadraoECarregarRanking() {
        // Assert: Datas padrão definidas (aproximadamente último ano)
        LocalDate hoje = LocalDate.now();
        assertEquals(hoje, dataFimPicker.getValue());
        assertEquals(hoje.minusYears(1), dataInicioPicker.getValue());
        
        // Assert: Ranking inicial carregado
        assertEquals(2, rankingTableView.getItems().size());
        assertEquals("Prof Top", rankingTableView.getItems().get(0).getNomeProfessor());
    }

    @Test
    void testHandleGerarRankingComDatasValidas() {
        // Arrange
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);
        LocalDate dataFim = LocalDate.of(2024, 6, 30);
        dataInicioPicker.setValue(dataInicio);
        dataFimPicker.setValue(dataFim);

        rankingTableView.getItems().clear();

        // Act
        controller.handleGerarRanking();

        // Assert
        assertEquals(2, rankingTableView.getItems().size());
    }

    @Test
    void testHandleGerarRankingComDatasInvalidasDeveLimparTabela() {
        // Arrange: Data de início após a data de fim
        LocalDate dataInicio = LocalDate.of(2024, 12, 31);
        LocalDate dataFim = LocalDate.of(2024, 1, 1);
        dataInicioPicker.setValue(dataInicio);
        dataFimPicker.setValue(dataFim);

        // Act
        controller.handleGerarRanking();

        // Assert
        assertTrue(rankingTableView.getItems().isEmpty());
    }

    @Test
    void testHandleVoltarDeveNavegar() {
        controller.handleVoltar();
        assertTrue(mainController.goToAdminRelatoriosDashboardChamado);
    }
}
