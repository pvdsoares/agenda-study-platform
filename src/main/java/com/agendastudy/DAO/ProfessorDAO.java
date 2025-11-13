package com.agendastudy.DAO;
public class ProfessorDAO {
    
    //IMPLEMENTAR DEMAIS MÉTODOS AQUI

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
}
