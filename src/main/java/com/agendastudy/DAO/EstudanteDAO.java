package com.agendastudy.DAO;

import com.agendastudy.model.Estudante;
import com.agendastudy.model.Aula;
import com.agendastudy.service.ServicoAgendamento; // Import do serviço
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.agendastudy.model.StatusAula;

/**
 * Gerencia as operações de acesso a dados (DAO) e ações para a entidade
 * Estudante.
 * Delega a lógica de negócio complexa para os serviços apropriados.
 *
 * @author VINICIUS ALVES RIBEIRO SILVA
 * @version 1.0
 * @since 2025-11-11
 */
public class EstudanteDAO extends UsuarioDAO {

    private final AvaliacaoDAO avaliacaoDAO;
    private final AulaDAO aulaDAO;

    /**
     * Construtor para injeção de dependência.
     */
    public EstudanteDAO(AvaliacaoDAO avaliacaoDAO, AulaDAO aulaDAO) {
        super(); // Chama o construtor de UsuarioDAO
        this.avaliacaoDAO = avaliacaoDAO;
        this.aulaDAO = aulaDAO;
    }

    /**
     * Retorna todas as aulas concluídas por um determinado estudante.
     *
     * @param estudante O estudante cujas aulas concluídas serão retornadas.
     * @return Lista de aulas com status CONCLUIDA.
     */
    public List<Aula> getAulasConcluidas(Estudante estudante) {
        return aulaDAO.buscarTodasAulas().stream()
                .filter(a -> estudante.equals(a.getEstudante()) && a.getStatus() == StatusAula.CONCLUIDA)
                .collect(Collectors.toList());
    }

    /**
     * Retorna todas as aulas agendadas por um determinado estudante.
     *
     * @param estudante O estudante cujas aulas agendadas serão retornadas.
     * @return Lista de aulas com status AGENDADA.
     */
    public List<Aula> getAulasAgendadas(Estudante estudante) {
        return aulaDAO.buscarTodasAulas().stream()
                .filter(a -> estudante.equals(a.getEstudante()) && a.getStatus() == StatusAula.AGENDADA)
                .collect(Collectors.toList());
    }

}
