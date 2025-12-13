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

    /**
     * Referência para o DAO de Avaliação.
     */
    private AvaliacaoDAO avaliacao;

    /**
     * Referência para o DAO de Avaliação.
     */
    private AulaDAO aulaDAO;

    /**
     * Construtor padrão da EstudanteDAO.
     *
     * Inicializa a classe EstudanteDAO e AulaDAO criando uma nova instância interna
     * de AvaliacaoDAO para gerenciar as operações de avaliação e de AulaDAO para
     * gerenciar
     * as operaões de aula.
     */
    public EstudanteDAO() {
        this.avaliacao = new AvaliacaoDAO();
        this.aulaDAO = new AulaDAO();
    }

    /**
     * Construtor com parâmetro.
     *
     * Permite que a instância de AvaliacaoDAO seja fornecida externamente.
     *
     * @param avaliacao A instância de AvaliacaoDAO a ser utilizada.
     */
    public EstudanteDAO(AvaliacaoDAO avaliacao) {
        this.avaliacao = avaliacao;
        this.aulaDAO = new AulaDAO();
    }

    /**
     * Método de ação para o estudante agendar uma aula (confirmar a reserva).
     * Delega a lógica para o ServicoAgendamento.
     * 
     * @param idAula             O ID da aula (disponibilidade) a ser reservada.
     * @param estudante          O estudante que está agendando.
     * @param servicoAgendamento O serviço que processará o agendamento.
     * @return A Aula atualizada com o estudante.-
     * 
     */
    public Aula agendarAula(String idAula, Estudante estudante, ServicoAgendamento servicoAgendamento) {
        return servicoAgendamento.agendarAula(idAula, estudante);
    }

    /**
     * Método de ação para o estudante reagendar uma aula que ele reservou.
     * Delega a lógica para o ServicoAgendamento.
     * 
     * @param idAula             O ID da aula a ser reagendada.
     * @param estudante          O estudante (usuário) que está reagendando.
     * @param novaDataHora       A nova data e hora desejada.
     * @param novaDuracao        A nova duração desejada.
     * @param servicoAgendamento O serviço que processará o reagendamento.
     * @return A Aula atualizada.
     */
    public Aula reagendarAula(String idAula, Estudante estudante, LocalDateTime novaDataHora, int novaDuracao,
            ServicoAgendamento servicoAgendamento) {
        return servicoAgendamento.reagendarAula(idAula, estudante, novaDataHora, novaDuracao);
    }

    /**
     * Método de ação para o estudante cancelar uma aula.
     * Delega a lógica para o ServicoAgendamento.
     * 
     * @param idAula             O ID da aula a ser cancelada.
     * @param estudante          O estudante (usuário) que está cancelando.
     * @param servicoAgendamento O serviço que processará o cancelamento.
     */
    public void cancelarAula(String idAula, Estudante estudante, ServicoAgendamento servicoAgendamento) {
        servicoAgendamento.cancelarAgendamento(idAula, estudante);
    }

    /**
     * Permite que o estudante possa avaliar uma aula após sua conclusão.
     * Delega a ação para o AvaliacaoDAO.
     *
     * @param estudante  estudante que irá avaliar
     * @param aula       aula a ser avaliada
     * @param nota       nota atribuída
     * @param comentario comentário sobre a aula (opcional)
     * @return true se a avaliação foi registrada, false caso contrário.
     */
    public boolean avaliarAula(Estudante estudante, Aula aula, int nota, String comentario) {
        return avaliacao.avaliar(estudante, aula, nota, comentario);
    }

    /**
     * Permite que o estudante possa avaliar uma aula (versão sem comentário).
     * Delega a ação para o AvaliacaoDAO.
     *
     * @param estudante estudante que irá avaliar
     * @param aula      aula a ser avaliada
     * @param nota      nota atribuída
     * @return true se a avaliação foi registrada, false caso contrário.
     */
    public boolean avaliarAula(Estudante estudante, Aula aula, int nota) {
        return avaliacao.avaliar(estudante, aula, nota);
    }

    /**
     * Verifica se o estudante já avaliou uma determinada aula.
     *
     * @param estudante o estudante que será verificado.
     * @param aula      a aula que será verificada se já foi avaliada.
     * @return true se a aula foi avaliada, false caso contrário.
     */
    public boolean jaAvaliouAula(Estudante estudante, Aula aula) {
        return avaliacao.getAvaliacaoPorEstudante(estudante).stream()
                .anyMatch(a -> a.getAula().getIdAula().equals(aula.getIdAula()));
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
