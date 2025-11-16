package com.agendastudy.DAO;

import com.agendastudy.model.Estudante;
import com.agendastudy.model.Aula;
import com.agendastudy.service.ServicoAgendamento; // Import do serviço
import java.time.LocalDateTime;

/**
 * Gerencia as operações de acesso a dados (DAO) e ações para a entidade Estudante.
 * Delega a lógica de negócio complexa para os serviços apropriados.
 *
 * @author VINICIUS ALVES RIBEIRO SILVA
 * @version 1.0
 * @since 2025-11-11
 */
public class EstudanteDAO extends UsuarioDAO{

    /**
     * Referência para o DAO de Avaliação.
     */
    private AvaliacaoDAO avaliacao;

    /**
     * Construtor padrão da EstudanteDAO.
     *
     * Inicializa a classe EstudanteDAO criando uma nova instância interna
     * de AvaliacaoDAO para gerenciar as operações de avaliação.
     */
    public EstudanteDAO() {
        this.avaliacao = new AvaliacaoDAO();
    }

    /**
     * Construtor com parâmetro.
     *
     * Permite que a instância de AvaliacaoDAO seja fornecida externamente.
     * Ideal para testes unitários.
     *
     * @param avaliacao A instância de AvaliacaoDAO a ser utilizada.
     */
    public EstudanteDAO(AvaliacaoDAO avaliacao) {
        this.avaliacao = avaliacao;
    }

    /**
     * Método de ação para o estudante agendar uma aula (confirmar a reserva).
     * Delega a lógica para o ServicoAgendamento.
     * @param idAula O ID da aula (disponibilidade) a ser reservada.
     * @param estudante O estudante que está agendando.
     * @param servicoAgendamento O serviço que processará o agendamento.
     * @return A Aula atualizada com o estudante.-
     * 
     */
    public Aula agendarAula(String idAula, Estudante estudante, ServicoAgendamento servicoAgendamento){
        return servicoAgendamento.agendarAula(idAula, estudante);
    }
    
    /**
     * Método de ação para o estudante reagendar uma aula que ele reservou.
     * Delega a lógica para o ServicoAgendamento.
     * @param idAula O ID da aula a ser reagendada.
     * @param estudante O estudante (usuário) que está reagendando.
     * @param novaDataHora A nova data e hora desejada.
     * @param servicoAgendamento O serviço que processará o reagendamento.
     * @return A Aula atualizada.
     */
    public Aula reagendarAula(String idAula, Estudante estudante, LocalDateTime novaDataHora, ServicoAgendamento servicoAgendamento){
        return servicoAgendamento.reagendarAula(idAula, estudante, novaDataHora);
    }
    
    /**
     * Método de ação para o estudante cancelar uma aula.
     * Delega a lógica para o ServicoAgendamento.
     * @param idAula O ID da aula a ser cancelada.
     * @param estudante O estudante (usuário) que está cancelando.
     * @param servicoAgendamento O serviço que processará o cancelamento.
     */
    public void cancelarAula(String idAula, Estudante estudante, ServicoAgendamento servicoAgendamento) {
        servicoAgendamento.cancelarAgendamento(idAula, estudante);
    }

    /**
     * Permite que o estudante possa avaliar uma aula após sua conclusão.
     * Delega a ação para o AvaliacaoDAO.
     *
     * @param estudante estudante que irá avaliar
     * @param aula aula a ser avaliada
     * @param nota nota atribuída
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
     * @param aula aula a ser avaliada
     * @param nota nota atribuída
     * @return true se a avaliação foi registrada, false caso contrário.
     */
    public boolean avaliarAula(Estudante estudante, Aula aula, int nota) {
        return avaliacao.avaliar(estudante, aula, nota);
    }
}
