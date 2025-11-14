package com.agendastudy.DAO;

import com.agendastudy.model.Estudante;
import com.agendastudy.model.Aula;
import com.agendastudy.service.ServicoAgendamento; // Import do serviço
import java.time.LocalDateTime;

public class EstudanteDAO extends UsuarioDAO{
   
    // IMPLEMENTAR DEMAIS MÉTODOS AQUI


    /**
     * solicita o cancelamento de uma aula previamente agendada (trata-se de um protótipo, ainda falta o método de agendar)
     * @param idAula ID da aula a ser cancelada
     * @param aulaDAO DAO de aula para cancelar a aula
     */
    /**
     * Método de ação para o estudante agendar uma aula (confirmar a reserva).
     */
    public Aula agendarAula(String idAula, Estudante estudante, ServicoAgendamento servicoAgendamento){
        return servicoAgendamento.agendarAula(idAula, estudante);
    }
    /**
     * Método de ação para o estudante reagendar uma aula que ele reservou.
     */
    public Aula reagendarAula(String idAula, Estudante estudante, LocalDateTime novaDataHora, ServicoAgendamento servicoAgendamento){
        return servicoAgendamento.reagendarAula(idAula, estudante, novaDataHora);
    }
    /**
     * Método de ação para o estudante cancelar uma aula.
     */
    public void cancelarAula(String idAula, Estudante estudante, ServicoAgendamento servicoAgendamento) {
        servicoAgendamento.cancelarAgendamento(idAula, estudante);
    }
    /* O método solicitarCancelamento antigo foi removido para evitar conflito
       e garantir que a lógica de negócio passe pelo ServicoAgendamento. */

    private AvaliacaoDAO avaliacao;

    /**
     * Permite que o estudante possa avaliar uma aula após sua conclusão
     *
     * @param estudante estudante que irá avaliar
     * @param aula aula a ser avaliada
     * @param nota nota atribuída
     * @param comentario comentário sobre a aula (opcional)
     */
    public void avaliarAula(Estudante estudante, Aula aula, int nota, String comentario) {
        avaliacao.avaliar(estudante, aula, nota, comentario);
    }
    //verão sem comentário
    public void avaliarAula(Estudante estudante, Aula aula, int nota) {
        avaliacao.avaliar(estudante, aula, nota);
    }
}
