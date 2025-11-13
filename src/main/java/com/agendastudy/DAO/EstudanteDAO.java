package com.agendastudy.DAO;

import com.agendastudy.model.Aula;
import com.agendastudy.model.Estudante;

public class EstudanteDAO {
   
    // IMPLEMENTAÇÃO DE DEMAIS MÉTODOS AQUI


    /**
     * solicita o cancelamento de uma aula previamente agendada (trata-se de um protótipo, ainda falta o método de agendar)
     * @param idAula ID da aula a ser cancelada
     * @param aulaDAO DAO de aula para cancelar a aula
     */
    public void solicitarCancelamento(String idAula, AulaDAO aulaDAO){
        try{
            aulaDAO.cancelarAula(idAula);
            System.out.println("Cancelamento solicitado.");
        } catch (Exception e){
            System.err.println("Erro ao cancelar aula: " + e.getMessage());
        }
    }

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